/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vasant
 */
public class logout extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String currentTime=null;
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            try{  
                Class.forName("com.mysql.jdbc.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:mysql://localhost:3306/my_time","vasant","vasant");
                Statement stmt=con.createStatement(); 
                currentTime=new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String currentDate=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                String email= request.getParameter("email");
                String password= request.getParameter("password");
                System.out.println("Email:"+email+"Passowrd:"+password);                
                ResultSet rs = stmt.executeQuery("select password from users where email='" + email + "';");
                String password_from_db = null;
                while (rs.next()) {
                    password_from_db = rs.getString("password");
                }
                if (password.equals(password_from_db)) {
                    ResultSet rs_logged_out = stmt.executeQuery("select * from time_details where user='"+email+"' and date='"+currentDate+"' and logout_time !='00:00:00';");
                    if(!rs_logged_out.next()){
                        stmt.executeUpdate("update time_details set logout_time='"+currentTime+"' where user='"+email+"' and date='"+currentDate+"';");
                        TimeUnit.SECONDS.sleep(5);            
                        response.sendRedirect(request.getHeader("referer"));
                    }
                    else{
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>MyTime Login</title>");
                        out.println(" <link rel='stylesheet' href='http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css'>\n"
                                + "\n"
                                + "      <link rel=\"stylesheet\" href=\"css/style.css\">");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<div class='login'><div style='color: rgb(213, 216, 226)'><h2>You are already logged out</h2></div></div>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                } else {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>MyTime LogOut</title>");
                    out.println(" <link rel='stylesheet' href='http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css'>\n"
                            + "\n"
                            + "      <link rel=\"stylesheet\" href=\"css/style.css\">");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div class='login'><div style='color: rgb(213, 216, 226)'><h2>Authentication Failure!</h2><p>Please try again</p></div></div>");
                    out.println("</body>");
                    out.println("</html>");
                    //response.sendRedirect(request.getHeader("referer"));

                }    
                con.close(); 
                }catch(Exception e){ System.out.println(e);} 
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
