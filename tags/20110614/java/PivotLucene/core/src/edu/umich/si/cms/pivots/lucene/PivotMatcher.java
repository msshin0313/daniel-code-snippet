package edu.umich.si.cms.pivots.lucene;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Match given node, return related nodes
 */
public class PivotMatcher {
    private final int PID;
    private final String URL_PREFIX;
    private final PivotSettings settings;

    public PivotMatcher(int pid, String prefix) {
        this.PID = pid;
        this.URL_PREFIX = prefix;
        this.settings = new PivotSettings(PID);
    }

    /**
     * Generate viewable results.
     * @param probe_nid The node id to be matched.
     * @param limitNum The maximum num of items returned; 0=unlimited.
     * @return The HTML block for the result.
     */
    public String matchReturnViewableContent(int probe_nid, int limitNum) {
        StringBuffer content = new StringBuffer();
        List<SimpleNode> results = match(probe_nid);
        if (results.size()==0) return "N/A";

        boolean offLimit = false;
        if (limitNum > 0 && results.size() > limitNum) {
            offLimit = true;
            results = results.subList(0, limitNum);
        }

        content.append("<div class=\"item-list\"><ul>");
        for (SimpleNode node : results) {
            content.append("<li><a href=\"").append(node.getLink()).append("\">")
                    .append(node.getTitle()).append("</a></li>");
        }
        content.append("</ul></div>");
        if (offLimit) {
            content.append("<div class=\"more-link\"><a href=\"").append(URL_PREFIX).append("pivots/")
                    .append(PID).append("/").append(probe_nid).append("\" title=\"More entries.\">more</a></div>");
        }

        return content.toString();
    }

    public List<SimpleNode> match(int probe_nid) {
        List<SimpleNode> results = new ArrayList<SimpleNode>();

        try {
            Connection conn = settings.getConnection();
            Statement stmt = conn.createStatement();
            // we only build index on candidate_type
            ResultSet rs = stmt.executeQuery(
                    "SELECT n.nid, n.title FROM node n INNER JOIN pivots_match p" +
                    " ON n.nid=p.dest_id WHERE p.src_id=" + probe_nid + " AND p.pivot_id=" + PID +
                    " AND p.dest_id<>" + probe_nid + " ORDER BY score DESC, timestamp DESC");
            while (rs.next()) {
                int nid = rs.getInt("nid");
                String title = rs.getString("title");
                String link = URL_PREFIX+"node/"+nid;
                results.add(new SimpleNode(nid, title, link));
            }
            settings.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return results;
    }
}
