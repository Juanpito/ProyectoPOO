package Modelo;

public class DetalleArriendo {
    private long precioAplicado;
    private Arriendo arriendo;
    private Equipo equipo;

    public DetalleArriendo(long precioAplicado,Equipo equipo,Arriendo arriendo){
        this.precioAplicado=precioAplicado;
        this.equipo=equipo;
        this.arriendo=arriendo;
        equipo.addDetalleArriendo(this);

    }


    public long getPrecioAplicado() {
        return precioAplicado;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Arriendo getArriendo() {
        return arriendo;
    }
}
