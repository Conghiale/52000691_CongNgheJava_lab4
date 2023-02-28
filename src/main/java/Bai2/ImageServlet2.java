package Bai2;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@WebServlet(name = "imageServlet2", value = "/image2")
public class ImageServlet2 extends HttpServlet {
    private final int ARBITARY_SIZE = 1048;
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        File file;
        try {
            file = new File(req.getServletContext().getResource("WEB-INF/classes/image/image1.jpg").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("image/jpg");
        resp.setHeader("Content-disposition", "attachment; filename="+file.getName());

        try(InputStream in = new FileInputStream(file);
            ServletOutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[ARBITARY_SIZE];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
