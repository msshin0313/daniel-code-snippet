# coding: utf8
'''
Created on Aug 8, 2010
@author: Daniel
'''

import os, os.path, random, tempfile, shutil, re

# rename the files in 'dir' (ordered by name) according to the new file names in 'renamelist'
# used to rename '52篇童话'
def rename_by_list(renamelist, dir):
  infile = open(renamelist, 'r')
  newnames = [l.strip() for l in infile.readlines()]
  infile.close()
  oldnames = os.listdir(dir)
  assert len(newnames) == len(oldnames)
  oldnames.sort()
  os.chdir(dir)
  for i in range(len(oldnames)):
    oldname = oldnames[i]
    # handle surfix
    surfix = oldname[oldname.rindex('.'):]
    newname = newnames[i]+surfix
    print oldname, newname
    os.rename(oldname, newname)

# remove prefix of file names
def remove_prefix(dir, prefix):
  os.chdir(dir)
  oldnames = os.listdir(dir)
  for oldname in oldnames:
    newname = oldname[len(prefix):]
    print oldname, newname
    os.rename(oldname, newname)
    

# index files as 01...999
def index_files(dir):
  os.chdir(dir)
  oldnames = os.listdir(dir)
  oldnames.sort()
  count = 1
  for oldname in oldnames:
    surfix = oldname[oldname.rindex('.'):]
    newname = "%d%s" % (count, surfix)
    print oldname, newname
    os.rename(oldname, newname)
    count += 1
  
  
# create temporary files/dirs in temp folder
def create_temp():
  #assert not os.path.exists(dir)
  root = tempfile.mkdtemp()
  # create first level
  a1 = tempfile.mkdtemp(dir=root)
  a2 = tempfile.mkdtemp(dir=root)
  a3 = tempfile.mkstemp(dir=root)
  a4 = tempfile.mkstemp(dir=root)
  # create second level
  b11 = tempfile.mkdtemp(dir=a1)
  b12 = tempfile.mkdtemp(dir=a1)
  b21 = tempfile.mkstemp(dir=a2)
  b22 = tempfile.mkstemp(dir=a2)
  # create final level
  c111 = tempfile.mkstemp(dir=b11)
  c112 = tempfile.mkstemp(dir=b11)
  return root


def flatten_dir(topdir, delim='$'):
  assert os.path.exists(topdir) and os.path.isdir(topdir)
  topdir = os.path.normpath(topdir) # remove the trailing '\'
  show_dir_info(topdir)
  ok = raw_input("Are you sure to continue? Press y to continue:")
  if ok != 'y':
    print "OK. Nothing is done. Exit."
    return
  for root, dirs, files in os.walk(topdir, topdown=False):
    if root != topdir: # only handle the files in the subfolders. don't touch files on the top level.
      for name in files:
        oldname = os.path.join(root, name)
        t = re.split(r'[\\/]', oldname[len(topdir)+1:])
        # note he join is for topdir
        newname = os.path.join(topdir, delim.join(t))
        #print oldname, newname
        shutil.move(oldname, newname)
    for name in dirs:
      os.rmdir(os.path.join(root, name))
  show_dir_info(topdir)
  print "Finished processing."
      
# append xxxxxx$ to the files, the numbers are randomized.
def randomize_index_files(topdir, delim='@'):
  assert os.path.exists(topdir) and os.path.isdir(topdir) and len(delim)==1, dir
  old_files = os.listdir(topdir)
  assert len(old_files) > 0, "Dir empty"
  prefix = ["%06d"%i for i in range(len(old_files))]
  random.shuffle(prefix)
  new_files = []
  for f in old_files:
    if not os.path.isfile(os.path.join(topdir, f)):
      print "Please use flatten_dir() first. The dir can't have sub-dirs."
      print "Nothing is done. Exit."
      return
    m = re.match('[0-9]{6}'+delim+'(.+)', f)
    if m != None: f = m.group(1)
    new_files.append(prefix[0]+delim+f)
    del prefix[0]
  assert len(old_files) == len(new_files)
  show_dir_info(topdir)
  ok = raw_input("Randomize files? Press y to continue:")
  if ok != 'y':
    print "OK. Nothing is done. Exit."
    return
  for i in range(len(old_files)):
    shutil.move(os.path.join(topdir, old_files[i]), os.path.join(topdir, new_files[i]))
  print "Randomizing finish successfully."


# display dir information. show total # of dirs/files. and randomly display 10.  
def show_dir_info(dir):
  assert os.path.exists(dir) and os.path.isdir(dir), dir
  print "Show info for dir:", dir
  total_dirs, total_files = [], []
  for root, dirs, files in os.walk(dir):
    #print sum(os.path.getsize(os.path.join(root, name)) for name in files)
    dirs = [os.path.join(root, name)+os.sep for name in dirs]
    files = [os.path.join(root, name) for name in files]
    total_dirs += dirs
    total_files += files
  print "Total dirs:", len(total_dirs), "--", "Total files:", len(total_files)
  combined = total_dirs + total_files
  random.shuffle(combined)
  print "10 random files/dirs:"
  for c in combined[:10]: print c
    

if __name__ == '__main__':
  #rename_by_list('M:/list.txt', 'M:/tonghua')
  #remove_prefix(r'C:\Users\Daniel\Desktop\a', u'mp3_正说清朝二十四臣')
  #index_files('N:/a')
  #show_dir_info(r'N:/+Data')
  #t = create_temp()
  #flatten_dir(r'c:\users\daniel\appdata\local\temp\tmpozpzku')
  randomize_index_files(r'c:\users\daniel\appdata\local\temp\tmpozpzku')