package Modelo;

public class Cliente {
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;
    private boolean activo=true;

    public Cliente(String rut, String nombre, String direccion, String telefono) {
        this.rut = rut;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;

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
        return activo;
    }

    public void setActivo() {
        this.activo = true;
    }

    public void setInactivo(){
        this.activo=false;
    }



}
