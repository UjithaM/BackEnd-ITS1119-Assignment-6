package api;

import DTO.CustomerDTO;
import DTO.ItemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.DBProcess;
import entity.Customer;
import entity.Item;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "item",urlPatterns = "/item")
public class ItemApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        if(req.getContentType() == null ||
                !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            Jsonb jsonb = JsonbBuilder.create();
            var itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
            Item item = new Item();
            item.setDescription(itemDTO.getDescription());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setQty(itemDTO.getQty());
            var dbProcess = new DBProcess();
            writer.println(dbProcess.saveItemOne(item));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        String itemId = req.getParameter("itemId");
        var dbProcess = new DBProcess();
        if(itemId == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else if(itemId.equals("all")){
            String json = getString(dbProcess);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        }else {
            var Item = dbProcess.getItemData(itemId);
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemId(Item.getItemId());
            itemDTO.setDescription(Item.getDescription());
            itemDTO.setUnitPrice(Item.getUnitPrice());
            itemDTO.setQty(Item.getQty());
            
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(itemDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
        }
    }

    private static String getString(DBProcess data) throws JsonProcessingException {
        var getAllId = data.getAllItemData();
        for (Item item : getAllId){
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemId(item.getItemId());
            itemDTO.setDescription(item.getDescription());
            itemDTO.setUnitPrice(item.getUnitPrice());
            itemDTO.setQty(item.getQty());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getAllId);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        String id = req.getParameter("itemId");
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            writer.println(dbProcess.updateItemData(id, getItemData(req)));
        }
    }
    private Item getItemData(HttpServletRequest req) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        var customerDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
        Item item = new Item();
        item.setDescription(customerDTO.getDescription());
        item.setUnitPrice(customerDTO.getUnitPrice());
        item.setQty(customerDTO.getQty());
        return item;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("itemId");
        var writer = resp.getWriter();
        var dbProcess = new DBProcess();
        if(id == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else {
            writer.println(dbProcess.deleteItemData(id));
        }
    }
}
