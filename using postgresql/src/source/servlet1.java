package source;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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

@WebServlet("/servlet1")
public class servlet1 extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        PrintWriter out=response.getWriter();
        HttpSession sess=request.getSession();
        sess.setAttribute("username",request.getParameter("uname"));
        sess.setAttribute("password",request.getParameter("password"));
        String uname=(String)sess.getAttribute("username");
        String password=(String)sess.getAttribute("password");

        servlet1 ob=new servlet1();
        try {
            Connection con=getConnection();
            Statement stm=con.createStatement();
            ob.database(uname,password,sess,out,stm,request,response);
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
    void database(String uname,String password,HttpSession HS,PrintWriter out,Statement stm,HttpServletRequest request,HttpServletResponse response) {
        try {
            String query = "SELECT password FROM logindbs WHERE username ='" + uname + "'";
            ResultSet RS = stm.executeQuery(query);
            int xk = 0;
            if (!RS.isBeforeFirst()) {
                HS.setAttribute("correct", "notExists");
            } else if (RS.next() && !RS.getString("password").equals(password)) {
                HS.setAttribute("correct", "wrong");
            }else{
                xk=1;
                RequestDispatcher RD= request.getRequestDispatcher("location.jsp");
                RD.forward(request,response);
            }
            if (xk == 0){
                RequestDispatcher RD= request.getRequestDispatcher(request.getContextPath());
                RD.forward(request,response);
            }
            stm.close();

        } catch (Exception e){
            e.printStackTrace(out);

        }
        out.close();
    }
}
