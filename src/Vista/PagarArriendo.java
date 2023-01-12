package Vista;

import Controlador.ControladorArriendoEquipos;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;

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
        NumTransaccion.setEnabled(false);
        NumTarjeta.setEnabled(false);
        NumCuotas.setEnabled(false);
        numtrans.setEnabled(false);
        numtar.setEnabled(false);
        numcuot.setEnabled(false);
        contadoRadioButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                contado();
            }
        });
        Monto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monto();
            }
        });
        creditoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                credito();
            }
        });
        debitoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debito();
            }
        });
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buscarArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {BuscarArriendo();}
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
    private void monto(){
        if(Monto !=null){
            NumTransaccion.setEnabled(true);
            NumTarjeta.setEnabled(true);
            NumCuotas.setEnabled(true);
            numtrans.setEnabled(true);
            numtar.setEnabled(true);
            numcuot.setEnabled(true);
        }else{
            NumTransaccion.setEnabled(false);
            NumTarjeta.setEnabled(false);
            NumCuotas.setEnabled(false);
            numtrans.setEnabled(false);
            numtar.setEnabled(false);
            numcuot.setEnabled(false);
        }
    }
    private void contado(){
        NumTransaccion.setEnabled(true);
        NumTarjeta.setEnabled(false);
        NumCuotas.setEnabled(false);
        numtrans.setEnabled(false);
        numtar.setEnabled(false);
        numcuot.setEnabled(false);
    }
    private void credito(){
        NumTransaccion.setEnabled(true);
        NumTarjeta.setEnabled(true);
        NumCuotas.setEnabled(true);
        numtrans.setEnabled(true);
        numtar.setEnabled(true);
        numcuot.setEnabled(true);
    }
    private void debito(){
        NumTransaccion.setEnabled(true);
        NumTarjeta.setEnabled(true);
        NumCuotas.setEnabled(true);
        numtrans.setEnabled(true);
        numtar.setEnabled(true);
        numcuot.setEnabled(true);
    }
    private void onOK() {

        String codigo= buscarArriendo.getText();
        String monto= Monto.getText();
        String NumeroDeTransaccion= NumTransaccion.getText();
        String NumeroDeTarjeta= NumTarjeta.getText();
        String NumeroDeCuotas= NumCuotas.getText();
        ButtonGroup grupo= new ButtonGroup();
        grupo.add(contadoRadioButton);
        grupo.add(debitoRadioButton);
        grupo.add(creditoRadioButton);

        if(contadoRadioButton.isSelected()&&!monto.isEmpty()){

            try{
                ControladorArriendoEquipos.getInstance().pagaArriendoContado(Long.parseLong(codigo),Long.parseLong(monto));

            }catch (Exception e){
                JOptionPane.showMessageDialog(this,"NO SE HA PODIDO PAGAR ","ERROR",JOptionPane.WARNING_MESSAGE);
            }
        } else if (creditoRadioButton.isSelected()) {
            try{
                ControladorArriendoEquipos.getInstance().pagaArriendoCredito(Long.parseLong(codigo),Long.parseLong(monto),NumeroDeTransaccion,NumeroDeTarjeta,Integer.parseInt(NumeroDeCuotas));
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,"NO SE HA PODIDO PAGAR ","ERROR",JOptionPane.WARNING_MESSAGE);
            }
        } else if (debitoRadioButton.isSelected()) {
            try {
                ControladorArriendoEquipos.getInstance().pagaArriendoDebito(Long.parseLong(codigo),Long.parseLong(monto),NumeroDeTransaccion,NumeroDeTarjeta);
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,"NO SE HA PODIDO PAGAR ","ERROR",JOptionPane.WARNING_MESSAGE);
            }

        }
        buscarArriendo.setText(" ");
        Monto.setText(" ");
        NumTransaccion.setText(" ");
        NumTarjeta.setText(" ");
        NumCuotas.setText(" ");

        dispose();
    }
    private void BuscarArriendo(){
        String CodigoDeArriendo = buscarArriendo.getText();
        if(!CodigoDeArriendo.isEmpty()){
            try {

                String[] consultaArriendo = ControladorArriendoEquipos.getInstance()
                        .consultaArriendoAPagar(Long.parseLong(CodigoDeArriendo));
                Estado.setText(consultaArriendo[1]);
                rutCliente.setText(consultaArriendo[2]);
                nombreCLiente.setText(consultaArriendo[3]);
                montoTotal.setText(consultaArriendo[4]);
                montoAdeudado.setText(consultaArriendo[5]);
                montoPagado.setText(consultaArriendo[6]);
                fechaHoy.setText(String.valueOf(LocalDate.now()));
                fechaHoy.setText(String.valueOf(LocalDate.now()));

            }catch(Exception e ){
                JOptionPane.showMessageDialog(this,"El arriendo no existe ","Advertencia",JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display(){
        PagarArriendo dialog = new PagarArriendo();
        dialog.pack();
        dialog.setVisible(true);
    }
}

