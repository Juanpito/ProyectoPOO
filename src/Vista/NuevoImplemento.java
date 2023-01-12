package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.EquipoException;

import javax.swing.*;
import java.awt.event.*;

public class NuevoImplemento extends JDialog {
    private JPanel panel1;
    private JButton OKButton;
    private JButton volverButton;
    private JTextField textFieldCodigo;
    private JTextField textFieldDescripcion;
    private JLabel Codigo;
    private JTextField textFieldPrecio;
    private JLabel Descripcion;
    private JLabel PrecioarriendoDia;

    public NuevoImplemento() {
        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(OKButton);

        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        volverButton.addActionListener(new ActionListener() {
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
        panel1.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void onOK(){
        String codigo = textFieldCodigo.getText();
        String descripcion = textFieldDescripcion.getText();
        String precio = textFieldPrecio.getText();


        if (!codigo.isEmpty()&&!descripcion.isEmpty()&&!precio.isEmpty()) {

            try {
                long cod = Long.parseLong(codigo);
                long pres = Long.parseLong(precio);
                ControladorArriendoEquipos.getInstance().creaImplemento(cod,descripcion,pres);
                JOptionPane.showMessageDialog(this,"Implemento creado satisfactoriamente","Mensaje",JOptionPane.INFORMATION_MESSAGE);
                textFieldDescripcion.setText("");
                textFieldPrecio.setText("");
                textFieldCodigo.setText("");
            }catch (EquipoException e){
                JOptionPane.showMessageDialog(this,e.getMessage(),"Advertencia",JOptionPane.WARNING_MESSAGE);
            }catch (NumberFormatException | NullPointerException nfe) {
                JOptionPane.showMessageDialog(this,"Ha ocurrido un error, debe ingreser solo datos numericos","",JOptionPane.ERROR_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(this,"Ha ocurrido un error, no se puede ingresar el implemento","",JOptionPane.ERROR_MESSAGE);
        }
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public static void display() {
        NuevoImplemento dialog = new NuevoImplemento();
        dialog.pack();
        dialog.setVisible(true);
    }
}
