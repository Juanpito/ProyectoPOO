package Vista;

import Controlador.ControladorArriendoEquipos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class ListadoClientes extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private TableModel tableModel;


    public ListadoClientes(String [][]datosListado,String []columnas) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // Construye el model que almacena los datos y los nombres de las columnas
        tableModel = new DefaultTableModel(datosListado, columnas){
            // La sobreescritura del metodo isCellEditable permite dejar la tabla no editable,
            // pero aun permite que se pueda seleccionar una fila haciendo doble clic en ella
            @Override
            public boolean isCellEditable(int row, int column) {
                // Todas las posiciones de la tabla en false
                return false;
            }
        };
        // Asocia el model a la JTable
        table1.setModel(tableModel);

        // Justifica las columnas a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i=0; i<table1.getColumnCount();i++) {
            table1.getColumnModel().getColumn(i)
                    .setCellRenderer(rightRenderer);

        }

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



    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public static void display(String datosListado[][],String []columnas) {
        ListadoClientes dialog = new ListadoClientes(datosListado,columnas);
        dialog.pack();
        dialog.setVisible(true);
    }
}
