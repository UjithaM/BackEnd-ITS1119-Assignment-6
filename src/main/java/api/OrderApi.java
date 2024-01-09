package api;

import DTO.CustomerDTO;
import DTO.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import db.DBProcess;
import entity.Customer;
import entity.Orders;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "order",urlPatterns = "/order")
public class OrderApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        if(req.getContentType() == null ||
                !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            Jsonb jsonb = JsonbBuilder.create();
            var orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);
            Orders orders = new Orders();
            orders.setDate(orderDTO.getDate());
            orders.setNetTotal(orderDTO.getNetTotal());
            orders.setDiscount(orderDTO.getDiscount());
            orders.setCash(orderDTO.getCash());
            orders.setSubTotal(orderDTO.getSubTotal());
            Customer customer  = new Customer();
            customer.setCustomerId(orderDTO.getCustomerId());
            orders.setCustomer(customer);
            var dbProcess = new DBProcess();
            writer.println(dbProcess.saveOrder(orders, orderDTO.getOrderItems()));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("orderId");
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else if(id.equals("all")){
            var getAllId = dbProcess.getAllOrderData();
            List<OrderDTO> orderDTOList = new ArrayList<>();
            for (Orders orders : getAllId) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderId(orders.getOrderId());
                orderDTO.setDate(orders.getDate());
                orderDTO.setNetTotal(orders.getNetTotal());
                orderDTO.setDiscount(orders.getDiscount());
                orderDTO.setCash(orders.getCash());
                orderDTO.setSubTotal(orders.getSubTotal());
                orderDTO.setCustomerId(orders.getCustomer().getCustomerId());
                orderDTO.setOrderItems(orders.getOrderItems());
                orderDTOList.add(orderDTO);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Hibernate6Module());
            String json = objectMapper.writeValueAsString(orderDTOList);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        } else {
            var getData = dbProcess.getOrderData(id);
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(getData.getOrderId());
            orderDTO.setDate(getData.getDate());
            orderDTO.setNetTotal(getData.getNetTotal());
            orderDTO.setDiscount(getData.getDiscount());
            orderDTO.setCash(getData.getCash());
            orderDTO.setSubTotal(getData.getSubTotal());
            orderDTO.setCustomerId(getData.getCustomer().getCustomerId());
            orderDTO.setOrderItems(getData.getOrderItems());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Hibernate6Module());
            String json = objectMapper.writeValueAsString(orderDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        String id = req.getParameter("orderId");
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            writer.println(dbProcess.updateOrderData(id, getOrderData(req)));
        }
    }

    private Orders getOrderData(HttpServletRequest req) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        var orderDTO = jsonb.fromJson(req.getReader(), OrderDTO.class);
        Orders orders = new Orders();
        orders.setDate(orderDTO.getDate());
        orders.setNetTotal(orderDTO.getNetTotal());
        orders.setDiscount(orderDTO.getDiscount());
        orders.setCash(orderDTO.getCash());
        orders.setSubTotal(orderDTO.getSubTotal());
        Customer customer  = new Customer();
        customer.setCustomerId(orderDTO.getCustomerId());
        orders.setCustomer(customer);
        orders.setOrderItems(orderDTO.getOrderItems());
        return orders;
    }
}
