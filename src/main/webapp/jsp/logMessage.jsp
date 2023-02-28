<%--
  Created by IntelliJ IDEA.
  User: conghiale
  Date: 2/22/2023
  Time: 8:32 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
    <title>Document</title>
</head>
<body>
<div class="col-md-4" style="margin: auto; padding-top: 10px">
        <div class="alert alert-info" style="display: none">
            <strong>Logged in successfully!</strong>
        </div>
        <div class="alert alert-danger"style="display: none">
            <strong>Login failed!</strong>
        </div>
        <div class="alert alert-warning"style="display: none">
            <strong>Account does not exist.</strong>
        </div>
    </div>

    <script>
        let status, message
        $(document).ready(function () {
            console.log("logMessage run")
            status = <%= request.getParameter("status")%>;
            message = <%= request.getParameter("message")%>;

            if (status === "failed"){
                $('.alert-danger').show()
            } else if (status === "notFound") {
                $('.alert-warning').show()
            } else if (status === "fileNotFound"){
                $('.alert-warning').text(message)
                $('.alert-warning').show()
            }
        });
    </script>
</body>
</html>
