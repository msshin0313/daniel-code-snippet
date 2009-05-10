/**
 * Created by IntelliJ IDEA.
 * User: Daniel
 * Date: May 9, 2009
 * Time: 8:02:18 PM
 * To change this template use File | Settings | File Templates.
 */

srcDir = new File("C:\\Work\\3")
destFile = new File("C:\\Work\\3.txt")

count = 1;
srcDir.eachFile {
  destFile << count << '. ' << it.getName() << '\n'
  count++
}

destFile << '\n'*5

count = 1;
srcDir.eachFile { file ->
  println 'processing file ' << count << ' ' << file.getName()
  destFile << '\n'*2 << '*'*10 <<'\n'
  destFile << count << '. ' << file.getName() << '\n'
  count++
  file.eachLine { line ->
    destFile << line.replace('$', '').trim() << '\n'
  }
}