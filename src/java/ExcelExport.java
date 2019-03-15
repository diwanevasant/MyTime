
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vasant
 */
public class ExcelExport {
    
    public static void main(String args[]){
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
        ResultSet resultSet = statement.executeQuery("select * from time_details");
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        XSSFSheet spreadsheet = workbook.createSheet("time_sheet");

        XSSFRow row = spreadsheet.createRow(1);
        XSSFCell cell;
        cell = row.createCell(1);
        cell.setCellValue("User");
        cell = row.createCell(2);
        cell.setCellValue("Date");
        cell = row.createCell(3);
        cell.setCellValue("Login Time");
        cell = row.createCell(4);
        cell.setCellValue("Logout Time");
        int i = 2;

        while(resultSet.next()) {
           row = spreadsheet.createRow(i);
           cell = row.createCell(1);
           cell.setCellValue(resultSet.getString("user"));
           cell = row.createCell(2);
           cell.setCellValue(resultSet.getString("date"));
           cell = row.createCell(3);
           cell.setCellValue(resultSet.getString("login_time"));
           cell = row.createCell(4);
           cell.setCellValue(resultSet.getString("logout_time"));
           i++;
        }

        FileOutputStream out = new FileOutputStream(new File("shift_details.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("exceldatabase.xlsx written successfully");
     }  catch (ClassNotFoundException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(export.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
