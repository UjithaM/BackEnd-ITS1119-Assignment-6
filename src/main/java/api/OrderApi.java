package api;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "order",urlPatterns = "/order")
public class OrderApi extends HttpServlet {

}
