<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Log in to Food4Bees</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <div class="login">
      <h1>Log in to Food4Bees</h1>
      <form action="authenticate-user" method="post">
        <c:if test="${not empty param.ref}"><input type="hidden" value="${param.ref}" name="ref"></c:if>
        <table>
          <c:if test="${not empty error}"><tr><td class="error">${error}</td></tr></c:if>
          <tr>
            <td><input id="email" type="text" name="email" value="${email}" placeholder="Email"></td>
          </tr>
          <tr>
            <td><input id="password" type="password" name="password" value="${password}" placeholder="Password"></td>
          </tr>
          <tr>
            <td class="buttons"><input type="submit" name="submit" value="Log in"></td>
          </tr>
        </table>
      </form>
      <p>Click <a href="register.jsp">here</a> to create a new account and <a href="reset_password.jsp">here</a> to reset your password.</p>
    </div>
  </body>
</html>
