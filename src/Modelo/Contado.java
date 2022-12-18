package Modelo;

import java.time.LocalDate;

public class Contado extends Pago{
    public Contado(long monto, LocalDate fecha) {
        super(monto, fecha);
    }
}
