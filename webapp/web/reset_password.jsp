<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Reset Password</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <div class="login">
      <h1>Reset Password</h1>
      <form action="reset-password" method="post">
        <table>
          <c:if test="${not empty error}"><tr><td class="error">${error}</td></tr></c:if>
          <tr>
            <td><input id="email" type="text" name="email" value="${email}" placeholder="Email" /></td>
          </tr>
          <tr>
            <td class="buttons"><input type="submit" name="submit" value="Submit" /> <input type="reset" name="reset" value="Reset" /></td>
          </tr>
        </table>
      </form>
      <p>Back to the <a href="login.jsp">login page</a></p>
    </div>
  </body>
</html>
