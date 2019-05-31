<%--
  Created by IntelliJ IDEA.
  User: fuzious
  Date: 5/26/2019
  Time: 1:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>
<%--    <style>--%>
<%--        --%>
<%--    </style>--%>
    <script language="javascript">
        function verify_geolocation_capability()
        {
            if (navigator.geolocation)
            {
                document.form1.capability.value = "Enabled";
            }
            else
            {
                document.form1.capability.value = "Not Enabled";
            }
        }

        function display_geolocation_properties(position)
        {
            document.form1.capability.value = "W o r k i n g . . .";
            document.form1.latitude.value = position.coords.latitude;
            document.form1.longitude.value = position.coords.longitude;
        }

        function handle_error(error)
        {
            document.form1.capability.value = "ERROR: " + error.code;
        }

        function retrieve_location()
        {
            if (navigator.geolocation)
            {
                document.form1.capability.value = "Starting...";
                navigator.geolocation.getCurrentPosition(display_geolocation_properties, handle_error);
                document.form1.capability.value = "Finished";
            }
            else
            {
                alert("This browser does not support geolocation services.");
            }
        }
    </script>
</head>
<body style="background: deeppink">
    <a href="index.jsp"><img src="radar.JPG" alt="homepage" style="display: block;margin-left: auto;margin-right: auto;width: 300px;height: 300px;"></a>
    <br><br>
    <form name="form1" id="form1" action="Locate" method="post">

        Geolocation Capability: <input type="text" name="capability" value="Unknown" required onkeydown="return false;"
                                       style="caret-color: transparent !important;">
        <p />
        Latitude:  <input type="text" name="latitude" required onkeydown="return false;"
                          style="caret-color: transparent !important;">
        <p />
        Longitude: <input type="text" name="longitude" required onkeydown="return false;"
                          style="caret-color: transparent !important;">
        <p />
        <input  type="button" name="submit" value="Click on me to fetch your location" onclick="retrieve_location()" >
        <input type="submit" value="After a successful fetch click on me for results"><br>
    </form>
</body>
</html>
