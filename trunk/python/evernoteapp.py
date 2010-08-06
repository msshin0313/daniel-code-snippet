# coding: utf8
# utility program for evernote

#
# To run (Unix):
#   export PYTHONPATH=../../lib/python; python EDAMTest.py myuser mypass
#

import sys, hashlib, time, ConfigParser
import thrift.protocol.TBinaryProtocol as TBinaryProtocol
import thrift.transport.THttpClient as THttpClient
import evernote.edam.userstore.UserStore as UserStore
import evernote.edam.userstore.constants as UserStoreConstants
import evernote.edam.notestore.NoteStore as NoteStore
import evernote.edam.type.ttypes as Types
import evernote.edam.notestore.ttypes as NoteStoreTypes

class EvernoteApp:

  def __init__(self):
    config = ConfigParser.ConfigParser()
    config.read('settings.cfg')
    section = 'evernote_sandbox'
    c = lambda x: config.get(section, x)

    # access user store
    userStoreHttpClient = THttpClient.THttpClient(c('user_store'))
    userStoreProtocol = TBinaryProtocol.TBinaryProtocol(userStoreHttpClient)
    userStore = UserStore.Client(userStoreProtocol)

    # check version
    versionOK = userStore.checkVersion("Python EDAMTest", UserStoreConstants.EDAM_VERSION_MAJOR, UserStoreConstants.EDAM_VERSION_MINOR)
    if not versionOK:
      print "Is my EDAM protocol version up to date? ", str(versionOK)
      exit(1)

    # authenticate
    authResult = userStore.authenticate(c('username'), c('password'), c('consumer_key'), c('consumer_secret'))
    user = authResult.user
    authToken = authResult.authenticationToken
    #print "Authentication was successful for ", user.username
    #print "Authentication token = ", authToken

    # access note store
    noteStoreUri =  c('note_store') + user.shardId
    noteStoreHttpClient = THttpClient.THttpClient(noteStoreUri)
    noteStoreProtocol = TBinaryProtocol.TBinaryProtocol(noteStoreHttpClient)
    noteStore = NoteStore.Client(noteStoreProtocol)

    # set object variables
    self.authToken = authToken
    self.noteStore = noteStore


  def runTask(self):
    notebooks = self.noteStore.listNotebooks(self.authToken)
    print "Found ", len(notebooks), " notebooks:"
    for notebook in notebooks:
      print "**", notebook.name, '--', notebook.guid
      #if notebook.defaultNotebook:
      #    defaultNotebook = notebook


class CreateImageNote(EvernoteApp):

  def runTask(self):
    # Create a note with one image resource in it ...
    image = open('enlogo.png', 'rb').read()
    md5 = hashlib.md5()
    md5.update(image)
    hashHex = md5.hexdigest()

    data = Types.Data()
    data.size = len(image)
    data.bodyHash = hashHex
    data.body = image

    resource = Types.Resource()
    resource.mime = 'image/png'
    resource.data = data

    note = Types.Note()
    defaultNotebook = self.noteStore.getDefaultNotebook(self.authToken)
    note.notebookGuid = defaultNotebook.guid
    note.title = "Another test note from EDAMTest.py"
    note.content = '<?xml version="1.0" encoding="UTF-8"?>'
    note.content += '<!DOCTYPE en-note SYSTEM "http://xml.evernote.com/pub/enml.dtd">'
    note.content += '<en-note>Here is the Evernote logo:<br/>'
    note.content += '<en-media type="image/png" hash="' + hashHex + '"/>'
    note.content += '</en-note>'
    note.created = int(time.time() * 1000)
    note.updated = note.created
    note.resources = [ resource ]

    createdNote = self.noteStore.createNote(self.authToken, note)
    print "Created note: ", str(createdNote)


class ListNotes(EvernoteApp):
  def runTask(self):
    notebook = self.noteStore.getDefaultNotebook(self.authToken)
    filter = NoteStoreTypes.NoteFilter()
    filter.notebookGuid = notebook.guid
    noteList = self.noteStore.findNotes(self.authToken, filter, 0, 10)
    for note in noteList.notes:
      print note.guid
      print note.title
      print self.noteStore.getNoteContent(self.authToken, note.guid)
      #print note.contentLength


if __name__ == '__main__':
  app = ListNotes()
  app.runTask()

