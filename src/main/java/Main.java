import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args){
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");
        String bdName = "BibliotecaJava";
        String url = "jdbc:postgresql://localhost:5432/"+bdName;

        try (Connection con = DriverManager.getConnection(url, username, password)){
            System.out.println("Conectado a la base de datos PostgreSQL");
            Menu.menu(con);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
