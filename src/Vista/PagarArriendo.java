package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PagarArriendo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField buscarArriendo;
    private JButton buscarArriendoButton;
    private JLabel Estado;
    private JLabel rutCliente;
    private JLabel nombreCLiente;
    private JLabel montoTotal;
    private JLabel montoPagado;
    private JLabel montoAdeudado;
    private JRadioButton contadoRadioButton;
    private JRadioButton debitoRadioButton;
    private JRadioButton creditoRadioButton;
    private JLabel fechaHoy;
    private JTextField NumTransaccion;
    private JTextField NumTarjeta;
    private JTextField NumCuotas;
    private JLabel numtrans;
    private JLabel numtar;
    private JLabel numcuot;
    private JTextField Monto;

    public PagarArriendo() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        actualizaTextField(false, false, false);
        fechaHoy.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        contadoRadioButton.addActionListener(e -> actualizaTextField(false, false, false));
        debitoRadioButton.addActionListener(e -> actualizaTextField(true, true, false));
        creditoRadioButton.addActionListener(e -> actualizaTextField(true, true, true));

        buttonOK.addActionListener(e -> onOK());

        buscarArriendoButton.addActionListener(e -> buscarArriendo());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    private void actualizaTextField(boolean transaccion, boolean tarjeta, boolean coutas) {
        numtrans.setEnabled(transaccion);
        numtar.setEnabled(tarjeta);
        numcuot.setEnabled(coutas);
        numtrans.setEnabled(transaccion);
        numtar.setEnabled(tarjeta);
        numcuot.setEnabled(coutas);
    }

    private void onOK() {
        String codigoStr = buscarArriendo.getText();
        String montoStr = Monto.getText();
        String NumeroDeTransaccion = numtrans.getText();
        String NumeroDeTarjeta = numtar.getText();
        String NumeroDeCuotas = numcuot.getText();
        if (codigoStr.trim().equals("") || montoStr.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "No se pueden dejar campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            long codigo = Long.parseLong(codigoStr);
            long monto = Long.parseLong(montoStr);

            if (contadoRadioButton.isSelected()) {
                ControladorArriendoEquipos.getInstance().pagaArriendoContado(codigo, monto);
                JOptionPane.showMessageDialog(this, "Arriendo pagado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            boolean checker = NumeroDeTarjeta.trim().equals("") || NumeroDeTransaccion.trim().equals("");

            if (debitoRadioButton.isSelected() && !checker) {
                ControladorArriendoEquipos.getInstance().pagaArriendoDebito(codigo,monto,NumeroDeTransaccion,NumeroDeTarjeta);
            } else if (creditoRadioButton.isSelected() && !checker) {
                int cuotas = Integer.parseInt(NumeroDeCuotas);
                ControladorArriendoEquipos.getInstance().pagaArriendoCredito(codigo,monto,NumeroDeTransaccion, NumeroDeTarjeta, cuotas);
            } else {
                JOptionPane.showMessageDialog(this, "Los parametros no pueden estar vacios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Arriendo pagado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (ArriendoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los campos de codigo, monto y cuotas deben ser numericos", "Error", JOptionPane.ERROR_MESSAGE);
        }

        buscarArriendo.setText("");
        Monto.setText("");
        numtrans.setText("");
        numtar.setText("");
        numcuot.setText("");

        Estado.setText("");
        rutCliente.setText("");
        nombreCLiente.setText("");
        montoTotal.setText("");
        montoAdeudado.setText("");
        montoPagado.setText("");
    }
    private void buscarArriendo(){
        String CodigoDeArriendo = buscarArriendo.getText();
        if(!CodigoDeArriendo.isEmpty()){
            try {
                String[] consultaArriendo = ControladorArriendoEquipos.getInstance()
                        .consultaArriendoAPagar(Long.parseLong(CodigoDeArriendo));
                Estado.setText(consultaArriendo[1]);
                rutCliente.setText(consultaArriendo[2]);
                nombreCLiente.setText(consultaArriendo[3]);
                montoTotal.setText(consultaArriendo[4]);
                montoAdeudado.setText(consultaArriendo[6]);
                montoPagado.setText(consultaArriendo[5]);

            }catch(Exception e) {
                JOptionPane.showMessageDialog(this,"No existe arriendo","Advertencia",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void display(){
        PagarArriendo dialog = new PagarArriendo();
        dialog.pack();
        dialog.setVisible(true);
    }
}

