package db;

import entity.Customer;
import entity.Item;
import entity.OrderItem;
import entity.Orders;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Order;

import java.util.List;

public class DBProcess {
    private static final String GET_ALL_CUSTOMER = "FROM Customer";

    private static final String GET_ALL_ITEM_DATA = "FROM Item";
    
    private static final String GET_ALL_ORDER_DATA = "FROM Orders";
    private final SessionFactory sessionFactory;

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

    public List<Customer> getAllCustomerData() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(GET_ALL_CUSTOMER, Customer.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String updateCustomerData(String customerId, Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            customer.setCustomerId(customerId);
            session.merge(customer);
            transaction.commit();
            return "Data Updated";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getCustomerData(String customerId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Customer.class, customerId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateCustomerId(Session session) {
        List<Customer> customers = session.createQuery(GET_ALL_CUSTOMER, Customer.class).list();
        int count = customers.size() + 1;
        return "CUST" + count;
    }

    public String deleteCustomerData(String customerId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class, customerId);
            if (customer != null) {
                session.remove(customer);
                transaction.commit();
                return "Data Deleted!";
            } else {
                return "Customer not found!";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //item process

    public String saveItemOne(Item item) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            item.setItemId(generateItemId(session));
            session.persist(item);
            transaction.commit();
            return "Data Saved!";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getAllItemData() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(GET_ALL_ITEM_DATA, Item.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateItemId(Session session) {
        List<Item> items = session.createQuery(GET_ALL_ITEM_DATA, Item.class).list();
        int count = items.size() + 1;
        return "ITEM" + count;
    }

    public Item getItemData(String itemCode) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Item.class, itemCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String updateItemData(String itemId, Item item) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            item.setItemId(itemId);
            session.merge(item);
            transaction.commit();
            return "Data Updated";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteItemData(String code) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Item item = session.get(Item.class, code);
            if (item != null) {
                session.remove(item);
                transaction.commit();
                return "Data Deleted!";
            } else {
                return "Item not found!";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Order process

    public String saveOrder(Orders orders, List<OrderItem> orderItems) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                orders.setOrderId(generateOrderId(session));

                session.persist(orders);

                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrders(orders);
                    session.persist(orderItem);
                }

                transaction.commit();

                return "Data Saved!";
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateOrderId(Session session) {
        List<Orders> items = session.createQuery(GET_ALL_ORDER_DATA, Orders.class).list();
        int count = items.size() + 1;
        return "ORDER" + count;
    }

    public List<Orders> getAllOrderData() {
        try (Session session = sessionFactory.openSession()) {
            List<Orders> orders = session.createQuery(GET_ALL_ORDER_DATA, Orders.class).list();
            for (Orders order : orders) {
                Hibernate.initialize(order.getOrderItems());
            }
            return  orders;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Orders getOrderData(String orderId) {
        try (Session session = sessionFactory.openSession()) {
            Orders orders = session.get(Orders.class, orderId);
            Hibernate.initialize(orders.getOrderItems());
            return orders;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
