import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) throws Exception {
        //String str = "BookXMLMain.asp-BookName=darshan+diaries-get out of your own way.txt.htm";
        File srcdir = new File("D:\\onlinebooks");
        //File dstdir = new File("D:\\osho");
        //Pattern pattern = Pattern.compile("BookXMLMain\\.asp-BookName=.++-.++\\.txt\\.htm");
        Pattern pattern = Pattern.compile("BookXMLMain\\.asp-BookName=(.+?)-(.+?)\\.txt\\.htm");
        //Matcher aa = pattern.matcher(str);
        //System.out.println(aa.matches());
        for (File infile : srcdir.listFiles()) {
            String bookname = infile.getName();
            Matcher m = pattern.matcher(bookname);
            if (m.matches()) {
                File xmlfile = new File("D:\\osho\\"+m.group(1)+'\\'+m.group(2)+".xml");
                xmlfile.createNewFile();
                BufferedReader reader = new BufferedReader(new FileReader(infile));
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(xmlfile)));
                writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                writer.println("<?xml-stylesheet type=\"text/xsl\" href=\"osho.xsl\"?>");
                String line = null;
                boolean wflag = false;
                boolean exitflag = false;
                while ((line=reader.readLine()) != null) {
                    if (line.equals("<xml id=\"docBookXML\">")) {
                        wflag = true;
                        continue;
                    }
                    if (line.equals("   </xml>")) {
                        exitflag = true;
                        break;
                    }
                    if (wflag == true) {
                        writer.println(line);
                    }
                }
                reader.close();
                writer.close();
                if (wflag != true || exitflag !=true) System.out.println("HANDLING ERROR: "+xmlfile);
            } else {
                System.out.println("NOT OPEN: "+bookname);
            }
        }
    }
}
