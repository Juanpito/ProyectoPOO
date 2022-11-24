
package Controlador;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ControladorArriendoEquipos {

    private static ControladorArriendoEquipos instancia = null;
    private ArrayList<Cliente> todosClientes;
    private ArrayList<Equipo> todosEquipos;
    private ArrayList<Arriendo> todosArriendos;



    private long codArriendo;
    private ControladorArriendoEquipos() {
        todosEquipos = new ArrayList<>();
        todosClientes = new ArrayList<>();
        todosArriendos = new ArrayList<>();

    }


    public static ControladorArriendoEquipos getInstance() {
        if (instancia == null) {
            instancia = new ControladorArriendoEquipos();
        }
        return instancia;
    }

    public void creaCliente(String nom, String rut, String dir, String tel) throws ClienteException {
        if(buscaCliente(rut)!=null) {
            throw new ClienteException("Ya existe un cliente con el rut ingresado");
        }

        todosClientes.add(new Cliente(nom, rut, dir, tel));
    }

    public void creaEquipo (String desc,long cod, long precio) throws EquipoException {
        if(buscaEquipo(cod)!=null) {
            throw new EquipoException("Ya existe un equipo con el codigo ingresado");
        }


        todosEquipos.add(new Equipo(cod, desc, precio));

    }

    public long creaArriendo (String rutCliente) throws ClienteException {
        Cliente encuentraCliente = buscaCliente(rutCliente);
        if(encuentraCliente==null){
            throw new ClienteException("No existe el cliente con el rut ingreado");
        }else{
            if(!encuentraCliente.isActivo()){
                throw new ClienteException("El cliente esta inactivo");
            }
        }

        long posicion=todosArriendos.size();
        todosArriendos.add(new Arriendo(posicion,LocalDate.now(),encuentraCliente));
        return posicion;//se llama posicion el codigo, porque es la ultima posicion que ocupa en mi coleccion

    }

    public String agregaEquipoToArriendo(long codArriendo, long codEquipo) throws ArriendoException,EquipoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        Equipo equipo = buscaEquipo(codEquipo);
        if(arriendo==null){
            throw new ArriendoException("No existe un arriendo con el codigo ingresado");
        }else{
            if(arriendo.getEstado()!= EstadoArriendo.INICIADO){
                throw new ArriendoException("El arriendo no esta inicializado");
            }
        }

        if(equipo == null){
            throw new EquipoException("No existe un equipo con el código dado");
        }else if(equipo.isArrendado()){
            throw new EquipoException("El equipo se encuentra arrendado");
        }else if(equipo.getEstado() != EstadoEquipo.OPERATIVO){
            throw new EquipoException("El equipo no está operativo");
        }




        arriendo.addDetalleArriendo(equipo);
        return equipo.getDescripcion();
    }

    public long cierraArriendo (long codArriendo) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if(arriendo==null){
            throw new ArriendoException("No existe un arriendo con el codigo dado");
        }
        if(arriendo.getEquipos().length==0){
            throw new ArriendoException("El arriedno no tiene equipos asignados");
        }
        arriendo.setEstado(EstadoArriendo.ENTREGADO);
        return arriendo.getMontoTotal();

    }

    public void devuelveEquipos (long codArriendo, EstadoEquipo[] estadoEquipos) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        boolean posicion;
        int i = 0;

        if(arriendo==null){
            throw new ArriendoException("No existe arriendo con el codigo ingresado");
        }else{
            if(arriendo.getEstado()!=EstadoArriendo.ENTREGADO){
                throw new ArriendoException("El arriendo no tiene devoluciones pendientes");
            }
        }


        for(Equipo equipo : arriendo.getEquipos()){
            posicion = true;

            for(int j = 0; j < todosEquipos.size() && posicion; j++){
                if(equipo.getCodigo() == todosEquipos.get(i).getCodigo()){
                    todosEquipos.get(i).setEstado(estadoEquipos[i]);
                }
            }

            i++;
        }

        todosArriendos.get((int) codArriendo).setEstado(EstadoArriendo.DEVUELTO);
        todosArriendos.get((int) codArriendo).setFechaDevolucion(LocalDate.now());





    }


    public void cambiaEstadoCliente(String rutCliente) throws ClienteException {
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
        Cliente cliente=buscaCliente(rut);
        String[]consultarCliente=new String[6];
        int cont=0;
        if(cliente==null){
            return new String[0];
        }

        consultarCliente[0] = rut;
        consultarCliente[1] = (cliente.getNombre());
        consultarCliente[2] = (cliente.getDireccion());
        consultarCliente[3] = (cliente.getTelefono());
        consultarCliente[4] = (cliente.isActivo())? "Activo" : "Inactivo";//Este metodo me lo enseño un amigo

        for(Arriendo arriendo:todosArriendos){
            if(arriendo.getCliente().getRut()==cliente.getRut()){
                cont++;//aqui tenemos nuestro contador, por cada vez que que el arriendo se encuentra con nuestro cliente
            }
        }
        consultarCliente[5]=String.valueOf((cont-cliente.getArriendosPorDevolver().length));
        //aqui esta la ultima parte de este metodo, donde restamos todo aquello por devolver
        return consultarCliente;


    }

    public String[] consultaEquipo(long codigo){
    Equipo equipo=buscaEquipo(codigo);

        if(equipo==null){
            return new String[0];
        }
        String[]equiposToString=new String[5];
        equiposToString[0]=Long.toString(codigo);
        equiposToString[1]=equipo.getDescripcion();
        equiposToString[2]=Long.toString(equipo.getPrecioArriendoDia());
        switch(equipo.getEstado()){
            case OPERATIVO->equiposToString[3]="equipo operativo";
            case EN_REPARACION -> equiposToString[3]="En reparacion";
            case DADO_DE_BAJA -> equiposToString[3]="Dado de baja";
        }

        if(equipo.isArrendado()){//recordar que este metodo del arriendo retorna Verdadero o falso segun
            //sea el caso
            equiposToString[4]="Arrendado";
        }else {
            equiposToString[4]="disponible";
        }

        return equiposToString;




    }

    public String[] consultaArriendo (long codigo){
        Arriendo arriendo=buscaArriendo(codigo);
        String[]detallestoArriendo=new String[7];
        Cliente cliente=arriendo.getCliente();

        if(arriendo==null){
        return new String [0];
        }
        LocalDate fechaArriendo=arriendo.getFechaDevolucion();
        fechaArriendo.format(DateTimeFormatter.ofPattern("dd/MM/yyy"));


        detallestoArriendo[0]=(Integer.toString(arriendo.getCodigo()));
        detallestoArriendo[1]= String.valueOf((fechaArriendo));

        LocalDate fechaDevolucion=arriendo.getFechaDevolucion();
        fechaDevolucion.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        if(fechaDevolucion==null){
            detallestoArriendo[2]="no devuelto";
        }else{
            detallestoArriendo[2]= String.valueOf(fechaDevolucion);
        }

        switch(arriendo.getEstado()){
            case INICIADO -> detallestoArriendo[3]=("iniciado");
            case ENTREGADO -> detallestoArriendo[3]=("entregado");
            case DEVUELTO -> detallestoArriendo[3]=("devuelto");
        }

        detallestoArriendo[4]=(cliente.getRut());
        detallestoArriendo[5]=(cliente.getNombre());
        detallestoArriendo[6]=(Long.toString(arriendo.getMontoTotal()));
        //Aqui terminamos con la clase, al igual que la anterior con la misma logica
        return detallestoArriendo;
    }


    public String[][] listaClientes() {
        String clientesArr[][] = new String[todosClientes.size()][6];
        int i = 0;
        for (Cliente cliente : todosClientes) {
            clientesArr[i][0] = cliente.getRut();
            clientesArr[i][1] = cliente.getNombre();
            clientesArr[i][2] = cliente.getDireccion();
            clientesArr[i][3] = cliente.getTelefono();

            if (todosClientes.get(i).isActivo()) {
                clientesArr[i][4] = "Activo";
            } else {
                clientesArr[i][4] = "Inactivo";
            }

            clientesArr[i][5]=String.valueOf(todosClientes.get(i).getArriendosPorDevolver().length);
            //esta es la parte en la que le retorna los arriendos que tiene por devolver
            i++;
        }
        return clientesArr;
    }

    public String[][] listaArriendos(LocalDate fechaInicioPeriodo, LocalDate fechaFinPeriodo){
        String arriendosArr[][] = new String[todosEquipos.size()][6];
        int i=0;
        for (Arriendo arriendo : todosArriendos) {//en el siguiente metodo implementamos la respectiva comparacion entre fechas en la iteracion
            if((arriendo.getFechaInicio().isAfter(fechaInicioPeriodo) && arriendo.getFechaInicio().isBefore(fechaFinPeriodo))) {
                arriendosArr[i][1] = (arriendo.getFechaInicio()).format(DateTimeFormatter.ofPattern("dd/MM/yy"));
                LocalDate fechaDevolucion = arriendo.getFechaDevolucion();
                if (fechaDevolucion == null) {
                    arriendosArr[i][2] = "no devuelto";
                } else {
                    arriendosArr[i][2] = (fechaDevolucion).format(DateTimeFormatter.ofPattern("dd/MM/yy"));
                }
                arriendosArr[i][3] = arriendo.getEstado().toString();
                arriendosArr[i][4] = arriendo.getCliente().getRut();
                arriendosArr[i][5] = Long.toString(arriendo.getMontoTotal());

                i++;

            }
        }

        return arriendosArr;
    }


    public String [][]listaArriendosPorDevolver(String rutCliente) throws ClienteException {//este metodo
        Cliente cliente = buscaCliente(rutCliente);
        Arriendo[] arriendosPorDev = cliente.getArriendosPorDevolver();
        String[][] listaArriendos = new String[arriendosPorDev.length][7];
        String[] detalleArriendo;

        if(cliente == null){
            throw new ClienteException("No existe un cliente con el rut dado");
        }


        for(int i = 0; i < arriendosPorDev.length; i++){
            detalleArriendo = consultaArriendo(arriendosPorDev[i].getCodigo());

            for(int j = 0; j < detalleArriendo.length; j++){
                listaArriendos[i][j] = detalleArriendo[j];
            }
        }

        return listaArriendos;





    }


    public String[][] listaDetallesArriendo(long codArriendo){
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
            if(equipo.getCodigo()==(codigo)){
                return equipo;
            }
        }
        return null;
    }

    private Arriendo buscaArriendo (long codigo){
        for (Arriendo arriendo:todosArriendos){
            if(arriendo.getCodigo()==(codigo)){
                return arriendo;
            }
        }
        return null;
    }

    public String[][] listaEquipos() {
        String equipoArr[][] = new String[todosEquipos.size()][5];
        int i=0;
        for (Equipo equipo : todosEquipos){
            equipoArr[i][0] = String.valueOf(equipo.getCodigo());
            equipoArr[i][1] = equipo.getDescripcion();
            equipoArr[i][2] = String.valueOf(equipo.getPrecioArriendoDia());

            switch(equipo.getEstado()) {
                case EN_REPARACION -> equipoArr[i][3]="en reparacion";
                case DADO_DE_BAJA -> equipoArr[i][3]="dado de baja";
                case OPERATIVO -> equipoArr[i][3]="operativo";
            }
            if(equipo.isArrendado()){
                equipoArr[i][4]="arrendado";
            }else{
                equipoArr[i][4]="Disponible";
            }

            i++;

        }
        return equipoArr;
    }
} //fin de la clase