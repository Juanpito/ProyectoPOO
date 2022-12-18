package Modelo;

import java.util.ArrayList;

public class Conjunto extends Equipo{
    private ArrayList<Equipo> equipos;

    public Conjunto(long codigo, String descripcion) {
        super(codigo, descripcion);
        this.equipos=new ArrayList<>();
    }

    @Override
    public long getPrecioArriendoDia() {
        long i=0;
        for(Equipo equipo:equipos){
            i=i+equipo.getPrecioArriendoDia();
        }
        return i;
    }

    public void addEquipo(Equipo equipo){
       super.addEquipo(equipo);

    }

    public int getNroEquipos(){
        return super.getNroEquipos();
    }




}
