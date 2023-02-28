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
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "downloadServlet", value = "/download")
public class DownloadServlet extends HttpServlet {
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
        boolean isExists = false;

        String queryString = req.getQueryString();
        Set<String> nameFileExists = getResourceFolderFiles("file");

        if (queryString == null)
            resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"File not found\"");
        else {
            String[] formQuery = queryString.split("=");
            if (formQuery.length == 2) {
                if (formQuery[0].equals("file") && formQuery[1].contains(".zip")) {
                    for (String nameFile : nameFileExists) {
                        if (nameFile.equals(formQuery[1])) {
                            isExists = true;
                            break;
                        }
                    }

                    if (isExists)
                        downloadFile(req, resp, formQuery[1]);
                    else
                        resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"File not exists in server\"");
                }else
                    resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"Invalid path\"");
            }else
                resp.sendRedirect("./jsp/logMessage.jsp?status=\"fileNotFound\"&message=\"Invalid path\"");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private Set<String> getResourceFolderFiles (String folder) {
//        ClassLoader loader = Thread.currentThread().getContextClassLoader();
//        URL url = loader.getResource(folder);

        URL url = getServletContext().getClassLoader().getResource(folder);
//        System.out.println("url: " + url);

        try {
            return Stream.of(new File(url.toURI()).listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .collect(Collectors.toSet());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadFile(HttpServletRequest req, HttpServletResponse resp, String nameFile) throws IOException {
        File file;
        try {
            file = new File(req.getServletContext().getResource("WEB-INF/classes/file/" + nameFile).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("application/zip");
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
}
