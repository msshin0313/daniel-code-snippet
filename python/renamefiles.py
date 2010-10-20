# coding: utf8
'''
Created on Aug 8, 2010
@author: Daniel
'''

import os, os.path

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
  

if __name__ == '__main__':
  #rename_by_list('M:/list.txt', 'M:/tonghua')
  #remove_prefix(r'C:\Users\Daniel\Desktop\a', u'mp3_正说清朝二十四臣')
  index_files('N:/a')