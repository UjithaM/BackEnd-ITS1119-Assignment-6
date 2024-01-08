package api;

import DTO.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "customer",urlPatterns = "/customer")
public class CustomerApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        var dbProcess = new DBProcess();
        if(req.getContentType() == null ||
                !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            writer.println(dbProcess.saveCustomerData(getCustomerData(req)));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("customerId");
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else if(id.equals("all")){
            var getAllId = dbProcess.getAllCustomerData();
            List<CustomerDTO> customerDTOList = new ArrayList<>();
            for (Customer customer : getAllId) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setCustomerId(customer.getCustomerId());
                customerDTO.setFullName(customer.getFullName());
                customerDTO.setAddress(customer.getAddress());
                customerDTOList.add(customerDTO);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customerDTOList);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        } else {
            var getData = dbProcess.getCustomerData(id);
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(getData.getCustomerId());
            customerDTO.setFullName(getData.getFullName());
            customerDTO.setAddress(getData.getAddress());
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customerDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        String id = req.getParameter("customerId");
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            writer.println(dbProcess.updateCustomerData(id, getCustomerData(req)));
        }
    }

    private Customer getCustomerData(HttpServletRequest req) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        var customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
        Customer customer = new Customer();
        customer.setFullName(customerDTO.getFullName());
        customer.setAddress(customerDTO.getAddress());;
        return customer;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("customerId");
        var writer = resp.getWriter();
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            writer.println(dbProcess.deleteCustomerData(id));
        }
    }
}
