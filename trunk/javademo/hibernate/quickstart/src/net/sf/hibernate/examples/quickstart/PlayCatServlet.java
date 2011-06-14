/**
 * Created by IntelliJ IDEA.
 * User: zhou_xiaodan
 * Date: 2004-4-23
 * Time: 16:33:54
 * To change this template use File | Settings | File Templates.
 */
package net.sf.hibernate.examples.quickstart;

import net.sf.hibernate.Transaction;
import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class PlayCatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String ssex = request.getParameter("sex");
        String sweight = request.getParameter("weight");
        if (name==null || ssex==null || sweight==null) throw new IllegalArgumentException();
        char sex = ssex.charAt(0);
        float weight = Float.parseFloat(sweight);
        try {
            addCat(name, sex, weight);
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCat(String name, char sex, float weight) throws HibernateException {
        Session session = HibernateUtil.currentSession();

        Transaction tx = session.beginTransaction();
        Cat newborn = new Cat();
        newborn.setName(name);
        newborn.setSex(sex);
        newborn.setWeight(weight);
        session.save(newborn);
        tx.commit();

        HibernateUtil.closeSession();
    }
}
