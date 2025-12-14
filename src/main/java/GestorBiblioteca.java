import java.sql.*;

public class GestorBiblioteca {
    private final Connection con;

    public GestorBiblioteca(Connection con) {
        this.con = con;
    }

    public void crearLibro(String titulo, String autor, String editorial) {
        String sql = "INSERT INTO Libro (titulo, nombre_autor, editorial) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, editorial);
            pstmt.executeUpdate();
            System.out.println("Libro guardado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarLibros() {
        String sql = "SELECT id_libro, titulo, nombre_autor, editorial, disponible FROM Libro";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- LISTA DE LIBROS ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Título: %s | Autor: %s | Editorial: %s | Disponible: %b%n",
                        rs.getInt("id_libro"),
                        rs.getString("titulo"),
                        rs.getString("nombre_autor"),
                        rs.getString("editorial"),
                        rs.getBoolean("disponible"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarLibro(int id, String titulo, String autor, String editorial) {
        String sql = "UPDATE Libro SET titulo = ?, nombre_autor = ?, editorial = ? WHERE id_libro = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, editorial);
            pstmt.setInt(4, id);

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                System.out.println("Libro actualizado correctamente. ");
            } else {
                System.out.println("No se encontró el libro con ID " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrarLibro(int id) {
        String sql = "DELETE FROM Libro WHERE id_libro = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas > 0) System.out.println("Libro eliminado. ");
            else System.out.println("No se encontró el libro ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void realizarPrestamo(int libroId, int clienteId) {
        String checkSql = "SELECT disponible FROM Libro WHERE id_libro = ?";
        String updateSql = "UPDATE Libro SET disponible = false WHERE id_libro = ?";
        String insertSql = "INSERT INTO Prestamo (id_libro, id_cliente) VALUES (?, ?)";

        try {
            con.setAutoCommit(false);

            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, libroId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                boolean isDisponible = rs.getBoolean("disponible");

                if (isDisponible) {
                    PreparedStatement updateStmt = con.prepareStatement(updateSql);
                    updateStmt.setInt(1, libroId);
                    updateStmt.executeUpdate();

                    PreparedStatement insertStmt = con.prepareStatement(insertSql);
                    insertStmt.setInt(1, libroId);
                    insertStmt.setInt(2, clienteId);
                    insertStmt.executeUpdate();

                    con.commit();
                    System.out.println("Préstamo realizado correctamente. ");
                } else {
                    System.out.println("El libro ya se encuentra prestado.");
                    con.rollback();
                }
            } else {
                System.out.println("El libro no existe.");
                con.rollback();
            }
            con.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                con.rollback();
                System.out.println("Error en la transacción. Se deshicieron los cambios. ");
                e.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}


