package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;

public class ListadoPagosArriendo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;

    public ListadoPagosArriendo(String[][] datos, String[] columnas, long codigo) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        TableModel tableModel = new DefaultTableModel(datos, columnas);
        table1.setModel(tableModel);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        String codigo = JOptionPane.showInputDialog("Ingrese codigo del arriendo");
        if(codigo.equals("")){
            JOptionPane.showMessageDialog(null, "Datos invalidos");
            return;
        }

        try{
            String[][] pagos = ControladorArriendoEquipos.getInstance().listaPagosDeArriendo(Long.parseLong(codigo));

            String[] columnas = {"Monto", "Fecha", "Tipo pago"};
            long codigoL = Long.parseLong(codigo);

            ListadoPagosArriendo dialog = new ListadoPagosArriendo(pagos, columnas, (codigoL));

            dialog.pack();
            dialog.setVisible(true);
            System.exit(0);
        }catch (ArriendoException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

