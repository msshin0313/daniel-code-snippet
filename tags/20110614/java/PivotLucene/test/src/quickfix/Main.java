package quickfix;

import Jama.Matrix;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String conn_string = "jdbc:mysql://localhost/stat_perl?user=root";
        Connection conn = DriverManager.getConnection(conn_string);


        // create thread
        /*final String SEPERATOR = "\n\n\n----\n\n\n";
        Statement stmtForum = conn.createStatement();
        ResultSet rsForum = stmtForum.executeQuery("select nid, title, body from _forum");
        while (rsForum.next()) {
            StringBuffer sb = new StringBuffer();
            String idForum = rsForum.getString("nid");
            sb.append(rsForum.getString("title")).append(SEPERATOR);
            sb.append(rsForum.getString("body")).append(SEPERATOR);

            Statement stmtComment = conn.createStatement();
            ResultSet rsComment = stmtComment.executeQuery("select subject, comment from comments where nid="+idForum);
            while (rsComment.next()) {
                sb.append(rsComment.getString("subject")).append(SEPERATOR);
                sb.append(rsComment.getString("comment")).append(SEPERATOR);
            }
            stmtComment.close();

            PreparedStatement stmtUpdate = conn.prepareStatement("insert into _thread(nid, body) values(?, ?)");
            stmtUpdate.setString(1, idForum);
            stmtUpdate.setString(2, sb.toString());
            stmtUpdate.executeUpdate();
            stmtUpdate.close();
        }
        stmtForum.close();*/


        // match module
        // pivot_id: 1001. for modules and themes.
        /*Statement probeStmt = conn.createStatement();
        ResultSet probeRs = probeStmt.executeQuery("select nid, title from _theme");
        while (probeRs.next()) {
            String probeNid = probeRs.getString("nid");
            String probeTitle = StringEscapeUtils.escapeSql(probeRs.getString("title"));
            String magicword = "theme";
            String regex1 = "'%"+magicword+" "+probeTitle+"%'";
            String regex2 = "'%"+probeTitle+" "+magicword+"%'";
            Statement updt1 = conn.createStatement();
            updt1.executeUpdate("INSERT INTO _match(pivot_id, src_id, dest_id, score) "+
                 "SELECT 1001, "+probeNid+", nid, timestamp FROM _thread WHERE body LIKE "+regex1+" OR body LIKE "+regex2);
            Statement updt2 = conn.createStatement();
            updt2.executeUpdate("INSERT INTO _match(pivot_id, dest_id, src_id, score) "+
                 "SELECT 1001, "+probeNid+", nid, timestamp FROM _thread WHERE body LIKE "+regex1+" OR body LIKE "+regex2);
            updt1.close();
            updt2.close();
        }*/

        // double pivots.
        ResultSet rs1 = conn.createStatement().executeQuery("SELECT count(*) FROM _project_in_match");
        rs1.next();
        int projectCount = rs1.getInt(1);
        ResultSet rs2 = conn.createStatement().executeQuery("SELECT count(*) FROM _thread_in_match");
        rs2.next();
        int threadCount = rs2.getInt(1);
        ResultSet pset = conn.createStatement().executeQuery("SELECT i, nid FROM _project_in_match");
        int[] ptable = new int[projectCount];
        while (pset.next()) {
            ptable[pset.getInt("i")-1] = pset.getInt("nid");
        }
        pset.close();
        double[][] data = new double[projectCount][threadCount];
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT p, t FROM _project_thread");
        while (rs.next()) {
            int p = rs.getInt("p");
            int t = rs.getInt("t");
            data[p-1][t-1] = 1;
        }
        Matrix matrix = new Matrix(data);
        Matrix result = matrix.times(matrix.transpose());
        assert(result.getRowDimension()==projectCount);
        assert(result.getColumnDimension()==projectCount);
        double[][] rdata = result.getArray();
        PreparedStatement update = conn.prepareStatement("INSERT INTO pivots_match(pivot_id, src_id, dest_id, score) VALUES(1002, ?, ?, ?)");
        for (int i=0; i<projectCount; i++) {
            for (int j=0; j<projectCount; j++) {
                int score = (int)Math.round(rdata[i][j]);
                if (score!=0) {
                    update.setInt(1, ptable[i]);
                    update.setInt(2, ptable[j]);
                    update.setInt(3, score);
                    update.executeUpdate();
                }
            }
        }

        conn.close();
    }

}
