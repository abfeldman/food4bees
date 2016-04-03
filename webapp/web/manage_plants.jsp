<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Manage Plants</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
<body>
  <div class="edit_delete">
    <h1>Manage Plants</h1>
    <table>
      <f4bi:plants />
      <c:if test="${plants == null}">
        <tr><td colspan="4">Internal error</td></tr>
      </c:if>
      <c:if test="${plants != null && empty plants}">
        <tr><td colspan="4">No plants</td></tr>
      </c:if>
      <c:if test="${not empty plants}">
        <tr><th>Common name</th><th>Latin name</th><th></th><th></th><th></th><th></th></tr>
        <c:forEach var="plant" items="${plants}">
          <tr>
            <td>${plant.commonName != null ? plant.commonName : 'N/A'}</td>
            <td>${plant.scientificName}</td>
            <td><a href="edit-plant?id=${plant.id}">Edit</a></td>
            <td><a href="delete-plant?id=${plant.id}">Delete</a></td>
            <td><a href="plant_image.jsp?plantid=${plant.id}">Add Image</a></td>
            <td><a href="manage_plant_images.jsp?plantid=${plant.id}">Manage Images</a></td>
          </tr>
        </c:forEach>
      </c:if>
    </table>
    <p>Back to the <a href="index.jsp">main page</a></p>
  </div>
</body>
</html>
