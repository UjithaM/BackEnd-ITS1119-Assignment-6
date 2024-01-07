package db;

import DTO.CustomerDTO;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DBProcess {
    private static final String GET_ALL_CUSTOMER = "FROM Customer";
    private SessionFactory sessionFactory;

    public DBProcess() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public String saveCustomerData(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            customer.setCustomerId(generateCustomerId(session));
            session.persist(customer);
            transaction.commit();
            return "Data saved";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String generateCustomerId(Session session) {
        List<Customer> customers = session.createQuery(GET_ALL_CUSTOMER, Customer.class).list();
        int count = customers.size() + 1;
        return "CUST" + count;
    }
}
