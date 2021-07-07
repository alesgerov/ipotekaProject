package jdbc.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {

    public static Connection open(Connection connection){
        Properties properties=new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
            String url=properties.getProperty("jdbc.url");
            String username=properties.getProperty("jdbc.username");
            String password=properties.getProperty("jdbc.password");
            String driver=properties.getProperty("jdbc.driver");

            Class.forName(driver);
            connection=DriverManager.getConnection(url,username,password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        return connection;
    }


    public static void close(ResultSet resultSet, PreparedStatement preparedStatement,Connection connection){
        try{
            if (resultSet!=null) resultSet.close();
            if (preparedStatement!=null) preparedStatement.close();
            if (connection!=null) connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void close(ResultSet resultSet, CallableStatement preparedStatement,Connection connection){
        try{
            if (resultSet!=null) resultSet.close();
            if (preparedStatement!=null) preparedStatement.close();
            if (connection!=null) connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
