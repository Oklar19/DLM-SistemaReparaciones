import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:reparaciones.db";

    // Crear conexión a la base de datos
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Crear tablas si no existen
    public static void crearTablas() {
        String sqlClientes = """
                CREATE TABLE IF NOT EXISTS clientes (
                    id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre_completo TEXT NOT NULL,
                    numero_documento TEXT NOT NULL,
                    celular TEXT,
                    correo TEXT,
                    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;

        String sqlReparaciones = """
                CREATE TABLE IF NOT EXISTS reparaciones (
                    id_reparacion INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_cliente INTEGER NOT NULL,
                    tipo_dispositivo TEXT NOT NULL,
                    tipo_mantenimiento TEXT NOT NULL,
                    diagnostico TEXT,
                    costo REAL,
                    descripcion_servicio TEXT,
                    fecha_servicio DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (id_cliente) REFERENCES clientes (id_cliente)
                )
                """;

        try (Connection conn = conectar();
                Statement stmt = conn.createStatement()) {

            stmt.execute(sqlClientes);
            stmt.execute(sqlReparaciones);
            System.out.println("✅ Tablas creadas/existen en la base de datos");

        } catch (SQLException e) {
            System.err.println("❌ Error creando tablas: " + e.getMessage());
        }
    }

    // Método para ver todos los datos guardados
    public static void verDatosGuardados() {
        String sqlClientes = "SELECT * FROM clientes";
        String sqlReparaciones = "SELECT * FROM reparaciones";

        try (Connection conn = conectar();
                Statement stmt = conn.createStatement();
                ResultSet rsClientes = stmt.executeQuery(sqlClientes)) {

            System.out.println("📊 CLIENTES GUARDADOS:");
            System.out.println("----------------------");

            while (rsClientes.next()) {
                System.out.println("ID: " + rsClientes.getInt("id_cliente"));
                System.out.println("Nombre: " + rsClientes.getString("nombre_completo"));
                System.out.println("Documento: " + rsClientes.getString("numero_documento"));
                System.out.println("Celular: " + rsClientes.getString("celular"));
                System.out.println("Correo: " + rsClientes.getString("correo"));
                System.out.println("Fecha: " + rsClientes.getString("fecha_registro"));
                System.out.println("----------------------");
            }

            // Ver reparaciones
            try (ResultSet rsReparaciones = stmt.executeQuery(sqlReparaciones)) {
                System.out.println("🔧 REPARACIONES GUARDADAS:");
                System.out.println("----------------------");

                while (rsReparaciones.next()) {
                    System.out.println("ID Reparación: " + rsReparaciones.getInt("id_reparacion"));
                    System.out.println("ID Cliente: " + rsReparaciones.getInt("id_cliente"));
                    System.out.println("Dispositivo: " + rsReparaciones.getString("tipo_dispositivo"));
                    System.out.println("Mantenimiento: " + rsReparaciones.getString("tipo_mantenimiento"));
                    System.out.println("Costo: $" + rsReparaciones.getDouble("costo"));
                    System.out.println("Fecha: " + rsReparaciones.getString("fecha_servicio"));
                    System.out.println("----------------------");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error leyendo datos: " + e.getMessage());
        }
    }
}
