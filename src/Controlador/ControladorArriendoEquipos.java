package Controlador;

import Modelo.Cliente;
import Modelo.Equipo;

import java.time.LocalDate;
import java.util.ArrayList;

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

    public long creaArriendo (String rutCliente){
        long posicion=todosArriendos.size()+1;
        LocalDate hoy=LocalDate.now();
        Cliente EncuentraCliente = buscaCliente(rutCliente);
        todosArriendos.add(new Arriendo(posicion, hoy));
        return posicion;

    }

    public String agregaEquipoToArriendo(long codArriendo, long codEquipo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        Equipo equipo = buscaEquipo(codEquipo);

        todosArriendos.get((int)codArriendo).addDetalleArriendo(equipo);
        return equipo.getDescripcion();
    }

    public long cierraArriendo (long codArriendo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        return arriendo.getMontoTotal();

    }

    public void devuelveEquipos (long codArriendo, EstadoEquipo[] estadoEquipos){
        Arriendo arriendo = buscaArriendo(codArriendo);
        int contador = 0;


        for(Equipo equipo : arriendo.getEquipos()){
            for(int i = 0; i < equipos.size() && flag; i++){
                if(equipo.getCodigo() == equipos.get(i).getCodigo()){
                    equipos.get(i).setEstado(estadoEquipos[contador]);
                    return true;
                }
            }

            contador++;
        }
    }

    public void cambiaEstadoCliente(String rutCliente){
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
            throw new ClienteException("Error: el rut no se asocia con ningun cliente");
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

        if(null){
            return cliente[0];
        }

        for (Cliente cliente : clientesArr){
            if(rut.equals(cliente.getRut)){
                clienteRut[0]=cliente.getRut;
                clienteRut[1]=cliente.getRut;
                clienteRut[2]=cliente.getRut;
                clienteRut[3]=cliente.getRut;
                clienteRut[4]=cliente.getRut;
                return clienteRut[];
            }
        }
    }

    public String[] consultaEquipo(long codigo){
        long equipoCodigo[];

        if(null){
            return equipo[0];
        }

        for (Equipo equipo : equiposArr){
            if(codigo.equals(equipo.getCodigo)){
                equipoCodigo[0]=equipo.getCodigo;
                equipoCodigo[1]=equipo.getCodigo;
                equipoCodigo[2]=equipo.getCodigo;
                equipoCodigo[3]=equipo.getCodigo;
                equipoCodigo[4]=equipo.getCodigo;
                return equipoCodigo[];
            }
        }

    }

    public String[] consultaArriendo (long codigo){

        if(null){
            return arriendo[0];
        }
        long equipoArriendo[];

        for (Equipo equipo : equiposArr){
            if(codigo.equals(equipo.getCodigo)){
                equipoCodigo[0]=equipo.getCodigo;
                equipoCodigo[1]=equipo.getCodigo;
                equipoCodigo[2]=equipo.getCodigo;
                equipoCodigo[3]=equipo.getCodigo;
                equipoCodigo[4]=equipo.getCodigo;
                return equipoCodigo[];
            }
        }
    }


    public String[][] listaClientes() {
        String clientesArr[][] = new String[todosClientes.size()][5];
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
        String arriendosArr[][] = new String[todosEquipos.size()][4];
        int i=0;
        for (Arriendo arriendo : todosArriendos){
            arriendosArr[i][0] = unArriendo();
            i++;
        }
    }

    String[][] listaDetallesArriendo(long codArriendo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            return new String[0][0];
        }
        return arriendo.getDetallesToString();

    }

    private Cliente buscaCliente(String rut){
        for (Cliente cliente:todosClientes){
            if(cliente.getRut().equals(rut)){
                return cliente;
            }
        }
        return null;
    }

    private Equipo buscaEquipo(long codigo){
        for (Equipo equipo:todosEquipos){
            if(equipo.getCodigo().equals(codigo)){
                return equipo;
            }
        }
        return null;
    }

    private Arriendo buscaArriendo (long codigo){
        for (Arriendo Arriendo:todosArriendos){
            if(arriendo.getCodigo().equals(codigo)){
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