<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Manage Users</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <div class="edit_delete">
      <h1>Manage Users</h1>
      <table>
        <f4bi:users />
        <c:choose>
          <c:when test="${error != null}">
            <tr><td colspan="5">${error}</td></tr>
          </c:when>
          <c:otherwise>
            <c:if test="${users == null}">
              <tr><td colspan="5">Internal error</td></tr>
            </c:if>
          </c:otherwise>
        </c:choose>
        <c:if test="${users != null && empty users}">
          <tr><td colspan="5">No users</td></tr>
        </c:if>
        <c:if test="${not empty users}">
          <tr><th>Name</th><th>Email</th><th>Group</th><th></th><th></th></tr>
          <c:forEach var="user" items="${users}">
            <tr>
              <td>${user.name}</td>
              <td>${user.email}</td>
              <td>${user.groupName}</td>
              <td><a href="edit-user?id=${user.id}">Edit</a></td>
              <td><a href="delete-user?id=${user.id}">Delete</a></td>
            </tr>
          </c:forEach>
        </c:if>
      </table>
      <p>Back to the <a href="index.jsp">main page</a></p>
    </div>
  </body>
</html>
