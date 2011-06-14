package test;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: MRZHOU
 * Date: Aug 19, 2007
 * Time: 12:27:52 PM
 * To change this template use File | Settings | File Templates.
 */

public class ServletDemo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action==null || action.equals("hello")) {
            response.getWriter().print("Hello, world.");
            response.getWriter().close();
        } else if (action.equals("forward")) {
            response.sendRedirect("http://www.google.com");
        }
    }
}
