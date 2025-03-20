package DB;

import java.sql.*;

public class DB {

    private static Connection connection = null;

    public static Connection getConnection() {
        String url = "jdbc:sqlite:C:/SQLITE/db/BdAula.db"; // Verifique se o caminho está correto

        if (connection == null) {
            try {
                // Carregar o driver manualmente
                Class.forName("org.sqlite.JDBC");

                // Estabelecer a conexão
                connection = DriverManager.getConnection(url);


                System.out.println("Conexão com SQLite estabelecida.");

               // connection.close(); // Sempre feche a conexão após o uso
            } catch (ClassNotFoundException e) {
                System.out.println("Driver JDBC do SQLite não encontrado: " + e.getMessage());
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão Fechada!");
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            } finally {

                connection = null;
            }

        }
    }

    public static void closeStatment(Statement st){
            if(st != null){
                try {
                    st.close();
                } catch (SQLException e) {
                    throw new DbException(e.getMessage());
                }
            }
    }

    public static void closeResulSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
