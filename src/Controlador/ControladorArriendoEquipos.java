
package Controlador;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;


import java.io.*;
import java.time.LocalDate;
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

    public void creaImplemento(long codigo, String descripcion,long precioArriendoDia) throws EquipoException {//nuevo metodo
        if(buscaEquipo(codigo) != null){
            throw new EquipoException("Ya existe un equipo con el código dado");
        }

        todosEquipos.add(new Implemento(codigo, descripcion, precioArriendoDia));
        //en el tenor del enunciado, dice que una vez implementado el creaimplemento el metodo creaEquipo debe ser eliminado


    }

    public void creaConjunto(String cod, String desc) throws EquipoException {//nuevo metodo

        if(buscaEquipo(Long.parseLong(cod)) != null){
            throw new EquipoException("Ya existe un equipo con el código dado");
        }

        todosEquipos.add(new Conjunto(Long.parseLong(cod), desc));

    }

    public void creaEquipo (String cod, String desc, long precio) throws EquipoException {
        //si existe un equipo con el mismo codigo, arroja una excepcion
        if(buscaEquipo(Long.parseLong(cod)) != null){
            throw new EquipoException("Ya existe un equipo con el código dado");
        }

        todosEquipos.add(new Equipo(Long.parseLong(cod), desc, precio));
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

        Equipo[] equiposDelArriendo = arriendo.getEquipos();////nueva implementacion para realizar la relacione entre equipo y arriendo
        for (Equipo equipoActual: equiposDelArriendo) {
            if (equipoActual.getCodigo() == equipo.getCodigo()) {
                throw new ArriendoException("El arriendo ya tiene este equipo");
            }
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

    public void PagaArriendoContado(long codArriendo, long monto) throws ArriendoException {//nuevo metodo
        Arriendo arriendo = buscaArriendo(codArriendo);

        if(arriendo == null){
            throw new ArriendoException("El arriendo asignado no existe");
        }else if(arriendo.getEstado() == EstadoArriendo.DEVUELTO){
            throw new ArriendoException("El arriendo se encuentra devuelto");
        }else if((arriendo.getSaldoAdeudado() < monto)){
            throw new ArriendoException("El monto es mayor que el adeudado");
        }

        Pago pagoContado = new Contado(monto, LocalDate.now());
        arriendo.addPagoContado((Contado) pagoContado);
    }

    public void pagaArriendoDebito(long codArriendo, long monto, String codTransaccion, String numTarjeta) throws ArriendoException {//nuevo metodo
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        }
        if (arriendo.getEstado() == EstadoArriendo.DEVUELTO) {
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        }
        if (arriendo.getSaldoAdeudado() < monto) {
            throw new ArriendoException("Monto supera el saldo adeudado");
        }

        Debito pagoDebito = new Debito(monto, LocalDate.now(), codTransaccion, numTarjeta);
        arriendo.addPagoDebito(pagoDebito);
    }

    public void pagaArriendoCredito(long codArriendo, long monto, String codTransaccion, String numTarjeta, int nroCuotas) throws ArriendoException {//nuevo metodo
        Arriendo arriendo = buscaArriendo(codArriendo);

        if(arriendo == null){
            throw new ArriendoException("");
        }else if(arriendo.getEstado() == EstadoArriendo.DEVUELTO){
            throw new ArriendoException("");
        }else if((arriendo.getSaldoAdeudado() < monto)){
            throw new ArriendoException("");
        }

        Pago pagoContado = new Contado(monto, LocalDate.now());
        arriendo.addPagoContado((Contado) pagoContado);
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
        LocalDate fechaArriendo = arriendo.getFechaDevolucion();
        if(arriendo==null){
        return new String [0];
        }
        try {

            fechaArriendo.format(DateTimeFormatter.ofPattern("dd/MM/yyy"));
        }catch(Exception e){
            System.out.println("no existe la fecha indicada");
        }

        detallestoArriendo[0]=(Integer.toString(arriendo.getCodigo()));
        detallestoArriendo[1]= String.valueOf((fechaArriendo));

        LocalDate fechaDevolucion=arriendo.getFechaDevolucion();
        try {
            fechaDevolucion.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        }catch(Exception e){
            System.out.println("no existe la fecha indicada");
        }
        if(fechaDevolucion==null){            detallestoArriendo[2]="no devuelto";
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

    public String[] consultaArriendoAPagar(long codigo){//nuevo metodo

        Arriendo arriendo = buscaArriendo(codigo);
        if (arriendo == null || arriendo.getEstado() != EstadoArriendo.DEVUELTO) {
            return new String[0];
        }
        String[] resultado = new String[7];
        resultado[0] = codigo + "";//esto se usa para evitar hacer el calue off y no tener problemas
        resultado[1] = arriendo.getEstado().toString().toLowerCase();
        resultado[2] = arriendo.getCliente().getRut();
        resultado[3] = arriendo.getCliente().getNombre();
        resultado[4] = arriendo.getMontoTotal() + "";
        resultado[5] = arriendo.getMontoPagado() + "";
        resultado[6] = arriendo.getSaldoAdeudado() + "";

        return resultado;
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
        if(cliente == null){
            throw new ClienteException("No existe un cliente con el rut dado");
        }

        Arriendo[] arriendosPorDev = cliente.getArriendosPorDevolver();
        String[][] listaArriendos = new String[arriendosPorDev.length][7];
        String[] detalleArriendo;



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

    public void readDatosSistema() throws ArriendoException {///nuevo metodo
        ObjectInputStream reader;

        try {
            reader = new ObjectInputStream(new FileInputStream("Datos.obj"));
            instancia = (ControladorArriendoEquipos) reader.readObject();
            reader.close();
        } catch (IOException e) {
            throw new ArriendoException("No ha sido posible leer datos del sistema desde archivo");
        } catch (ClassNotFoundException e) {
            throw new ArriendoException("No ha sido posible leer datos del sistema desde archivo");
        }

    }


    public void saveDatosSistemas() throws ArriendoException {///nuevo metodo
        ObjectOutputStream escritor;
        try {
            escritor = new ObjectOutputStream(new FileOutputStream("Datos.obj"));
            escritor.writeObject(this);
            escritor.close();
        } catch (IOException e) {
            throw new ArriendoException("No ha sido posible leer datos del sistema desde archivo");
        }
    }


    public String[][] listaArriendosPagados(){///nuevo metodo
        String[][] detallesArriendosPagados;
        ArrayList<Integer> codArriendosPagados = new ArrayList<Integer>();

        for(Arriendo arriendo : todosArriendos){
            if(arriendo.getPagosToString().length == 0){
                codArriendosPagados.add(arriendo.getCodigo());
            }
        }

        detallesArriendosPagados = new String[codArriendosPagados.size()][];
        for(int i = 0; i < codArriendosPagados.size(); i++){
            detallesArriendosPagados[i] = consultaArriendo(codArriendosPagados.get(i));
        }

        return detallesArriendosPagados;
    }

    public String[][] listaPagosDeArriendo(long codArriendo) throws ArriendoException{///nuevo metodo
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe arriendo con el codigo dado");
        }
        if (arriendo.getEstado() != EstadoArriendo.DEVUELTO) {
            throw new ArriendoException("Arriendo no se encuentra habilitado para pagos");
        }

        return arriendo.getPagosToString();

    }




} //fin de la clase