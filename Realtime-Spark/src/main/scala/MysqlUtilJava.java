import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class MysqlUtilJava {
    public static ArrayList<JSONObject> queryList(String sql) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
        Connection conn = DriverManager.getConnection("jdbc:mysql://39.103.148.195:3306/play?characterEncoding=utf-8&useSSL=false","root","lovely0620");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        ResultSetMetaData md = rs.getMetaData();
        while (rs.next()) {
            JSONObject rowData = new JSONObject();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            resultList.add(rowData);
        }
        stat.close();
        conn.close();
        return resultList;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ArrayList<JSONObject> result = queryList("select * from Employee");
        System.out.println(result);
    }
}
