<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>View Vegetation</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <div class="edit_delete">
      <h1>View Vegetation</h1>
      <table>
        <f4bi:vegetation />
        <c:if test="${vegetation == null}">
          <tr><td colspan="4">Internal error</td></tr>
        </c:if>
        <c:if test="${vegetation != null && empty vegetation}">
          <tr><td colspan="4">No vegetation</td></tr>
        </c:if>
        <c:if test="${not empty vegetation}">
          <tr><th>Time</th><th>User</th><th>Plant</th><th>Area</th></tr>
          <c:forEach var="entry" items="${vegetation}">
            <tr>
              <td>${entry.time}</td>
              <td>${entry.user}</td>
              <td>${entry.plant}</td>
              <td>${entry.area}</td>
            </tr>
          </c:forEach>
        </c:if>
      </table>
      <p>Back to the <a href="index.jsp">main page</a></p>
    </div>
  </body>
</html>
