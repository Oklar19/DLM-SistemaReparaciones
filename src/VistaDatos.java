import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaDatos {
    
    public static void mostrarVentanaDatos() {
        // Crear ventana
        JFrame ventana = new JFrame("📊 Datos Guardados - Sistema de Reparaciones");
        ventana.setSize(900, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel principal con color de fondo
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("📋 HISTORIAL DE REPARACIONES");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(41, 128, 185));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Modelo de tabla
        DefaultTableModel modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Documento");
        modeloTabla.addColumn("Dispositivo");
        modeloTabla.addColumn("Servicio");
        modeloTabla.addColumn("Costo");
        modeloTabla.addColumn("Fecha");
        
        // Tabla
        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(52, 152, 219));
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        // Scroll para la tabla
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(245, 245, 245));
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        JButton btnVerDetalles = new JButton("👁️ Ver Detalles");
        JButton btnCerrar = new JButton("❌ Cerrar");
        
        // Estilos de botones
        for (JButton btn : new JButton[]{btnActualizar, btnVerDetalles, btnCerrar}) {
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setBackground(new Color(46, 204, 113));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        }
        
        btnCerrar.setBackground(new Color(231, 76, 60));
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnCerrar);
        
        // Cargar datos
        cargarDatosEnTabla(modeloTabla);
        
        // Acciones de botones
        btnActualizar.addActionListener(e -> {
            modeloTabla.setRowCount(0); // Limpiar tabla
            cargarDatosEnTabla(modeloTabla);
            JOptionPane.showMessageDialog(ventana, "Datos actualizados correctamente", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnVerDetalles.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(ventana, "Selecciona un registro para ver detalles", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            mostrarDetallesRegistro(filaSeleccionada, modeloTabla);
        });
        
        btnCerrar.addActionListener(e -> ventana.dispose());
        
        // Ensamblar ventana
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        ventana.add(panel);
        ventana.setVisible(true);
    }
    
    private static void cargarDatosEnTabla(DefaultTableModel modeloTabla) {
        String sql = """
            SELECT 
                r.id_reparacion, c.nombre_completo, c.numero_documento, 
                r.tipo_dispositivo, r.tipo_mantenimiento, r.costo, r.fecha_servicio
            FROM reparaciones r
            JOIN clientes c ON r.id_cliente = c.id_cliente
            ORDER BY r.fecha_servicio DESC
            """;
        
        try (Connection conn = DatabaseManager.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_reparacion"),
                    rs.getString("nombre_completo"),
                    rs.getString("numero_documento"),
                    rs.getString("tipo_dispositivo"),
                    rs.getString("tipo_mantenimiento"),
                    "$" + rs.getDouble("costo"),
                    rs.getString("fecha_servicio").substring(0, 16) // Solo fecha y hora
                };
                modeloTabla.addRow(fila);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error cargando datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void mostrarDetallesRegistro(int fila, DefaultTableModel modeloTabla) {
        int idReparacion = (int) modeloTabla.getValueAt(fila, 0);
        
        String sql = """
            SELECT 
                c.nombre_completo, c.numero_documento, c.celular, c.correo,
                r.tipo_dispositivo, r.tipo_mantenimiento, r.diagnostico, 
                r.costo, r.descripcion_servicio, r.fecha_servicio
            FROM reparaciones r
            JOIN clientes c ON r.id_cliente = c.id_cliente
            WHERE r.id_reparacion = ?
            """;
        
        try (Connection conn = DatabaseManager.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReparacion);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String detalles = 
                    "👤 INFORMACIÓN DEL CLIENTE:\n" +
                    "────────────────────────────\n" +
                    "• Nombre: " + rs.getString("nombre_completo") + "\n" +
                    "• Documento: " + rs.getString("numero_documento") + "\n" +
                    "• Celular: " + rs.getString("celular") + "\n" +
                    "• Correo: " + rs.getString("correo") + "\n\n" +
                    
                    "🔧 INFORMACIÓN DEL SERVICIO:\n" +
                    "────────────────────────────\n" +
                    "• Dispositivo: " + rs.getString("tipo_dispositivo") + "\n" +
                    "• Mantenimiento: " + rs.getString("tipo_mantenimiento") + "\n" +
                    "• Costo: $" + rs.getDouble("costo") + "\n" +
                    "• Fecha: " + rs.getString("fecha_servicio") + "\n\n" +
                    
                    "🔍 DIAGNÓSTICO:\n" +
                    "────────────────────────────\n" +
                    rs.getString("diagnostico") + "\n\n" +
                    
                    "📝 DESCRIPCIÓN DEL SERVICIO:\n" +
                    "────────────────────────────\n" +
                    rs.getString("descripcion_servicio");
                
                JTextArea areaTexto = new JTextArea(detalles);
                areaTexto.setEditable(false);
                areaTexto.setFont(new Font("Consolas", Font.PLAIN, 12));
                areaTexto.setBackground(new Color(248, 248, 248));
                
                JScrollPane scroll = new JScrollPane(areaTexto);
                scroll.setPreferredSize(new Dimension(500, 400));
                
                JOptionPane.showMessageDialog(null, scroll, "📋 Detalles Completos del Registro", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error cargando detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}