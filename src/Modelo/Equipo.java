package Modelo;

public class Equipo {
    private long codigo;
    private String descripcion;
    private long precioArriendoDia;
    private EstadoEquipo estado=EstadoEquipo.OPERATIVO;

    public Equipo(long codigo, String descripcion, long precioArriendoDia) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioArriendoDia = precioArriendoDia;
    }

    public long getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getPrecioArriendoDia() {
        return precioArriendoDia;
    }


    public EstadoEquipo getEstado() {
        return estado;
    }
}
