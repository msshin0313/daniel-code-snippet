package hb;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Iterator;


public class Main {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration().configure();
            SessionFactory sessFactory = conf.buildSessionFactory();
            Session sess = sessFactory.openSession();
            //Transaction tx = sess.beginTransaction();
            //insert(sess);
            //getOne(sess);
            //getMany(sess);
            //getMouseCat(sess);
            Cat c = (Cat)sess.get(Cat.class, "2c908b08fc21085100fc210857680001");
            Iterator iter = c.getMiceBread().iterator();
            while (iter.hasNext()) {
                System.out.println(((Mouse)iter.next()).getName());
            }
            //tx.commit();
            sess.close();
        } catch (ValidationFailure e) {
            e.printStackTrace();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    private static void getMouseCat(Session sess) throws HibernateException {
        List mice = sess.find("from Mouse");
        for (int i=0; i<mice.size(); i++) {
            Mouse m = (Mouse) mice.get(i);
            Cat c = m.getEatenBy();
            if (c!=null) System.out.println(m.getName() + " is eaten by " + c.getName());
            else System.out.println(m.getName() + "is not eaten by any cat");
        }
    }

    private static void getMany(Session sess) throws HibernateException {
        Query q = sess.getNamedQuery("hb.Cat.return.all");
        List cats = q.list();
        for (int i=0; i<cats.size(); i++) {
            Cat cat = (Cat) cats.get(i);
            System.out.println(cat.getId());
            System.out.println(cat.getName());
        }
    }

    private static void getOne(Session sess) throws HibernateException {
        Cat cat = (Cat)sess.get(Cat.class, "2c908b08fc21085100fc210857680001");
        System.out.println(cat.getName());
        sess.delete(cat);
    }

    private static void insert(Session sess) throws HibernateException {
        Cat newborn = new Cat();
        newborn.setName("nancy");
        newborn.setSex('f');
        newborn.setWeight(80);
        sess.save(newborn);
    }
}
