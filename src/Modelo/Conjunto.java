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
        return equipos.stream().map(equipo -> equipo.getPrecioArriendoDia()).reduce(0L, Long::sum);
    }

    public void addEquipo(Equipo equipo){
       super.addEquipo(equipo);

    }

    public int getNroEquipos(){
        return super.getNroEquipos();
    }




}
