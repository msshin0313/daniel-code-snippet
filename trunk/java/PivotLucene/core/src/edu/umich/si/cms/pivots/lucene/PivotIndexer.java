package edu.umich.si.cms.pivots.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similar.MoreLikeThisQuery;
import org.apache.lucene.search.similar.SimilarityQueries;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;
import java.util.Date;

// TODO: we haven't done the comments indexing.
// TODO: haven't done the incremental indexing. (Timestamp is inconsistent among Java/PHP/MySQL/UNIX
// TODO: body Similarity

/**
 * The main class of the pivots indexer
 */
public class PivotIndexer {

    private final Logger logger = Logger.getLogger("edu.umich.si.cms.pivots.lucene.PivotIndexer");
    private final File INDEX_DIR;
    private final int PID;  // the pivot id in the database.
    private final String PROBE_TYPE;  // the probe content type
    private final String CANDIDATE_TYPE;    // the candidate content type
    private final PivotSettings settings;
    //private final String TABLE_PREFIX;  // the table prefix for drupal multi-site installation
    //private int timestamp;
    private final int INDICATOR_SIZE = 1000;

    public PivotIndexer(int pid, String probe_type, String candidate_type) {
        this.PID = pid;
        this.PROBE_TYPE = probe_type;
        this.CANDIDATE_TYPE = candidate_type;
        this.INDEX_DIR = new File("index_"+pid);
        this.settings = new PivotSettings(PID);
    }

    /**
     * Build Lucene index, not pivots index which requires query.
     * we are building comments into the index here.
     */
    public void luceneFullIndex() {
        try {
            logger.info("Start full indexing at " + new Date());
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriter writer = new IndexWriter(INDEX_DIR, analyzer, true);
            logger.info("Indexing to directory '" + INDEX_DIR + "'...");

            // generate index from database
            logger.info("Start database operations...");
            Connection conn = settings.getConnection();
            Statement nodeStmt = conn.createStatement();
            int count =0;
            // we only build index on candidate_type
            ResultSet nodeRs = nodeStmt.executeQuery(
                    "SELECT n.nid, n.title, r.body FROM node n INNER JOIN node_revisions r " +
                    "ON n.nid=r.nid AND n.vid=r.vid WHERE n.type='" + CANDIDATE_TYPE + "'");
            PreparedStatement commentStmt = conn.prepareStatement(
                    "SELECT subject, comment FROM comments WHERE nid = ?");
            while (nodeRs.next()) {
                Document doc = new Document();
                doc.add(new Field("nid", nodeRs.getString("nid"), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field("title", nodeRs.getString("title"), Field.Store.YES, Field.Index.TOKENIZED));
                doc.add(new Field("body", nodeRs.getString("body"), Field.Store.YES, Field.Index.TOKENIZED));

                // add comments
                commentStmt.setString(1, nodeRs.getString("nid"));
                ResultSet commentRs = commentStmt.executeQuery();
                StringBuffer comments = new StringBuffer();
                while (commentRs.next()) {
                    comments.append(commentRs.getString("subject")).append("\n");
                    comments.append(commentRs.getString("comment")).append("\n");
                }
                if (comments.length()>0)
                    doc.add(new Field("comments", comments.toString(), Field.Store.YES, Field.Index.TOKENIZED));

                logger.finer("-> add document '" + nodeRs.getString("title") + "'...");
                writer.addDocument(doc);
                count++;
                if (count%INDICATOR_SIZE == 0)
                    logger.fine("Indexing records count: " + count);
            }
            logger.info("Finish database operations.");
            settings.closeConnection(conn);

            logger.info("Optimizing...");
            writer.optimize();
            writer.close();
            logger.info("Finish full indexing at " + new Date());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void luceneIncrementalIndex() {

    }

    public void pivotFullIndex() {
        try {
            logger.info("Start pivot full index based on Lucene index files.");
            IndexReader reader = IndexReader.open(INDEX_DIR);
            Searcher searcher = new IndexSearcher(reader);
            // TODO: review the following settings. It's not queried as "Phrase", but dismangled asa words connecting by OR.
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "body"}, analyzer);

            // retrieve probe from database.
            logger.info("Start database operations...");
            Connection conn = settings.getConnection();
            // clean the obsolete data since we are doing full index.
            pivotCleanTable(conn);

            Statement stmt = conn.createStatement();
            // we only retrieve probe_type to be used in queries.
            ResultSet rs = stmt.executeQuery(
                    "SELECT n.nid, n.title, r.body FROM node n INNER JOIN node_revisions r " +
                    "ON n.nid=r.nid AND n.vid=r.vid WHERE n.type='" + PROBE_TYPE + "'");
            while (rs.next()) {
                int probe_nid = rs.getInt("nid");
                String probe_title = rs.getString("title");
                Query query = parser.parse(probe_title);
                Hits hits = searcher.search(query);

                for (int i=0; i<hits.length(); i++) {
                    Document doc = hits.doc(i);
                    int candidate_nid = Integer.parseInt(doc.get("nid"));
                    float score = hits.score(i);
                    pivotInsertRecord(conn, probe_nid, candidate_nid, score);
                }
            }
            logger.info("Finish database operations.");
            settings.closeConnection(conn);

        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public /*List<SimpleNode>*/ void directLuceneQuery() {
        //List<SimpleNode> result = new ArrayList<SimpleNode>();
        try {
            IndexReader reader = IndexReader.open(INDEX_DIR);
            Searcher searcher = new IndexSearcher(reader);

            // construction query

            Query query = new TermQuery(new Term("comments", "shoot"));

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



            Hits hits = searcher.search(query);
            for (int i=0; i<hits.length(); i++) {
                Document doc = hits.doc(i);
                int nid = Integer.parseInt(doc.get("nid"));
                String title = doc.get("title");
                String body = doc.get("body");
                float score = hits.score(i);
                System.out.printf("%3d: %s (%5.3f)\n(%s)\n\n", nid, title, score, body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return result;
    }

    private void pivotCleanTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM pivots_async_node WHERE pivot_id=" + PID);
    }

    private void pivotInsertRecord(Connection conn, int src_id, int dest_id, float score) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pivots_async_node WHERE src_id=? AND dest_id=? AND pivot_id=?");
        PreparedStatement insertStmt = conn.prepareStatement("INSERT pivots_async_node(src_id, dest_id, pivot_id, score) VALUES(?, ?, ?, ?)");

        deleteStmt.setInt(1, src_id);
        deleteStmt.setInt(2, dest_id);
        deleteStmt.setInt(3, PID);
        deleteStmt.executeUpdate();

        insertStmt.setInt(1, src_id);
        insertStmt.setInt(2, dest_id);
        insertStmt.setInt(3, PID);
        insertStmt.setFloat(4, score);
        insertStmt.executeUpdate();

        deleteStmt.setInt(2, src_id);
        deleteStmt.setInt(1, dest_id);
        deleteStmt.setInt(3, PID);
        deleteStmt.executeUpdate();

        insertStmt.setInt(2, src_id);
        insertStmt.setInt(1, dest_id);
        insertStmt.setInt(3, PID);
        insertStmt.setFloat(4, score);
        insertStmt.executeUpdate();
    }
}
