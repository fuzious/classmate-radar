<%--
  Created by IntelliJ IDEA.
  User: fuzious
  Date: 5/20/2019
  Time: 1:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Classmate Radar</title>
  </head>

  <body onload="verify_geolocation_capability()" style="background: aqua">
  <a href="index.jsp"><img src="radar.JPG" alt="homepage" style="display: block;margin-left: auto;margin-right: auto;width: 300px;height: 300px"></a>
  <h1 style="color: darkorange;text-align: center">Classmate Radar</h1>
      <form action="servlet1" method="post" style="float: left;width: 50%;">
          <p style="font-size: 30px;">Existing user? Sign in here</p>
          <span style="font-family: 'Comic Sans MS',sans-serif;color: crimson" >Username </span><input type="text" name="uname" value="username" required><br>
          <span style="font-family: 'Comic Sans MS',sans-serif;color: crimson" >Password </span><input type="password" name="password" value="password" required><br>
        <input type="submit" value="Login"><br>
        <%
            if(session.getAttribute("correct")!=null) {
                if (session.getAttribute("correct").equals("notExists")) {
                    out.println("Please sign up first,username does not exist");
                    session.setAttribute("correct",null);
                }
                else if (session.getAttribute("correct").equals("wrong")) {
                    out.println("Wrong username and password combination");
                    session.setAttribute("correct", null);
                }
            }
        %>
      </form>

      <form action="servlet2" method="post" style="float: right;width: 50%;">
          <p style="font-size: 30px;">New user? Sign up here</p>
          <p style="font-size: 20px;">Choose username and password here</p>
         <span style="font-family: 'Comic Sans MS',sans-serif;color: deeppink ">Choose username </span>  <input type="text" name="setUname" value="username" required><br>
          <span style="font-family: 'Comic Sans MS',sans-serif;color: deeppink ">Choose password </span> <input type="password" name="setPassword" value="password" required><br>
          <span style="font-family: 'Comic Sans MS',sans-serif;color: deeppink ">Your name </span><input type="text" name="name" value="your name" required><br>
          <span style="font-family: 'Comic Sans MS',sans-serif;color: deeppink ">Your batch</span>
        <select name="batch" required>
            <option value="2017">2017</option>
            <option value="2018">2018</option>
            <option value="2019">2019</option>
            <option value="2020">2020</option>
            <option value="2021">2021</option>
            <option value="2022">2022</option>
        </select>
        <input type="submit" value="Sign up"><br>
          <%
              if(session.getAttribute("correct")!=null&&session.getAttribute("correct").equals("usernameExists")) {
                  out.println("choose another username ,this username already exists");
                  session.setAttribute("correct", null);
              }
              if(session.getAttribute("correct")!=null&&session.getAttribute("correct").equals("registered")) {
                  out.println("you have been registered successfully");
                  session.setAttribute("correct", null);
              }
          %>
      </form>

  </body>
</html>
