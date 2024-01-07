package api;

import DTO.CustomerDTO;
import db.DBProcess;
import entity.Customer;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "customer",urlPatterns = "/customer")
public class CustomerApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        if(req.getContentType() == null ||
                !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            Jsonb jsonb = JsonbBuilder.create();
            var customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            Customer customer = new Customer();
            customer.setFullName(customerDTO.getFullName());
            customer.setAddress(customerDTO.getAddress());
            var dbProcess = new DBProcess();
            writer.println(dbProcess.saveCustomerData(customer));
        }
    }
}
