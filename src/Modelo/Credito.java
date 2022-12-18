package Modelo;

import java.time.LocalDate;

public class Credito extends Pago {
    private String codTransaccion;
    private String numTarjeta;
    private int nroCuotas;

    public Credito(long monto, LocalDate fecha, String codTransaccion, String numTarjeta, int nroCuotas) {
        super(monto, fecha);
        this.codTransaccion = codTransaccion;
        this.numTarjeta = numTarjeta;
        this.nroCuotas = nroCuotas;
    }

    public String getCodTransaccion() {
        return codTransaccion;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public int getNroCuotas() {
        return nroCuotas;
    }
}
