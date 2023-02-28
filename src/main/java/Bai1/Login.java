package Bai1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "login", value = "/login")
public class Login extends HttpServlet {

    HashMap<String, String> account = new HashMap<String, String>();

    @Override
    public void init() throws ServletException {
        super.init();

        account.put("admin", "123456");
        account.put("lab4", "123456");
        account.put("tdtu", "123456");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // TODO Auto-generated method stub
        super.service(req, resp);
        System.out.println("Phuong thuc cua request " + req.getMethod());// tar về phuong thức tương ứng
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (account.get(username) == null) {
            resp.sendRedirect("./jsp/logMessage.jsp?status=\"notFound\"&message=\"Account does not exist.\"");
//            req.getRequestDispatcher("/jsp/logMessage.jsp?status=notFound").forward(req, resp);
        }else if (password.equals(account.get(username))) {
            resp.sendRedirect("./jsp/logMessage.jsp?status=\"success\"&message=\"Logged in successfully!\"");
//            req.getRequestDispatcher("/jsp/logMessage.jsp?status=success").forward(req, resp);
        }else {
            resp.sendRedirect("./jsp/logMessage.jsp?status=\"failed\"&message=\"Login failed!\"");
//            req.getRequestDispatcher("/jsp/logMessage.jsp?status=failed").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
