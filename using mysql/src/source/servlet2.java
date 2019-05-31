package source;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/servlet2")
public class servlet2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        servlet2 ob=new servlet2();
        try {
            String url="jdbc:mysql://localhost:3306/abc";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,"root","arpit");
            Statement stm=con.createStatement();
            HttpSession HS=request.getSession();
            String details=request.getParameter("name")+" , "+request.getParameter("batch");
            ob.database(request.getParameter("setUname"),request.getParameter("setPassword"),details,stm,request,response,HS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void database(String uname, String password,String details, Statement stm, HttpServletRequest request, HttpServletResponse response, HttpSession HS) throws Exception{
        String query="insert into logindbs values ('"+uname+"','"+password+"','0','0','"+details+"')";
        ResultSet RS=stm.executeQuery("SELECT password FROM logindbs WHERE username ='"+uname+"'");
        System.out.println(query);
        if(RS.isBeforeFirst()) {
            HS.setAttribute("correct", "usernameExists");
            response.sendRedirect(request.getContextPath());
        }
        else {
            stm.executeUpdate(query);
            HS.setAttribute("correct","registered");
            response.sendRedirect(request.getContextPath());
        }
        stm.close();
    }
}
