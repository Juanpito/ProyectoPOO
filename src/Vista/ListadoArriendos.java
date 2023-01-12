package Vista;

import Controlador.ControladorArriendoEquipos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ListadoArriendos extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablaArriendos;
    private JTextField fechaDesdeTextField;
    private JTextField fechaHastaTextField;
    private String[]titulos;

    public ListadoArriendos() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonCancel.addActionListener(e -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonOK.addActionListener(e -> actualizarTabla());

        titulos = new String[]{"Codigo", "Fecha inicio", "Fecha devolución", "Estado", "Rut cliente", "Monto total"};
        TableModel modelo = new DefaultTableModel(null  ,titulos);
        tablaArriendos.setModel(modelo);

        LocalDate ahora = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechaDesdeTextField.setText(ahora.minusMonths(3).format(formatter));
        fechaHastaTextField.setText(ahora.plusDays(1).format(formatter));
    }

    private void actualizarTabla() {
        LocalDate fechaInicio, fechaTermino;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaInicio = LocalDate.parse(fechaDesdeTextField.getText(), formatter);
            fechaTermino = LocalDate.parse(fechaHastaTextField.getText(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "La fecha ingresada no es valida", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[][] datos = ControladorArriendoEquipos.getInstance().listaArriendos(fechaInicio, fechaTermino);
        if (datos.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TableModel modelo = new DefaultTableModel(datos, titulos);
        tablaArriendos.setModel(modelo);
    }

    public static void display() {
        ListadoArriendos dialog = new ListadoArriendos();
        dialog.pack();
        dialog.setVisible(true);
    }
}

