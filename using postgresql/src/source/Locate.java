package source;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@WebServlet("/Locate")
public class Locate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        Locate ob=new Locate();
        try {
            Connection con=getConnection();
            Statement stm=con.createStatement();
            HttpSession HS=request.getSession();
            ob.database((String)HS.getAttribute("username"),(String)HS.getAttribute("password"),stm,request,response,HS);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void database(String uname, String password, Statement stm, HttpServletRequest request, HttpServletResponse response, HttpSession HS) throws Exception{
        String latitude=request.getParameter("latitude");
        String longitude=request.getParameter("longitude");
        String query1="update logindbs set latitude='"+latitude+"',longitude='"+longitude+"' where username='"+uname+"'";
        int a=stm.executeUpdate(query1);
        String query2="select * from logindbs";
        ResultSet RS=stm.executeQuery(query2);

        SortedMap<Double,String> SM=new TreeMap<>();
        while(RS.next()){
            String itrUser=RS.getString("username");
            String details=RS.getString("details");
            if(itrUser.equals(uname))
                continue;
            double lat=Double.parseDouble(RS.getString("latitude"));
            double lon=Double.parseDouble(RS.getString("longitude"));
            double la2=Double.parseDouble(latitude);
            double lo2=Double.parseDouble(longitude);
            double dis=distance(lat,lon,la2,lo2,"K");
            dis=roundAvoid(dis,3);
            SM.put(dis,details);
        }

        PrintWriter out=response.getWriter();

        out.println("<html>\n" +
                "<head>\n" +
                "<script>\n" +
                "function goBack() {\n" +
                "  window.history.back()\n" +
                "}\n" +
                "</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<button onclick=\"goBack()\">Go Back to Dashboard</button><br>\n");
        out.println("you are<br>");
        for (Map.Entry<Double,String> entry : SM.entrySet()) {
            out.println("<pre>"+entry.getKey() +"km far from " + entry.getValue()+"<br>");
        }
        out.println("</body></html>");
        out.close();
        stm.close();
    }
    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return (Math.round(value * scale) / scale);
    }
    static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

}
