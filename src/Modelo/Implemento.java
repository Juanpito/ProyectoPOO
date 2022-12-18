package Modelo;

public class Implemento extends Equipo{
    private long precioArriendoDia;

    public Implemento(long codigo, String descripcion, long precioArriendoDia) {
        super(codigo, descripcion);
        this.precioArriendoDia = precioArriendoDia;
    }

    @Override
    public long getPrecioArriendoDia() {
        return precioArriendoDia;
    }
}
