package edu.umich.si.cms.pivots.lucene;

import java.util.Properties;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Command line control
 */
public class CmdControl {
    public static void main(String[] args) throws Exception {
        runIndexer();
        //runMatcher();
        //testProperties();
        //directLuceneQuery();
    }

    private static void directLuceneQuery() {
        PivotIndexer pivotIndexer = new PivotIndexer(5, "object", "forum");
        pivotIndexer.directLuceneQuery();
    }

    private static void testProperties() throws IOException {
        //Properties prop = new Properties();
        //prop.setProperty("connection", "jdbc:mysql://localhost/pivots?user=root");
        //prop.setProperty("string", "jdbc:mysql://localhost/pivots?user=root");
        //prop.storeToXML(new FileOutputStream("config.xml"), "Configuration file");
    }

    private static void runMatcher() {
        PivotMatcher pivotMatcher = new PivotMatcher(5, "http://localhost/pivot/?q=");
        System.out.println(pivotMatcher.matchReturnViewableContent(2, 5));
    }

    private static void runIndexer() {
        //PivotIndexer pivotIndexer = new PivotIndexer(9901, "module_demo", "forum");
        //PivotIndexer pivotIndexer = new PivotIndexer(5, "object", "forum");
        PivotIndexer pivotIndexer = new PivotIndexer(9999, "project_project", "forum");
        pivotIndexer.luceneFullIndex();
        //pivotIndexer.pivotFullIndex();
    }
}
