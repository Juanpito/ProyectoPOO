package Modelo;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Equipo implements Serializable {
    private long codigo;
    private String descripcion;

    private EstadoEquipo estado;
    private ArrayList<DetalleArriendo>detalleArriendos;
    private ArrayList<Conjunto>conjuntos;
    private ArrayList<Equipo>equipos;


    public Equipo(long codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estado=EstadoEquipo.OPERATIVO;
        this.detalleArriendos=new ArrayList<>();
        this.equipos=new ArrayList<>();
        this.conjuntos=new ArrayList<>();

    }

    public long getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
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
        if (detalleArriendos.isEmpty()){
            return false;
        }


        for (DetalleArriendo detalle:detalleArriendos){
            if (detalleArriendos.get(detalleArriendos.size()-1).getArriendo().getEstado()==EstadoArriendo.ENTREGADO){
                return true;
            }


        }

        return false;


    }


    public abstract long getPrecioArriendoDia();
    public void addEquipo (Equipo equipo){}
    public int getNroEquipos(){
        return 0;
    }





}
