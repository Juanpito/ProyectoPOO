package Modelo;

import java.time.LocalDate;

public abstract class Pago {
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
