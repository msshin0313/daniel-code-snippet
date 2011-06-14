这个程序的作用是消除某个目录下的目录结构。

比如想消除c:\root下的目录结构，则c:\root\folder\file.txt会被移动为c:\root\folder_file.txt文件；所有的空子目录都被删除。这样，c:\root下不含目录，全部是文件。

该程序考虑到了命名冲突的问题，比如\folder\file文件改名成\folder_file可能正好同一个现有文件冲突，本程序则自动扩展为\folder_file_1。若有扩展名，则跳过扩展名。比如，\folder\file.txt转换为\folder_file_1.txt。

出于安全考虑，本程序只能运行再Windows下，运行时会自动检查。另外，用户要保证有足够的权限操作文件，否则返回RuntimeException。

写代码时参照极限编程准则，没大写注释，最好不要再修改代码了。