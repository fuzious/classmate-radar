package source;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

@WebServlet("/servlet2")
public class servlet2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        servlet2 ob=new servlet2();
        try {
            Connection con=getConnection();
            Statement stm=con.createStatement();
            HttpSession HS=request.getSession();
            String details=request.getParameter("name")+" , "+request.getParameter("batch");
            ob.database(request.getParameter("setUname"),request.getParameter("setPassword"),details,stm,request,response,HS);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void database(String uname, String password,String details, Statement stm, HttpServletRequest request, HttpServletResponse response, HttpSession HS) throws Exception{
        String query="insert into logindbs values ('"+uname+"','"+password+"','0','0','"+details+"')";
        ResultSet RS=stm.executeQuery("SELECT password FROM logindbs WHERE username ='"+uname+"'");
        if(RS.isBeforeFirst()) {
            HS.setAttribute("correct", "usernameExists");
            RequestDispatcher RD= request.getRequestDispatcher(request.getContextPath());
            RD.forward(request,response);
        }
        else {
            stm.executeUpdate(query);
            HS.setAttribute("correct","registered");
            RequestDispatcher RD= request.getRequestDispatcher(request.getContextPath());
            RD.forward(request,response);
        }
        stm.close();

    }
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
