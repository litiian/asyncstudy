package com.study.phnenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	 try {
             List<Map<String, String>> result = new ArrayList<>();
             byte[] collectTime = convertDateToByte(new Date());
             Date date=new Date();
             String sql = "SELECT * FROM V_REPORT WHERE ROW_KEY >= ? AND ROW_KEY <= ? ORDER BY ROW_KEY DESC";
             sql="select * from v_report limit 10";
             sql="select * from v_report where ROW_KEY like bytes("+date+")";
             Connection connection = DriverManager.getConnection(String.format("jdbc:phoenix:%s", "172.16.4.50,172.16.4.69,172.16.4.70"));
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setBytes(1, collectTime);
             ResultSet resultSet = preparedStatement.executeQuery();
             ResultSetMetaData metaData = resultSet.getMetaData();
             while (resultSet.next()) {
                 Map<String, String> values = new HashMap<>();
                 for (int i = 2; i < 73; i++) {
                     String columnName = metaData.getColumnName(i);
                     Object o = resultSet.getObject(i);
                     if (o != null) {
                         values.put(columnName, o.toString());
                     } else {
                         values.put(columnName, "");
                     }
                 }
                 System.out.println("==="+values) ;
                 result.add(values);
             }
            // System.out.println(result.toString()) ;
         } catch (SQLException e) {
            e.printStackTrace();
         }
    }
    
    public static byte[] convertDateToByte(Date date) {
        Calendar calendar = Calendar.getInstance();

        if (date != null) {
            calendar.setTime(date);
        }

        byte[] result = new byte[6];
        result[0] = (byte)(calendar.get(Calendar.YEAR) -2000);
        result[1] = (byte)(calendar.get(Calendar.MONTH) + 1);
        result[2] = (byte)calendar.get(Calendar.DATE);
        result[3] = (byte)calendar.get(Calendar.HOUR_OF_DAY);
        result[4] = (byte)calendar.get(Calendar.MINUTE);
        result[5] = (byte)calendar.get(Calendar.SECOND);

        return result;
    }
}
