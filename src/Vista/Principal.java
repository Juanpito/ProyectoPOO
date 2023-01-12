package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Modelo.Cliente;
import Modelo.Implemento;

import javax.swing.*;
import java.awt.event.*;

public class Principal extends JDialog {
    private JPanel contentPane;
    private JButton GuardarButton;
    private JButton salirButton;
    private JButton arriendaEquipoButton;
    private JButton nuevoClienteButton;
    private JButton nuevoImplementoButton;
    private JButton nuevoConjuntoButton;
    private JButton devuelveEquipoButton;
    private JButton pagaArriendoButton;
    private JButton listadoArriendosButton;
    private JButton listadoArriendosButton1;
    private JButton listadoClientesButton;
    private JButton listadoEquiposButton;
    private JButton listadoPagosArriendoButton;
    private JButton detalleDeUnArriendoButton;
    private JButton abrirButton;

    public Principal() {
        setContentPane(contentPane);
        setModal(true);




        salirButton.addActionListener(new ActionListener() {
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

        abrirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onabrirButton();

            }


        });
        GuardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onguardarButton();

            }

        });
        arriendaEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        nuevoClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NuevoCliente.display();

            }
        });
        nuevoImplementoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NuevoImplemento.display();
            }
        });
        nuevoConjuntoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        devuelveEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        pagaArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        listadoArriendosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        listadoArriendosButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        listadoEquiposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        listadoPagosArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        detalleDeUnArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        pagaArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PagarArriendo.display();
            }
        });
        arriendaEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArriendaEquipo.display();
            }
        });
        nuevoConjuntoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NuevoConjunto.display();
            }
        });
        devuelveEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DevuelveEquipo.display();
            }
        });
        listadoArriendosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadoArriendosPagados.display();
            }
        });
        listadoArriendosButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadoArriendos.display();
            }
        });
        listadoClientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onListarClientes();

            }
        });
        listadoEquiposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadoEquipos.display();
            }
        });
        listadoPagosArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadoPagosArriendo.display();
            }
        });
        detalleDeUnArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DetalleDeUnArriendo.display();
            }
        });
    }

    private void onListarClientes() {
        String[][] datosListado = ControladorArriendoEquipos.getInstance().listaClientes();
        if (datosListado.length > 0) {
            String[] columnas = {"Rut", "Nombre", "Dirección", "Telefono", "Activo/Inactivo"};
            ListadoClientes.display(datosListado,columnas);
        } else {
            JOptionPane.showMessageDialog(this,"No existen datos asociados", "",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onguardarButton() {
        try {
            ControladorArriendoEquipos.getInstance().saveDatosSistemas();
            JOptionPane.showMessageDialog(this, "Datos guardos correctamente", "info", JOptionPane.INFORMATION_MESSAGE);
        } catch (ArriendoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void onabrirButton() {
        try {
            ControladorArriendoEquipos.getInstance().readDatosSistema();
            JOptionPane.showMessageDialog(this, "Datos cargados correctamente", "info", JOptionPane.INFORMATION_MESSAGE);
        } catch (ArriendoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }




    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        Principal dialog = new Principal();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
