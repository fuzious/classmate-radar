package source;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
            String url="jdbc:mysql://localhost:3306/abc";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,"root","arpit");
            Statement stm=con.createStatement();
            ob.database(uname,password,sess,out,stm,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void database(String uname,String password,HttpSession HS,PrintWriter out,Statement stm,HttpServletRequest request,HttpServletResponse response) throws Exception{
        String query="SELECT password FROM logindbs WHERE username ='"+uname+"'" ;
        ResultSet RS=stm.executeQuery(query);
        int xk=0;
//        System.out.println(RS.getString("password"));
        if(!RS.isBeforeFirst()){
            HS.setAttribute("correct","notExists");

            System.out.println("not exists "+query);
        }
        else if(RS.next()&&!RS.getString("password").equals(password)){
            HS.setAttribute("correct","wrong");
            System.out.println("wong password "+RS.getString("password")+" "+password);
        }
        else {
            xk=1;
            response.sendRedirect(response.encodeRedirectURL("location.jsp"));
        }
        if(xk==0)
            response.sendRedirect(request.getContextPath());
        stm.close();
    }
}
