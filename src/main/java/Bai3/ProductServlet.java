package Bai3;

import com.google.gson.Gson;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ProductServlet", value = "/ProductServlet")
public class ProductServlet extends HttpServlet {
    private Gson gson = new Gson();
    private boolean isExists = false;
    private boolean isFound = false;
    private Product p = new Product();
    List<Product> productList = new ArrayList<>();
    Map<String, Object> myMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        productList.add(new Product(1, "product1", 100));
        productList.add(new Product(2, "product2", 200));
        productList.add(new Product(3, "product3", 300));
        productList.add(new Product(4, "product4", 400));
        productList.add(new Product(5, "product5", 500));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String qs = req.getQueryString();
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String s_id = req.getParameter("id");

        if (qs == null) {
            myMap.clear();
            myMap.put("code", 0);
            myMap.put("Message", "Get all products successfully");
            myMap.put("data", productList);
//            out.print(JsonParser.parseString(gson.toJson(myMap)));
//            json -> Map ---
//            Type typeOfHashMap = new TypeToken<Map<String, Object>>() { }.getType();
//            Map<String, Object> newMap = gson.fromJson(gson.toJson(myMap), typeOfHashMap); // This type must match TypeToken



//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("code", 0);
//            jsonObject.addProperty("Message", "Get all products successfully");
//            jsonObject.addProperty("data", data.toString());
//            PrintWriter out = resp.getWriter();
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            out.print(jsonObject);
//            out.flush();
        }else {
            String[] formGET = qs.split("&");
            if (formGET.length == 1 && s_id != null){
                if (!Objects.requireNonNull(s_id).equals("")) {
                    if (isNumeric(s_id)) {
                        isFound = false;
                        int id = Integer.parseInt(s_id);
                        productList.forEach(product -> {
                            if (product.getId() == id) {
                                isFound = true;
                                p = product;
                            }
                        });
                        if (isFound){
                            myMap.clear();
                            myMap.put("code", 1);
                            myMap.put("Message", "Get product id:" + id + " successfully.");
                            myMap.put("data", p);
                        }else {
                            myMap.clear();
                            myMap.put("code", 2);
                            myMap.put("Message", "Not found product with id " + id);
                        }
                    }else{
                        myMap.clear();
                        myMap.put("code", 3);
                        myMap.put("Message", "id must be a number.");
                    }
//                    resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"Invalid path or ID must be a natural number.\"");
                }else {
                    myMap.clear();
                    myMap.put("code", 4);
                    myMap.put("Message", "Value of id, name and price not empty.");
                }
            } else{
                myMap.clear();
                myMap.put("code", 5);
                myMap.put("Message", "Invalid path");
//                resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"Invalid path\"");
            }
        }
        out.print(gson.toJson(myMap));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String s_id = req.getParameter("id");
        String name = req.getParameter("name");
        String s_price = req.getParameter("price");

        if (s_id != null && name != null && s_price != null) {
            if (!Objects.requireNonNull(s_id).equals("") && !Objects.equals(s_price, "") && !Objects.equals(name, "")) {
                if (isNumeric(s_id) && isNumeric(s_price)) {
                    int id = Integer.parseInt(s_id);
                    isExists = false;
                    productList.forEach(product -> {
                        if (id == product.getId()){
                            p = product;
                            isExists = true;
                        }
                    });

                    if (isExists){
                        myMap.clear();
                        myMap.put("code", 0);
                        myMap.put("Message", "id " + id + " already exists.");
                        myMap.put("data", p);
                    } else{
                        double price = Double.parseDouble(s_price);
                        productList.add(new Product(id, name, price));

                        myMap.clear();
                        myMap.put("code", 1);
                        myMap.put("Message", "Add product with id " + id + " successfully");
                        myMap.put("data", productList);
                    }

                }else{
                    myMap.clear();
                    myMap.put("code", 2);
                    myMap.put("Message", "ID and price must be a number.");
                    //                        resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"ID and price must be a number.\"");
                }
            }else{
                myMap.clear();
                myMap.put("code", 3);
                myMap.put("Message", "Value of id, name and price not empty.");
                //                    resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"Value of id, name and price not empty.\"");
            }
        }else{
            myMap.clear();
            myMap.put("code", 4);
            myMap.put("Message", "Invalid path");
            //                resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"Invalid path\"");
        }
        out.print(gson.toJson(myMap));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String s_id = req.getParameter("id");

        if (req.getQueryString().split("&").length == 1 && s_id != null) {
            if (!Objects.requireNonNull(s_id).equals("")) {
                if (isNumeric(s_id)) {
                    int id = Integer.parseInt(s_id);
                    isExists = false;
                    productList.forEach(product -> {
                        if (id == product.getId()){
                            p = product;
                            isExists = true;
                        }
                    });

                    if (isExists){
                        productList.remove(p);
                        myMap.clear();
                        myMap.put("code", 0);
                        myMap.put("Message", "Delete product with id: " + id + " successfully");
                        myMap.put("data", productList);
                    } else{
                        myMap.clear();
                        myMap.put("code", 1);
                        myMap.put("Message", "Delete fail. Not found product with id:" + id);
                    }
                } else {
                    myMap.clear();
                    myMap.put("code", 2);
                    myMap.put("Message", "id and price must be a number.");
                }
            }else {
                myMap.clear();
                myMap.put("code", 3);
                myMap.put("Message", "Value of id, name and price not empty.");
            }
        }else {
            myMap.clear();
            myMap.put("code", 4);
            myMap.put("Message", "Invalid path");
        }
        out.print(this.gson.toJson(myMap));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String s_id = req.getParameter("id");
        String name = req.getParameter("name");
        String s_price = req.getParameter("price");

        if (s_id != null && name != null && s_price != null ) {
            if (!Objects.requireNonNull(s_id).equals("") && !Objects.equals(s_price, "") && !Objects.equals(name, "")) {
                if (isNumeric(s_id) && isNumeric(s_price)) {
                    int id = Integer.parseInt(s_id);
                    isExists = false;
                    productList.forEach(product -> {
                        if (id == product.getId()){
                            p = product;
                            isExists = true;
                        }
                    });

                    if (isExists){
                        double price = Double.parseDouble(s_price);
                        p.setName(name);
                        p.setPrice(price);

                        myMap.clear();
                        myMap.put("code", 0);
                        myMap.put("Message", "Update product with id: " + id + " successfully");
                        myMap.put("data", productList);
                    } else{
                        myMap.clear();
                        myMap.put("code", 1);
                        myMap.put("Message", "Update fail. Not found product with id:" + id);
                    }
                } else {
                    myMap.clear();
                    myMap.put("code", 2);
                    myMap.put("Message", "id and price must be a number.");
                }
            }else {
                myMap.clear();
                myMap.put("code", 3);
                myMap.put("Message", "Value of id, name and price not empty.");
            }
        }else {
            myMap.clear();
            myMap.put("code", 4);
            myMap.put("Message", "Invalid path");
        }
        out.print(this.gson.toJson(myMap));
        out.flush();
    }

//    Kiem tra chuoi co phai la so hay khong ---
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
