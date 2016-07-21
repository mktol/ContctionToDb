package com.example;


import com.example.entity.Address;
import com.example.entity.User;
//import org.hibernate.Criteria;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
//import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;


import javax.persistence.Query;
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
        Address adres = new Address("Vovka", "54323", "London");
        User user = new User("Tom", "tom@cat.miy", "Hrrr");

        Long curr = System.currentTimeMillis();
        Set<Address> sets = new HashSet<>(1000);
        for (int i = 0; i < 2_000; i++) {
            Address temp = new Address("Summer " + i, "" + random.nextInt(100000), "Liberty");
            temp.setUser(user);
            session.save(temp);

        }

        user.setAddresses(sets);
        long userId = (Long) session.save(user);
        selectAddresses(session, userId);
        selectAddresses(session, 10, 30);
        selectAddressByNativeSql(session);
        updateUserNameByNativeSql(session, "Tiger", userId);

        Address address = getAddress(session, 34L);


        session.getTransaction().commit();

        session.close();

        Thread.sleep(1000);

        curr = System.currentTimeMillis();
        session = sessionFactory.openSession();
        session.beginTransaction();
        User userDB = session.get(User.class, userId);

        session.getTransaction().commit();
        session.close();
        System.out.println(" ----- Current time " + (System.currentTimeMillis() - curr));


        sessionFactory.close();
    }

    /**
     * Select addresses from Db by Criteria API
     *
     * @param session hibernate session
     * @param from    begin element
     * @param count   count of elements
     */
    private static List<Address> selectAddresses(Session session, int from, int count) {
        Criteria criteria = session.createCriteria(Address.class);
        List<Address> addresses = criteria.setFirstResult(from).setMaxResults(count).list();
        for (Address address : addresses) {
            System.out.println(address);
        }
        return addresses;
    }


    private static Address getAddress(Session session, Long id){
        Query query = session.createQuery("from Address where id = :inputId");
        query.setParameter("inputId", id);
        Address address = (Address) query.getSingleResult();
        return address;
    }

    private static void selectAddresses(Session session, long id) {

        Query query = session.createQuery("from User where id= :id");
        query.setParameter("id", id);
        User user = (User) query.getSingleResult();
        System.out.println(user);
    }

    private static void selectAddressByNativeSql(Session session) {
        Query query = session.createNativeQuery("select emp_id, city, zipcode from address")
                .addScalar("city", new StringType())
                .addScalar("zipcode", new StringType())
                .addScalar("emp_id", new LongType());
        List<Object[]> rows = query.getResultList();
        for (Object[] row : rows) {
            Address address = new Address();
            address.setCity(row[0].toString());
            address.setZipcode(row[1].toString());
            address.setId(Long.parseLong(row[2].toString()));
            System.out.println(address);
        }
    }

    private static void updateUserNameByNativeSql(Session session, String name, Long id) {
        String query = "UPDATE user SET name = '" + name + "' WHERE id = '" + id + "'";
        Query sqlQuery = session.createNativeQuery(query);
        sqlQuery.executeUpdate();
    }


    private static SessionFactory getSessionFactory() {

        Configuration configuration = new Configuration().configure();

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()

                .configure();

        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

        return sessionFactory;

    }


}
