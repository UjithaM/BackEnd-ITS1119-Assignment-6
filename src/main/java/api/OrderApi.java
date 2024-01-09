package api;

import DTO.OrderDTO;
import DTO.OrderDTO;
import db.DBProcess;
import entity.Customer;
import entity.Item;
import entity.Orders;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
}
