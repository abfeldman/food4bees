<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Food4Bees Index Dashboard</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>

  <body>
    <h1>Food4Bees Index Dashboard</h1>

    <c:if test="${empty sessionScope.uid}">
      <ul>
        <li><a href='login.jsp?ref=${pageContext.request.requestURI}'>Log in</a></li>
      </ul>
    </c:if>

    <c:if test="${not empty sessionScope.uid}">
      <ul>
        <li><a href='user.jsp'>Add a new user</a></li>
        <li><a href='manage_users.jsp'>Manage existing users</a></li>
        <li><a href='plant.jsp'>Add a new plant</a></li>
        <li><a href='manage_plants.jsp'>Manage existing plants</a></li>
        <li><a href='add_vegetation.jsp'>Add vegetation</a></li>
        <li><a href='view_vegetation.jsp'>View vegetation</a></li>
      </ul>
      <ul>
        <li><a href='export-db'>Export database</a></li>
      </ul>
      <ul>
        <li><a href='logout?ref=${pageContext.request.requestURI}'>Log out</a></li>
      </ul>

      <f4bi:statistics />

      <c:if test="${not empty statistics}">
         Database statistics:
         <table>
           <tr><td>Users:</td><td><c:out value="${statistics.users}" /></td></tr>
           <tr><td>Plants:</td><td><c:out value="${statistics.plants}" /></td></tr>
           <tr><td>Plant images:</td><td><c:out value="${statistics.plantImages}" /></td></tr>
           <tr><td>Vegetation records:</td><td><c:out value="${statistics.vegetationRecords}" /></td></tr>
         </table>
      </c:if>

    </c:if>

    <p>Download the <a href='../beedroid/beedroid-0.1.apk'>Beedroid v. 0.1</a> client application:<br />
    <a href='../beedroid/beedroid-0.1.apk'><img alt="beedroid-0.1" src="images/beedroid-0.1.png" /></a></p>
  </body>
</html>
