package com.example;



import com.example.entity.Address;
import com.example.entity.Cat;
import com.example.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.List;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        SessionFactory sessionFactory = getSessionFactory();

        Random random = new Random();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Address adres = new Address("Vovka","54323", "London");
        User user = new User("Tom","tom@cat.miy","Hrrr");

        Long curr = System.currentTimeMillis();
        Set<Address> sets = new HashSet<>(1000);
        for (int i = 0; i < 2_000; i++) {
            Address temp = new Address("Summer "+i, ""+random.nextInt(100000), "Liberty" );
            temp.setUser(user);
            session.save(temp);

        }

         user.setAddresses(sets);
        long userId = (Long) session.save(user);

        session.getTransaction().commit();

//        session.flush();
        session.close();

        Thread.sleep(1000);

        curr = System.currentTimeMillis();
        session = sessionFactory.openSession();
        session.beginTransaction();
        User userDB = session.get(User.class, userId);

        session.getTransaction().commit();
        session.close();
        System.out.println(" ------------------------------------- Current time "+(System.currentTimeMillis() - curr));
        selectAddresses(10, 30);


        sessionFactory.close();
    }

    private static void selectAddresses(int from, int count){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Address.class);
        Criteria criteria1 = session.createCriteria(Address.class);
        List<Address> addresses = criteria.setFirstResult(from).setMaxResults(count).list();
        List<Address> addresses1 = criteria1.list();
        for (Address address : addresses1) {
            System.out.println(address);
        }
    }


    private static SessionFactory getSessionFactory() {

        Configuration configuration = new Configuration().configure();

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()

                .configure();

        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

        return sessionFactory;

    }


}
