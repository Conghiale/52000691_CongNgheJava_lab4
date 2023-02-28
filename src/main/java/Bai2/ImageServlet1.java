package Bai2;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

@WebServlet(name = "imageServlet1", value = "/image1")
public class ImageServlet1 extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        super.doGet(req, resp);
        resp.setContentType("image/jpg");
        ServletOutputStream out = resp.getOutputStream();

//        Solution1. ---
//        BufferedImage theBufferedImage = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("image/image1.jpg")));
//        ImageIO.write(theBufferedImage, "jpg", out);

//        Solution2. ---
//        URL url = req.getServletContext().getResource("WEB-INF/classes/image/image1.jpg");
        URL url = Objects.requireNonNull(getServletContext().getClassLoader().getResource("image/image1.jpg"));
        File file;
        try {
            file = new File(url.toURI());
            System.out.println(file.getName());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);

        int ch=0;
        while ((ch=bufferedInputStream.read()) != -1) {
            bufferedOutputStream.write(ch);
        }
        bufferedInputStream.close();
        fileInputStream.close();
        bufferedOutputStream.close();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
