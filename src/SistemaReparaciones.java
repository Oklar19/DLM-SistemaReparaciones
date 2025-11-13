import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SistemaReparaciones {
    // Declaramos los componentes como variables de clase para acceder desde
    // cualquier método
    private static JTextField campoNombre, campoDocumento, campoCelular, campoCorreo, campoCosto;
    private static JRadioButton rbMovil, rbComputadora, rbCorrectivo, rbPreventivo;
    private static JTextArea areaDiagnostico, areaDescripcion;

    public static void main(String[] args) {
        // Crear la ventana principal
        JFrame ventana = new JFrame("Sistema de Reparaciones - Celulares y Computadoras");

        // Configurar tamaño y posición
        ventana.setSize(500, 800); // Aumentamos la altura para más campos
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null); // Centrar en pantalla

        // Crear panel principal
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240)); // Color de fondo gris claro
        ventana.add(panel);
        DatabaseManager.crearTablas();

        // Llamar a método que crea los componentes
        crearComponentes(panel);

        // Hacer visible la ventana
        ventana.setVisible(true);
    }

    private static void crearComponentes(JPanel panel) {
        panel.setLayout(null); // Usaremos posiciones manuales

        //  TÍTULO 
        JLabel titulo = new JLabel(" SISTEMA DE REPARACIONES");
        titulo.setBounds(150, 20, 250, 30);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo);

        //  INFORMACIÓN DEL CLIENTE 
        JLabel seccionCliente = new JLabel("INFORMACIÓN DEL CLIENTE:");
        seccionCliente.setBounds(50, 60, 200, 20);
        seccionCliente.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(seccionCliente);

        // Campo Nombre
        agregarCampo(panel, "Nombre Completo:", 90, campoNombre = new JTextField());

        // Campo Documento
        agregarCampo(panel, "Número de Documento:", 140, campoDocumento = new JTextField());

        // Campo Celular
        agregarCampo(panel, "Celular:", 190, campoCelular = new JTextField());

        // Campo Correo
        agregarCampo(panel, "Correo Electrónico:", 240, campoCorreo = new JTextField());

        // ==================== TIPO DE DISPOSITIVO ====================
        JLabel seccionDispositivo = new JLabel("TIPO DE DISPOSITIVO:");
        seccionDispositivo.setBounds(50, 290, 200, 20);
        seccionDispositivo.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(seccionDispositivo);

        // Botones para dispositivo
        rbMovil = new JRadioButton("Dispositivo Móvil");
        rbComputadora = new JRadioButton("Computadora");

        rbMovil.setBounds(50, 315, 150, 25);
        rbComputadora.setBounds(200, 315, 150, 25);

        // Grupo para que solo se pueda seleccionar uno
        ButtonGroup grupoDispositivo = new ButtonGroup();
        grupoDispositivo.add(rbMovil);
        grupoDispositivo.add(rbComputadora);

        panel.add(rbMovil);
        panel.add(rbComputadora);

        //  TIPO DE MANTENIMIENTO 
        JLabel seccionMantenimiento = new JLabel("TIPO DE MANTENIMIENTO:");
        seccionMantenimiento.setBounds(50, 350, 200, 20);
        seccionMantenimiento.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(seccionMantenimiento);

        // Botones para mantenimiento
        rbCorrectivo = new JRadioButton("Mantenimiento Correctivo");
        rbPreventivo = new JRadioButton("Mantenimiento Preventivo");

        rbCorrectivo.setBounds(50, 375, 200, 25);
        rbPreventivo.setBounds(250, 375, 200, 25);

        // Grupo para que solo se pueda seleccionar uno
        ButtonGroup grupoMantenimiento = new ButtonGroup();
        grupoMantenimiento.add(rbCorrectivo);
        grupoMantenimiento.add(rbPreventivo);

        panel.add(rbCorrectivo);
        panel.add(rbPreventivo);

        // DIAGNÓSTICO 
        JLabel labelDiagnostico = new JLabel("DIAGNÓSTICO:");
        labelDiagnostico.setBounds(50, 410, 150, 20);
        labelDiagnostico.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelDiagnostico);

        areaDiagnostico = new JTextArea();
        areaDiagnostico.setBounds(50, 435, 400, 60);
        areaDiagnostico.setLineWrap(true); // Texto que baja de línea automáticamente
        panel.add(areaDiagnostico);

        //  COSTO 
        JLabel labelCosto = new JLabel("COSTO DEL SERVICIO:");
        labelCosto.setBounds(50, 510, 150, 20);
        labelCosto.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelCosto);

        campoCosto = new JTextField();
        campoCosto.setBounds(50, 535, 150, 25);
        panel.add(campoCosto);

        JLabel labelPesos = new JLabel("$");
        labelPesos.setBounds(35, 535, 15, 25);
        panel.add(labelPesos);

        //  DESCRIPCIÓN DEL SERVICIO 
        JLabel labelDescripcion = new JLabel("DESCRIPCIÓN DEL SERVICIO:");
        labelDescripcion.setBounds(50, 570, 200, 20);
        labelDescripcion.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelDescripcion);

        areaDescripcion = new JTextArea();
        areaDescripcion.setBounds(50, 595, 400, 60);
        areaDescripcion.setLineWrap(true);
        panel.add(areaDescripcion);

        // GUARDAR REPORTE 
        JButton GuardarReporte = new JButton("GUARDAR REPORTE");
        GuardarReporte.setBounds(150, 670, 200, 35);
        GuardarReporte.setBackground(new Color(70, 130, 180)); // Color azul
        GuardarReporte.setForeground(Color.WHITE);
        GuardarReporte.setFont(new Font("Arial", Font.BOLD, 12));

        // AGREGAR ACCIÓN AL BOTÓN
       GuardarReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        panel.add(GuardarReporte);

        // BOTÓN VER DATOS  
        
        JButton botonVerDatos = new JButton("VER HISTORIAL");
        botonVerDatos.setBounds(150, 710, 200, 35);
        botonVerDatos.setBackground(new Color(155, 89, 182)); // Color morado
        botonVerDatos.setForeground(Color.WHITE);
        botonVerDatos.setFont(new Font("Arial", Font.BOLD, 12));

        botonVerDatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VistaDatos.mostrarVentanaDatos();
            }
        });

        panel.add(botonVerDatos);

    // Para documento y celular (solo números)
campoDocumento.addKeyListener(new KeyAdapter() {
    public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar())) {
            e.consume();
        }
    }
});

campoCelular.addKeyListener(new KeyAdapter() {
    public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar())) {
            e.consume();
        }
    }
});

// Para costo (números y punto)
campoCosto.addKeyListener(new KeyAdapter() {
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) && c != '.') {
            e.consume();
        }
    }
});   
    }

    // Método auxiliar para agregar campos de texto más fácilmente
    private static void agregarCampo(JPanel panel, String textoLabel, int posY, JTextField campo) {
        JLabel label = new JLabel(textoLabel);
        label.setBounds(50, posY, 150, 20);
        panel.add(label);

        campo.setBounds(50, posY + 25, 400, 25);
        panel.add(campo);
    }

    private static void generarReporte() {
        // 1. CAPTURAR TODOS LOS DATOS
        String nombre = campoNombre.getText();
        String documento = campoDocumento.getText();
        String celular = campoCelular.getText();
        String correo = campoCorreo.getText();

        // 2. CAPTURAR DISPOSITIVO SELECCIONADO
        String dispositivo = "";
        if (rbMovil.isSelected()) {
            dispositivo = "Dispositivo Móvil";
        } else if (rbComputadora.isSelected()) {
            dispositivo = "Computadora";
        }

        // 3. CAPTURAR MANTENIMIENTO SELECCIONADO
        String mantenimiento = "";
        if (rbCorrectivo.isSelected()) {
            mantenimiento = "Correctivo";
        } else if (rbPreventivo.isSelected()) {
            mantenimiento = "Preventivo";
        }

        // 4. CAPTURAR LOS DEMÁS DATOS
        String diagnostico = areaDiagnostico.getText();
        String costo = campoCosto.getText();
        String descripcion = areaDescripcion.getText();

        // 5. VALIDAR DATOS OBLIGATORIOS
        if (nombre.isEmpty() || documento.isEmpty() || dispositivo.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "❌ Por favor complete:\n- Nombre\n- Documento\n- Tipo de dispositivo",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 7. GUARDAR EN BASE DE DATOS
        try {
            // Guardar cliente
            int idCliente = guardarCliente(nombre, documento, celular, correo);

            // Guardar reparación
            guardarReparacion(idCliente, dispositivo, mantenimiento, diagnostico, costo, descripcion);

            // 8. GENERAR PDF
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Error guardando en base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int guardarCliente(String nombre, String documento, String celular, String correo)
            throws SQLException {
        String sql = "INSERT INTO clientes (nombre_completo, numero_documento, celular, correo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, documento);
            pstmt.setString(3, celular);
            pstmt.setString(4, correo);
            pstmt.executeUpdate();

            // Obtener el ID generado automáticamente
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    private static void guardarReparacion(int idCliente, String dispositivo, String mantenimiento,
            String diagnostico, String costo, String descripcion) throws SQLException {
        String sql = """
                INSERT INTO reparaciones
                (id_cliente, tipo_dispositivo, tipo_mantenimiento, diagnostico, costo, descripcion_servicio)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            pstmt.setString(2, dispositivo);
            pstmt.setString(3, mantenimiento);
            pstmt.setString(4, diagnostico);
            pstmt.setString(5, costo.isEmpty() ? "0" : costo); // Si no hay costo, poner 0
            pstmt.setString(6, descripcion);
            pstmt.executeUpdate();
        }
    }
}