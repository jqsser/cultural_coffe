package tn.esprit.tools;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public static final String URL = "jdbc:mysql://127.0.0.1:3306/cafeCulture";
    public static final String USER = "root";
    public static final String PWD = "";
    private Connection cnx;
    private static MyDataBase myDataBase;

    private MyDataBase() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection established successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (myDataBase == null)
            myDataBase = new MyDataBase();
        return myDataBase;
    }

    public Connection getCnx() {
        return cnx;
    }
}

