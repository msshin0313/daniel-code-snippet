package test;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Transaction;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

public class MainServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String name = req.getParameter("name");

        if (name == null) {
            out.println("Please specify name");
        } else {
            Cat cat = new Cat();
            cat.setName(name);
            try {
                Session session = HibernateUtil.currentSession();
                Transaction trans = session.beginTransaction();
                session.save(cat);
                trans.commit();
                out.println("A new cat "+name+" has been saved.");
            } catch (HibernateException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                throw new ServletException("Hibernate Exception");
            } finally {
                try {
                    HibernateUtil.closeSession();
                } catch (HibernateException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    throw new ServletException("Hibernate Exception during close phase");
                }
            }
        }
        out.flush();
        out.close();
    }
}
