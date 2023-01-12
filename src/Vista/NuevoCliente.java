package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ClienteException;

import javax.swing.*;
import java.awt.event.*;

public class NuevoCliente extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nombre;
    private JTextField rut;
    private JTextField direccion;
    private JTextField telefono;

    public NuevoCliente() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
        String StrNombre=nombre.getText();
        String StrRut=rut.getText();
        String StrDireccion=direccion.getText();
        String Strtelefono= telefono.getText();

        ///Aqui podria ir un if Radiobutton
        //if(organicoRadioButton.isSelected()){
        // tipoCultivo=Tipocultivo.ORGANICO;
        // }

        if(!StrNombre.isEmpty()&&!StrRut.isEmpty()&& !StrDireccion.isEmpty()
                &&!Strtelefono.isEmpty()){
            try{
                ControladorArriendoEquipos.getInstance().creaCliente(StrRut,StrNombre,StrDireccion,Strtelefono);
                JOptionPane.showMessageDialog(this,"Se ha creado cliente exitosamente", "",JOptionPane.PLAIN_MESSAGE);





            } catch (ClienteException e) {
                throw new RuntimeException(e);
            }//aqui podria bien ir un catch(NumberFormatException e){
            // JOptionPane.showMessageDialog(this, "id no valido","",JOptionPane.Error_Message);
            //
            // }catch (XXXEcpetion e){
            // JoptionPane.showMessageDialog(this, e.getMessage(),[....])}

        }else{
            JOptionPane.showMessageDialog(this,"uno o mas datos ausentes","",JOptionPane.ERROR_MESSAGE);
        }

        nombre.setText("");
        rut.setText("");
        direccion.setText("");
        telefono.setText("");
        //aqui podria haber un button organicoRadioButton.setSelected(true);




        //aqui sacamos el dispose(para que no arroje salir de la ventana)


    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        NuevoCliente dialog = new NuevoCliente();
        dialog.pack();
        dialog.setVisible(true);
    }


}
