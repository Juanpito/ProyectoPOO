
package Controlador;

import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ControladorArriendoEquipos implements Serializable {
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Equipo> equipos;
    private final ArrayList<Arriendo> arriendos;

    private static ControladorArriendoEquipos instance = null;

    private ControladorArriendoEquipos() {
        clientes = new ArrayList<>();
        equipos = new ArrayList<>();
        arriendos = new ArrayList<>();
    }

        public static ControladorArriendoEquipos getInstance() {
            if (instance == null) {
                instance = new ControladorArriendoEquipos();
            }
            return instance;
        }

    public void creaCliente(String rut, String nom, String dir, String tel) throws ClienteException {
        //si existe un cliente con el mismo rut, arroja una excepcion
        if(buscaCliente(rut) != null) {
            throw new ClienteException("Ya existe un cliente con el rut dado");
        }

        clientes.add(new Cliente(rut, nom, tel, dir));
    }

    //Actualizacion de creaEquipo
    public void creaImplemento(long codigo, String descripcion, long precioArriendoDia) throws EquipoException {

        if(buscaEquipo(codigo) != null){
            throw new EquipoException("Ya existe un equipo con el código dado");
        }

        equipos.add(new Implemento(codigo, descripcion, precioArriendoDia));
    }

    public void creaConjunto(long cod, String desc, long[] codEquipos) throws EquipoException {
        //si existe un equipo con el mismo codigo, arroja una excepcion
        if(buscaEquipo(cod) != null){
            throw new EquipoException("Ya existe un equipo con el código dado");
        }

        ArrayList<Equipo> elementosComponentes = new ArrayList<>();
        for (long codigo: codEquipos) {
            Equipo componente = buscaEquipo(codigo);
            if (componente == null) {
                throw new EquipoException("Uno de los componentes indicados no existe");
            }
            elementosComponentes.add(componente);
        }

        Conjunto conjunto = new Conjunto(cod, desc);
        equipos.add(conjunto);
        elementosComponentes.forEach(conjunto::addEquipo);
    }

    public long creaArriendo(String rutCliente) throws ClienteException {
        Cliente cliente = buscaCliente(rutCliente);

        //si el cliente no existe o el cliente no esta activo arroja una excepcion

        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        } else if (!cliente.isActivo()) {
            throw new ClienteException("El cliente etá inactivo");
        }

        long nuevoCodigo = arriendos.size();
        arriendos.add(new Arriendo(nuevoCodigo, LocalDate.now(), cliente));

        return nuevoCodigo;
    }

    // Fabian
    public String agregaEquipoToArriendo(long codArriendo, long codEquipo) throws EquipoException, ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        Equipo equipo = buscaEquipo(codEquipo);

        if(arriendo == null){
            throw new ArriendoException("No existe un arriendo con el código dado");
        }else if(arriendo.getEstado() != EstadoArriendo.INICIADO){
            throw new ArriendoException("El arriendo no está iniciado");
        }

        if(equipo == null){
            throw new EquipoException("No existe un equipo con el código dado");
        }else if(equipo.isArrendado()){
            throw new EquipoException("El equipo se encuentra arrendado");
        }else if(equipo.getEstado() != EstadoEquipo.OPERATIVO){
            throw new EquipoException("El equipo no está operativo");
        }
        Equipo[] equiposDelArriendo = arriendo.getEquipos();
        for (Equipo equipoActual: equiposDelArriendo) {
            if (equipoActual.getCodigo() == equipo.getCodigo()) {
                throw new ArriendoException("El arriendo ya tiene este equipo");
            }
        }
        arriendo.addDetalleArriendo(equipo);
        return equipo.getDescripcion();
    }

    // Gonzalo
    public long cierraArriendo(long codArriendo) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe arriendo con el codigo dado");
        }
        if (arriendo.getEquipos().length == 0) {
            throw new ArriendoException("El arriendo no tiene equipos asociados");
        }
        arriendo.setEstado(EstadoArriendo.ENTREGADO);

        return arriendo.getMontoTotal();
    }

    // Fabian
    public void devuelveEquipos(long codArriendo, EstadoEquipo[] estadoEquipos) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        int contador = 0;

        if(arriendo == null){
            throw new ArriendoException("No existe arriendo con el codigo dado");
        }else if(arriendo.getEstado() != EstadoArriendo.ENTREGADO){
            throw new ArriendoException("El arriendo no tiene devolucion pendiente");
        }

        // XD? no tengo ni idea que haces acá pero no quiero tocarlo
        // de todas formas creo que puedes eliminar el if y hacer solo un for
        for(Equipo equipo : arriendo.getEquipos()){
            for (Equipo value : equipos) {
                if (equipo.getCodigo() == value.getCodigo()) {
                    value.setEstado(estadoEquipos[contador]);
                }
            }
            contador++;
        }

        arriendos.get((int) codArriendo).setEstado(EstadoArriendo.DEVUELTO);
        arriendos.get((int) codArriendo).setFechaDevolucion(LocalDate.now());
    }

    public void pagaArriendoContado(long codArriendo, long monto) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);

        if(arriendo == null){
            throw new ArriendoException("No existe un arriendo con el código dado");
        }else if(arriendo.getEstado() != EstadoArriendo.DEVUELTO){
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        }else if((arriendo.getSaldoAdeudado() < monto)){
            throw new ArriendoException("Monto supera el saldo adeudado");
        }

        Contado pagoContado = new Contado(monto, LocalDate.now());
        arriendo.addPagoContado(pagoContado);
    }

    public void pagaArriendoDebito(long codArriendo, long monto, String codTransaccion, String numTarjeta) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        }
        if (arriendo.getEstado() != EstadoArriendo.DEVUELTO) {
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        }
        if (arriendo.getSaldoAdeudado() < monto) {
            throw new ArriendoException("Monto supera el saldo adeudado");
        }

        Debito pagoDebito = new Debito(monto, LocalDate.now(), codTransaccion, numTarjeta);
        arriendo.addPagoDebito(pagoDebito);
    }

    public void pagaArriendoCredito(long codArriendo, long monto, String codTransaccion, String numTarjeta, int nroCuotas) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);

        if(arriendo == null){
            throw new ArriendoException("No existe un arriendo con el código dado");
        }else if(arriendo.getEstado() != EstadoArriendo.DEVUELTO){
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        }else if((arriendo.getSaldoAdeudado() < monto)){
            throw new ArriendoException("Monto supera el saldo adeudado");
        }

        Credito pagoCredito = new Credito(monto, LocalDate.now(), codTransaccion, numTarjeta, nroCuotas);
        arriendo.addPagoCredito(pagoCredito);
    }

    // Gonzalo
    public void cambiaEstadoCliente(String rutCliente) throws ClienteException {
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        }
        if (cliente.isActivo()) {
            cliente.setInactivo();
        } else {
            cliente.setActivo();
        }
    }

    // Fabian
    public String[] consultaCliente(String rut){
        Cliente cliente = buscaCliente(rut);
        String[] detalleCliente = new String[6];
        int contadorArriendos = 0;

        if(cliente == null){
            return new String[0];
        }

        detalleCliente[0] = rut;
        detalleCliente[1] = (cliente.getNombre());
        detalleCliente[2] = (cliente.getDireccion());
        detalleCliente[3] = (cliente.getTelefono());
        detalleCliente[4] = (cliente.isActivo())? "Activo" : "Inactivo";

        // ??????????? WTF
        for(Arriendo arriendo : arriendos){
            if(arriendo.getCliente().getRut().equals(cliente.getRut())){
                contadorArriendos++;
            }
        }
        detalleCliente[5] = String.valueOf((contadorArriendos - cliente.getArriendosPorDevolver().length));

        return detalleCliente;
    }

    // Gonzalo
    public String[] consultaEquipo(long codigo){
        Equipo equipo = buscaEquipo(codigo);
        if (equipo == null) {
            return new String[0];
        }
        String[] equipoString = new String[5];

        equipoString[0] = Long.toString(codigo);
        equipoString[1] = equipo.getDescripcion();
        equipoString[2] = Long.toString(equipo.getPrecioArriendoDia());
        switch (equipo.getEstado()) {
            case OPERATIVO -> equipoString[3] = "Operativo";
            case EN_REPARACION -> equipoString[3] = "En reparacion";
            case DADO_DE_BAJA -> equipoString[3] = "Dado de baja";
        }
        if (equipo.isArrendado()) {
            equipoString[4] = "Arrendado";
        } else {
            equipoString[4] = "Disponible";
        }

        return equipoString;
    }

    // Fabian
    public String[] consultaArriendo(long codigo){
        Arriendo arriendo = buscaArriendo(codigo);
        String[] detallesArriendo = new String[7];

        if(arriendo == null){
            return new String[0];
        }

        Cliente cliente = arriendo.getCliente();

        detallesArriendo[0] = (Integer.toString(arriendo.getCodigo()));
        detallesArriendo[1] = (formateaFecha(arriendo.getFechaInicio()));

        LocalDate fechaDevolucion = arriendo.getFechaDevolucion();
        detallesArriendo[2] =((arriendo.getFechaDevolucion() == null)? "No devuelto" : formateaFecha(fechaDevolucion));

        switch (arriendo.getEstado()){
            case INICIADO -> detallesArriendo[3] = ("iniciado");
            case ENTREGADO -> detallesArriendo[3] = ("entregado");
            case DEVUELTO -> detallesArriendo[3] = ("devuelto");
        }

        detallesArriendo[4] = (cliente.getRut());
        detallesArriendo[5] = (cliente.getNombre());
        detallesArriendo[6] = (Long.toString(arriendo.getMontoTotal()));

        return detallesArriendo;
    }

    public String[] consultaArriendoAPagar(long codigo){

        Arriendo arriendo = buscaArriendo(codigo);
        if (arriendo == null || arriendo.getEstado() != EstadoArriendo.DEVUELTO) {
            return new String[0];
        }
        String[] resultado = new String[7];
        resultado[0] = codigo + "";
        resultado[1] = arriendo.getEstado().toString().toLowerCase();
        resultado[2] = arriendo.getCliente().getRut();
        resultado[3] = arriendo.getCliente().getNombre();
        resultado[4] = arriendo.getMontoTotal() + "";
        resultado[5] = arriendo.getMontoPagado() + "";
        resultado[6] = arriendo.getSaldoAdeudado() + "";

        return resultado;
    }

    public String[][] listaClientes() {
        // Crear lista para devolver
        String[][] listClientes = new String[clientes.size()][6];

        for (int i=0; i<listClientes.length; i++) {
            // Agregar datos a la lista
            listClientes[i][0] = clientes.get(i).getRut();
            listClientes[i][1] = clientes.get(i).getNombre();
            listClientes[i][2] = clientes.get(i).getDireccion();
            listClientes[i][3] = clientes.get(i).getTelefono();

            // Pasar de boolean a String
            if (clientes.get(i).isActivo()) {
                listClientes[i][4] = "Activo";
            } else {
                listClientes[i][4] = "Inactivo";
            }

            listClientes[i][5] = String.valueOf(clientes.get(i).getArriendosPorDevolver().length);
        }

        return listClientes;
    }

    public String[][] listaArriendos(LocalDate fechaInicioPeriodo, LocalDate fechaFinPeriodo){
        ArrayList<String[]> listaArriendos = new ArrayList<>();

        for (int i=0; i<arriendos.size(); i++) {
            Arriendo arriendo = arriendos.get(i);
             if (arriendo.getFechaInicio().isAfter(fechaInicioPeriodo) && arriendo.getFechaInicio().isBefore(fechaFinPeriodo)||arriendo.getFechaInicio().equals(arriendo.getFechaDevolucion())){
                String[] arriendoString = new String[6];
                arriendoString[0] = Integer.toString(arriendo.getCodigo());
                arriendoString[1] = formateaFecha(arriendo.getFechaInicio());
                LocalDate fechaDevolucion = arriendo.getFechaDevolucion();
                if (fechaDevolucion == null) {
                    arriendoString[2] = "No devuelto";
                } else {
                    arriendoString[2] = formateaFecha(fechaDevolucion);
                }
                arriendoString[3] = arriendo.getEstado().toString().toLowerCase();
                arriendoString[4] = arriendo.getCliente().getRut();
                arriendoString[5] = Long.toString(arriendo.getMontoTotal());

                listaArriendos.add(arriendoString);
            }
        }
        return listaArriendos.toArray(new String[0][0]);
    }

    // Fabian
    public String[][] listaArriendosPorDevolver(String rutCliente) throws ClienteException {
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

    // Gonzalo
    public String[][] listaDetallesArriendo(long codArriendo){
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            return new String[0][0];
        }
        return arriendo.getDetallesToString();
    }

    public String[][] listaEquipos() {
        int cantEquipos = equipos.size();
        Equipo equipo;
        String[][] listaE = new String[cantEquipos][5];
        //Declaraciones

        for(int i = 0; i < cantEquipos; i++){
            //consiguiendo equipo de la lista
            equipo = equipos.get(i);

            //Guardado de equipo en arreglo
            listaE[i][0] = Long.toString(equipo.getCodigo());
            listaE[i][1] = equipo.getDescripcion();
            listaE[i][2] = String.format("%,d", equipo.getPrecioArriendoDia());

            switch (equipo.getEstado()) {
                case EN_REPARACION -> listaE[i][3] = "en reparación";
                case DADO_DE_BAJA -> listaE[i][3] = "dado de baja";
                case OPERATIVO -> listaE[i][3] = "operativo";
            }

            if (equipo.isArrendado()) {
                listaE[i][4] = "Arrendado";
            } else {
                listaE[i][4] = "Disponible";
            }
        }

        return listaE;
    }

    public void readDatosSistema() throws ArriendoException {
        ObjectInputStream reader;

        try {
            reader = new ObjectInputStream(new FileInputStream("Datos.obj"));
            instance = (ControladorArriendoEquipos) reader.readObject();
            reader.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new ArriendoException("No ha sido posible leer datos del sistema desde archivo");
        }

    }

    public void saveDatosSistemas() throws ArriendoException {
        ObjectOutputStream escritor;
        try {
            escritor = new ObjectOutputStream(new FileOutputStream("Datos.obj"));
            escritor.writeObject(this);
            escritor.close();
        } catch (IOException e) {
            throw new ArriendoException("No ha sido posible guardar datos del sistema desde archivo");
        }
    }

    private Cliente buscaCliente(String rut){
        for(Cliente cliente : clientes) {
            if(cliente.getRut().equals(rut)) {
                return cliente;
            }
        }

        return null;
    }

    private Equipo buscaEquipo(long codigo){
        for(Equipo equipo : equipos){
            if(codigo == equipo.getCodigo()){
                return equipo;
            }
        }

        return null;
    }

    private Arriendo buscaArriendo(long codigo){
        for(Arriendo arriendo : arriendos){
            if(codigo == arriendo.getCodigo()){
                return arriendo;
            }
        }

        return null;
    }

    public String[][] listaArriendosPagados(){
        String[][] detallesArriendosPagados;
        ArrayList<Arriendo> codArriendosPagados = new ArrayList<>();

        for(Arriendo arriendo : arriendos){
            if(arriendo.getPagosToString().length != 0){
                codArriendosPagados.add(arriendo);
            }
        }

        if (codArriendosPagados.size() == 0) {
            return new String[0][0];
        }

        detallesArriendosPagados = new String[codArriendosPagados.size()][6];
        for(int i = 0; i < codArriendosPagados.size(); i++){
            String[] resultado = new String[7];
            Arriendo arriendo = codArriendosPagados.get(i);
            resultado[0] = arriendo.getCodigo() + "";
            resultado[1] = arriendo.getEstado().toString().toLowerCase();
            resultado[2] = arriendo.getCliente().getRut();
            resultado[3] = arriendo.getCliente().getNombre();
            resultado[4] = arriendo.getMontoTotal() + "";
            resultado[5] = arriendo.getMontoPagado() + "";
            resultado[6] = arriendo.getSaldoAdeudado() + "";
            detallesArriendosPagados[i] = resultado;
        }

        return detallesArriendosPagados;
    }

    public String[][] listaPagosDeArriendo(long codArriendo) throws ArriendoException{
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe arriendo con el codigo dado");
        }
        if (arriendo.getEstado() != EstadoArriendo.DEVUELTO&&arriendo.getEstado()!=EstadoArriendo.PAGADO) {
            throw new ArriendoException("Arriendo no se encuentra habilitado para pagos");
        }

        return arriendo.getPagosToString();

    }

    private String formateaFecha(LocalDate fecha) {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
    }

}

