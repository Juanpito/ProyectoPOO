package Modelo;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Pago implements Serializable {
    private long monto;
    private LocalDate fecha;

    public Pago(long monto, LocalDate fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }
    public long getMonto(){
        return monto;
    }

    public LocalDate getFecha(){
        return fecha;
    }




}
