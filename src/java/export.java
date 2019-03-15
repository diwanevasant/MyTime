/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author vasant
 */
@WebServlet(urlPatterns = {"/export"})
public class export extends HttpServlet {

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
        try {

            // Create new Excel workbook and sheet
            HSSFWorkbook xlsWorkbook = new HSSFWorkbook();
            HSSFSheet xlsSheet = xlsWorkbook.createSheet();
            short rowIndex = 0;

            // Execute SQL query
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/my_time", "vasant", "vasant");
            Statement statement = connection.createStatement();
            Date start_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("start_date"));
            Date end_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("end_date"));
            String s_date = new SimpleDateFormat("yyyy-MM-dd").format(start_date);
            String e_date = new SimpleDateFormat("yyyy-MM-dd").format(end_date);
            System.out.println("select * from time_details where date between '" + s_date + "' and '" + e_date + "';");
            ResultSet resultSet = statement.executeQuery("select * from time_details where date between '" + s_date + "' and '" + e_date + "';");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("time_sheet");

            XSSFRow row = spreadsheet.createRow(1);
            XSSFCell cell;
            cell = row.createCell(1);
            cell.setCellValue("User");
            cell = row.createCell(2);
            cell.setCellValue("Date");
            cell = row.createCell(3);
            cell.setCellValue("In Time");
            cell = row.createCell(4);
            cell.setCellValue("Out Time");
            cell = row.createCell(5);
            cell.setCellValue("Total Time Worked");
            int i = 2;

            while (resultSet.next()) {
                row = spreadsheet.createRow(i);
                cell = row.createCell(1);
                cell.setCellValue(resultSet.getString("user"));
                cell = row.createCell(2);
                cell.setCellValue(resultSet.getString("date"));
                cell = row.createCell(3);
                cell.setCellValue(resultSet.getString("login_time"));
                cell = row.createCell(4);
                cell.setCellValue(resultSet.getString("logout_time"));
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = format.parse(resultSet.getString("login_time"));
                    d2 = format.parse(resultSet.getString("logout_time"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Get msec from each, and subtract.
                long diff = d2.getTime() - d1.getTime();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);              
                cell = row.createCell(5);
                cell.setCellValue(diffHours+":"+diffMinutes+":"+diffSeconds);
                i++;
            }

            FileOutputStream out = new FileOutputStream(new File("/home/vasant/Downloads/shift_details.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("shift_details.xlsx written successfully");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
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
