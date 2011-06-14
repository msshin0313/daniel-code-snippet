package edu.umich.si.cms.pivots.lucene;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

/**
 * HTTP control interface
 */

public class HttpControl extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");
        if (!key.equals("wolverine")) return; // TODO: more complex mechanism needed.

        String action = request.getParameter("action");
        if (action==null) return;

        Map<String, String[]> parameters = (Map<String, String[]>) request.getParameterMap();
        String content = null;
        if (action.equalsIgnoreCase("FullIndex")) {
            content = fullIndex(parameters);
        } else if (action.equalsIgnoreCase("IncrementIndex")) {
            content = incrementalIndex(parameters);
        } else if (action.equalsIgnoreCase("query")) {
            content = query(parameters);
        } else if (action.equalsIgnoreCase("loadjs")) { // load on-demand javascript
            content = loadjs(parameters, response);
        }

        if (content == null) {
            content = "";
        }
        response.getWriter().print(content);
        response.getWriter().close();
    }

    private String loadjs(Map<String, String[]> parameters, HttpServletResponse response) {
        String content = query(parameters);
        String js = "setContent('" + StringEscapeUtils.escapeJavaScript(content) + "');";
        response.setContentType("application/x-javascript");
        return js;
    }

    private String query(Map<String, String[]> parameters) {
        String result = null;
        try {
            int pid = Integer.parseInt(parameters.get("pid")[0]);
            String url_prefix = (parameters.get("prefix")[0]);
            int nid = Integer.parseInt(parameters.get("nid")[0]);
            int limit = Integer.parseInt(parameters.get("limit")[0]);
            PivotMatcher matcher = new PivotMatcher(pid, url_prefix);
            result = matcher.matchReturnViewableContent(nid, limit);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String incrementalIndex(Map<String, String[]> parameters) {
        return null;
    }

    private String fullIndex(Map<String, String[]> parameters) {
        return null;
    }
}
