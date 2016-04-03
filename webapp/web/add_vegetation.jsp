<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f4bi" uri="/WEB-INF/f4bi.tld" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Add Vegetation</title>
    <script>
      function addPolygonPoint()
      {
          var table = document.getElementById("vegetation");
          var row = table.insertRow(table.rows.length - 1);
          var cell1 = row.insertCell(0);
          var cell2 = row.insertCell(1);
          cell1.innerHTML = 'Region point ' + (table.rows.length - 4) + ':';
          cell2.innerHTML = '<input type="text" name="x" size="5" maxlength="32" />, <input type="text" name="y" size="5" maxlength="32" />';
      }
    </script>
  </head>

  <body>
    <h1>Add Vegetation Debug Form</h1>

    <form action='add-vegetation' method='post' accept-charset='UTF-8'>
      <table id='vegetation'>
        <f4bi:emails />
        <tr>
          <td>User:</td>
          <td>
            <select name='user'>
              <c:forEach var="user" items="${users}">
                <option value='${user.id}'>${user.email}</option>
              </c:forEach>
            </select>
          </td>
        </tr>
        <f4bi:scientific-names />
        <tr>
          <td>Plant:</td>
          <td>
            <select name='plant'>
              <c:forEach var="plant" items="${plants}">
                <option value='${plant.id}'>${plant.scientificName}</option>
              </c:forEach>
            </select>
          </td>
        </tr>
        <tr><td>Amount of vegetation:</td><td><input type='text' name='amount' size='10' maxlength='32' /></td></tr>
        <tr><td>Region point 1:</td><td><input type='text' name='x' size='5' maxlength='32' />, <input type='text' name='y' size='5' maxlength='32' /></td></tr>
	<tr><td colspan='2'><button type="button" onclick="addPolygonPoint()">Add Point</button></td></tr>
      </table>
      
      <input type='submit' />
    </form>
    Back to the <a href="index.jsp">administrative console</a>
  </body>
</html>
