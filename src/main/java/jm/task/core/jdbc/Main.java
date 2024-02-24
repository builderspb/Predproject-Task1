package jm.task.core.jdbc;

import jm.task.core.jdbc.util.ConnectionManager;
import org.postgresql.Driver;
import java.sql.Connection;
import java.sql.SQLException;


// реализуйте алгоритм здесь
public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = ConnectionManager.open()){
            System.out.println("dfghj");
        }


    }
}

