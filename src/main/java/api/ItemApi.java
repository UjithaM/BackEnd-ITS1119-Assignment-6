package api;

import DTO.ItemDTO;
import db.DBProcess;
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
}
