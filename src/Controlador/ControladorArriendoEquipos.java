package Controlador;

import Modelo.*;
import java.time.LocalDate;
import java.util.ArrayList;
import Excepciones.*;
import Vista.*;

public class ControladorArriendoEquipos {

    private static ControladorArriendoEquipos instancia = null; //hola
    private final ArrayList<Cliente> todosClientes;
    private final ArrayList<Equipo> todosEquipos;
    private final ArrayList<Arriendo> todosArriendos;
    private final ArrayList<EstadoEquipo> estadoEquipos;


    private long codArriendo;
    private ControladorArriendoEquipos() {
        todosEquipos = new ArrayList<>();
        todosClientes = new ArrayList<>();
        todosArriendos = new ArrayList<>();
        estadoEquipos = new ArrayList<>();
    }


    public static ControladorArriendoEquipos getInstance() {
        if (instancia == null) {
            instancia = new ControladorArriendoEquipos();
        }
        return instancia;
    }

    public void creaCliente(String nom, String rut, String dir, String tel) {
        todosClientes.add(new Cliente(nom, rut, dir, tel));
    }

    public void creaEquipo (String desc,long cod, long precio) {
        todosEquipos.add(new Equipo(cod, desc, precio));
    }

    public long creaArriendo (long cod, LocalDate fecha, Cliente cliente){
        long posicion=todosArriendos.size()+1;//
        LocalDate hoy=LocalDate.now();
        todosArriendos.add(new Arriendo(cod, hoy, cliente));

        return posicion;

    }

    public String agregaEquipoToArriendo(long codArriendo, long codEquipo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        Equipo equipo = buscaEquipo(codEquipo);
        todosArriendos.get((int)codArriendo).addDetalleArriendo(equipo);
        return equipo.getDescripcion();
    }//por favor revisar este bloque↑

    public long cierraArriendo (long codArriendo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        return arriendo.getMontoTotal();

    }

    public void devuelveEquipos (long codArriendo, EstadoEquipo[] estadoEquipos){
        Arriendo arriendo = buscaArriendo(codArriendo);
        int contador = 0;


        for(Equipo equipo : arriendo.getEquipos()){
                if(equipo.getCodigo() == equipo.getCodigo()){
                    equipo.setEstado(estadoEquipos[contador]);

                }


            contador++;
        }
    }

    public void cambiaEstadoCliente(String rutCliente){
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
         
        }
        if (cliente.isActivo()) {
            cliente.setInactivo();
        }
        else{
            cliente.setActivo();
        }
    }

    public String[] consultaCliente(String rut){
        String clienteRut[];


        if(todosClientes ==  null){
            clienteRut=new String[0];
            return clienteRut;
        }
        clienteRut=new String[todosClientes.size()];
        for (Cliente cliente : todosClientes){
            if(rut.equals(cliente.getRut())){
                clienteRut[0]=cliente.getRut();
                clienteRut[1]=cliente.getRut();
                clienteRut[2]=cliente.getRut();
                clienteRut[3]=cliente.getRut();
                clienteRut[4]=cliente.getRut();

            }
        }
        return clienteRut;
    }

    public String[] consultaEquipo(long codigo){
        String[] equipoCodigo;

        if(todosEquipos== null){
            equipoCodigo=new String[0];
            return equipoCodigo;
        }
        equipoCodigo=new String[todosEquipos.size()];
        for (Equipo equipo : todosEquipos){
            if(codigo==(equipo.getCodigo())){
                equipoCodigo[0]= String.valueOf(equipo.getCodigo());
                equipoCodigo[1]= String.valueOf(equipo.getCodigo());
                equipoCodigo[2]= String.valueOf(equipo.getCodigo());
                equipoCodigo[3]= String.valueOf(equipo.getCodigo());
                equipoCodigo[4]= String.valueOf(equipo.getCodigo());

            }
        }
        return equipoCodigo;
    }

    public String[] consultaArriendo (long codigo){

        String equipoArriendo[];
        if (todosArriendos ==null){
            equipoArriendo =new String[0];
            return equipoArriendo ;
        }
       equipoArriendo =new String[todosArriendos.size()];

        for (Arriendo arriendo : todosArriendos){
            if(codigo==(arriendo.getCodigo())){
                equipoArriendo[0]= String.valueOf(arriendo.getCodigo());
                equipoArriendo[1]= String.valueOf(arriendo.getCodigo());
                equipoArriendo[2]= String.valueOf(arriendo.getCodigo());
                equipoArriendo[3]= String.valueOf(arriendo.getCodigo());
                equipoArriendo[4]= String.valueOf(arriendo.getCodigo());

            }
        }
        return equipoArriendo;
    }


    public String[][] listaClientes() {
        String clientesArr[][] = new String[todosClientes.size()][5];
        if(todosClientes==null){
            return clientesArr;
        }

        int i = 0;
        for (Cliente cliente : todosClientes) {
            clientesArr[i][0] = cliente.getRut();
            clientesArr[i][1] = cliente.getNombre();
            clientesArr[i][2] = cliente.getDireccion();
            clientesArr[i][3] = cliente.getTelefono();
            clientesArr[i][4] = String.valueOf(cliente.isActivo());
            i++;
        }
        return clientesArr;
    }

    public String[][] listaArriendos(){
        if(this.todosArriendos==null){}
        String arriendosArr[][] = new String[todosArriendos.size()][5];
        int i=0;
        for (Arriendo arriendo : todosArriendos){
            arriendosArr[i][0] = String.valueOf(arriendo.getCodigo());
            arriendosArr[i][1] = String.valueOf(arriendo.getFechaInicio());
            arriendosArr[i][2] = String.valueOf(arriendo.getFechaDevolucion());
            arriendosArr[i][3] = String.valueOf(arriendo.getEstado());
            arriendosArr[i][4] = arriendo.getCliente().getRut();
            arriendosArr[i][5] = String.valueOf(arriendo.getMontoTotal());
            i++;
        }
        return arriendosArr;
    }

    public String[][] listaDetallesArriendo(long codArriendo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            return new String[0][0];
        }
        return arriendo.getDetallesToString();

    }

    public Cliente buscaCliente(String rut){
        for (Cliente cliente:todosClientes){
            if(cliente.getRut().equals(rut)){
                return cliente;
            }
        }
        return null;
    }

    public Equipo buscaEquipo(long codigo){
        for (Equipo equipo:todosEquipos){
            if(equipo.getCodigo()==(codigo)){
                return equipo;
            }
        }
        return null;
    }

    private Arriendo buscaArriendo (long codigo){
        for (Arriendo arriendo:todosArriendos){
            if(arriendo.getCodigo()==((int)codigo)){
                return arriendo;
            }
        }
        return null;
    }

    public String[][] listaEquipos() {
        String equipoArr[][] = new String[todosEquipos.size()][4];
        int i=0;
        for (Equipo equipo : todosEquipos){
            equipoArr[i][0] = String.valueOf(equipo.getCodigo());
            equipoArr[i][1] = equipo.getDescripcion();
            equipoArr[i][2] = String.valueOf(equipo.getPrecioArriendoDia());
            equipoArr[i][3] = String.valueOf(equipo.getEstado());
            i++;
        }
        return equipoArr;
    }
} //fin de la clase
