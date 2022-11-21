package Modelo;

import java.util.ArrayList;

public class Equipo {
    private long codigo;
    private String descripcion;
    private long precioArriendoDia;
    private EstadoEquipo estado;
    ArrayList<DetalleArriendo>detalleArriendos;

    public Equipo(long codigo, String descripcion, long precioArriendoDia) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioArriendoDia = precioArriendoDia;
        this.estado=EstadoEquipo.OPERATIVO;
        detalleArriendos=new ArrayList<>();
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

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public void addDetalleArriendo(DetalleArriendo detalle){
        detalleArriendos.add(detalle);
    }


    //en este caso para saber si un equipo es arrendado o no, necesitasmos saberlo desde la clase arriendo, por tanto usamos DetalleArriendo como puente (entregado o no) para obtener la informacion
    public boolean isArrendado(){
        for (DetalleArriendo detalle:detalleArriendos){
            if (detalle.getArriendo().getEstado()== EstadoArriendo.ENTREGADO){
                return true;
            }


        }

        return false;


    }




}
