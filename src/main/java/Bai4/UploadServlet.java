package Bai4;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "uploadServlet", value = "/uploadServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 5)// 50MB)
public class UploadServlet extends HttpServlet {
    private final int ARBITARY_SIZE = 1048;
    String filename, extname;
    Part part;
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("line 36: ");
        if (req.getParameter("filename") == null){
            System.out.println("line 38: ");
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            req.getRequestDispatcher("/html/upload.html").forward(req, resp); // render file html
//        resp.sendRedirect("./html/upload.html");
        }else {
            System.out.println("line 75: " + req.getParameter("filename"));
            downloadFile(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()); // duong dan folder resources/root

        filename = req.getParameter("filename");
        String checkbox = req.getParameter("checkbox");
        Set<String> nameFileExists = getResourceFolderFiles(getFolderUpload("/uploads"));
        part = req.getPart("fileUpLoad");

        extname = extensionFile(part);

        boolean isExists = false;
        for (String nameFile : nameFileExists) {
            if (nameFile.equals("" + filename + extname)) {
                isExists = true;
                break;
            }
        }

        if (isExists){
            if (checkbox != null){
                uploadFile(req, resp, "File has been overridden");
            }else {
                req.setAttribute("status", "warning");
                req.setAttribute("message", "File already exists");
                req.getRequestDispatcher("/jsp/logMessage_Forward.jsp").forward(req, resp);
            }
        }else {
            uploadFile(req, resp, "File has been uploaded");
        }

    }
    private void uploadFile(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException, ServletException {
        if (extname.equals(".txt") || extname.equals(".doc") || extname.equals(".docx") || extname.equals(".png")
                || extname.equals(".jpg") || extname.equals(".pdf") || extname.equals(".rar") || extname.equals(".zip")) {
            part.write(this.getFolderUpload("/uploads").getAbsolutePath() + File.separator + filename + extname);

            req.setAttribute("status", "success");
            req.setAttribute("message", message);
        }else {
            req.setAttribute("status", "warning");
            req.setAttribute("message", "Unsupported file extension");
        }
        req.getRequestDispatcher("/jsp/logMessage_Forward.jsp").forward(req, resp);
    }
    private void downloadFile(HttpServletRequest req, HttpServletResponse resp) {
        File file;
        try {
            file = new File(req.getServletContext().getResource("WEB-INF/classes/uploads/" + filename + extname).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("image/jpg; application/zip");
        resp.setHeader("Content-disposition", "attachment; filename="+file.getName());

        try(InputStream in = new FileInputStream(file);
            ServletOutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[ARBITARY_SIZE];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Set<String> getResourceFolderFiles (File folderUpload) {
        return Stream.of(Objects.requireNonNull(folderUpload.listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

//    get extension file upload (png, jpg,...)
    private String extensionFile(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("."), s.length() - 1);
            }
        }
        return "";
    }
    public File getFolderUpload(String pathFolder) {
        URL url = getServletContext().getClassLoader().getResource(pathFolder);
        File folderUpload;
        if (url == null) {
            String u = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + pathFolder;
            folderUpload = new File(u);
        }else {
            try {
                folderUpload = new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }
        return folderUpload;
    }


//    Extracts file name from HTTP header content-disposition
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";

//        Solution2----
//        String filename = Path.of(part.getSubmittedFileName()).getFileName().toString(); // lay ten cua fileUpload
    }
//    https://www.tabnine.com/code/java/methods/javax.servlet.http.Part/getContentType
//    https://www.youtube.com/watch?v=BXJLoDILt50
}
