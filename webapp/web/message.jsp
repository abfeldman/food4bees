<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>${caption}</title>
    <link rel="stylesheet" href="css/f4bi.css">
  </head>
  <body>
    <div class="message">
      <h1>${caption}</h1>
      <form action="${destination}" method="post">
        <table>
          <tr>
            <td>${message}</td>
          </tr>
          <tr>
            <td class="buttons"><input type="submit" name="submit" value="Ok" /></td>
          </tr>
        </table>
      </form>
    </div>
  </body>
</html>
