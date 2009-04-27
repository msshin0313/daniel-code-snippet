import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import static org.apache.lucene.index.IndexWriter.MaxFieldLength.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.*;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class LuceneDemo {

    public static void main(String[] args) throws Exception {
        LuceneDemo luceneDemo = new LuceneDemo();
        luceneDemo.index();
        luceneDemo.query();
    }

    private final File TEXTDIR = new File("TEXT");
    private final File INDEXDIR = new File("INDEX");

    public void index() throws Exception {
        //System.out.println(TEXTDIR.isDirectory());
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriter indexWriter = new IndexWriter(INDEXDIR, analyzer, UNLIMITED);
        for (File file : TEXTDIR.listFiles()) {
            FileReader reader = new FileReader(file);
            Document doc = new Document();
            doc.add(new Field("title", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("body", reader));
            indexWriter.addDocument(doc);
            reader.close();
        }
        indexWriter.optimize();
        indexWriter.close();
    }

    public void query() throws Exception {
        IndexReader indexReader = IndexReader.open(INDEXDIR);
        Searcher searcher = new IndexSearcher(indexReader);

        // queries
        Query query = new TermQuery(new Term("body", "porter"));
        //Query query = new FuzzyQuery(new Term("body", "apple"));
        //Query query = new MoreLikeThisQuery("apple fruit", new String[]{"title", "body"}, new StandardAnalyzer());
        //Query query = SimilarityQueries.formSimilarQuery("apple fruit", new StandardAnalyzer(), "body", null);

        /*PhraseQuery query = new PhraseQuery();
        query.add(new Term("title", "object"));
        query.add(new Term("title", "apple"));*/

        /*Query query1 = new TermQuery(new Term("title", "apple"));
        Query query2 = new TermQuery(new Term("body", "apple"));
        BooleanQuery query = new BooleanQuery();
        query.add(new BooleanClause(query1, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(query2, BooleanClause.Occur.MUST_NOT));*/

        // NOTE: the following code works strange.
        // The logic is very strange anyway. They don't even use getter/setters....
        TopDocCollector collector = new TopDocCollector(10);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        for (int i=0; i<hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            String title = doc.get("title");
            System.out.println("Hits: " + title + ", " + hits[i].score);
        }
    }
}
