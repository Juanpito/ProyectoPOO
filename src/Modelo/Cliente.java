package Modelo;

import java.util.ArrayList;

public class Cliente {
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;
    private boolean activo;
    private ArrayList<Arriendo>arriendos;
    private ArrayList<Cliente>todosClientes;

    public Cliente(String rut, String nombre, String direccion, String telefono) {
        this.rut = rut;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo=true;
        this.arriendos=new ArrayList<>();

    }

    public String getRut() {
        return rut;
    }


    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }


    public String getTelefono() {
        return telefono;
    }


    public boolean isActivo(){
        if (this.activo==true){
            return true;
        }
        else return false;
    }

    public void setActivo() {
        this.activo = true;
    }




    public void setInactivo(){
        this.activo=false;
    }

    public void addArriendo(Arriendo arriendo){
        arriendos.add(arriendo);
    }

    public Arriendo[]getArriendosPorDevolver(){
        ArrayList<Arriendo>estosArriendos=new ArrayList<>();//en este metodo todo aquello que es entregado, es lo que falta por devolver
        for(Arriendo arriendo:arriendos){
            if (arriendo.getEstado()==EstadoArriendo.ENTREGADO){
                estosArriendos.add(arriendo);
            }

        }
        return estosArriendos.toArray(new Arriendo[0]);
    }




}
