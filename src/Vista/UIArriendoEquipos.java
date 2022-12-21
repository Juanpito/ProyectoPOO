package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.EstadoEquipo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UIArriendoEquipos {
    private static UIArriendoEquipos instance=null;
    private UIArriendoEquipos(){
    }

    public static UIArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }

    //invocacion de menú
    public void menu() {
        int opcion;
        do {
            Scanner tcld = getTcld();
            System.out.println("******* SISTEMA DE ARRIENDO DE EQUIPOS DE NIEVE ********\n");
            System.out.println("*** MENU DE OPCIONES ***");
            System.out.println("1.  Crea un nuevo cliente");
            System.out.println("2.  Crea un nuevo equipo");
            System.out.println("3.  Arrienda equipos");
            System.out.println("4.  Devuelve equipos");
            System.out.println("5.  Cambia estado de un cliente");
            System.out.println("6.  Paga arriendo");
            System.out.println("7.  Genera reportes");
            System.out.println("8.  cargar los datos del archivo");
            System.out.println("9.  Guardar los datos del archivo");
            System.out.println("10. Salir");
            System.out.print("\tIngrese opcion: ");
                opcion = tcld.nextInt();

                switch (opcion){
                    case 1 -> creaCliente();
                    case 2 -> creaEquipo();
                    case 3 -> arriendaEquipos();
                    case 4 -> devuelveEquipos();
                    case 5 -> cambiaEstadoCliente();
                    case 6 -> pagaArriendo();
                    case 7 -> generaReportes();
                    case 8 -> listaArriendos();
                    case 9 -> listaDetallesArriendo();
                    case 10->{}


                    default -> System.out.println("***Opcion invalida intentelo de nuevo***");
                }

            }
        while(true);

    }

    private void generaReportes(){
        int opcion;
        do {
            Scanner tcld = getTcld();
            System.out.println("*** MENU DE REPORTES ***\n");
            System.out.println("1.  Lista todos los clientes");
            System.out.println("2.  Lista todos los equipos");
            System.out.println("3.  Lista todos los arriendos");
            System.out.println("4.  Lista detalles de un arriendo");
            System.out.println("5.  Lista arriendos con pagos");
            System.out.println("6.  Lista los pagos de un arriendo");
            System.out.println("7.  Salir");
            System.out.print("\tIngrese opcion: ");

            try {
                opcion = tcld.nextInt();
                switch (opcion){
                    case 1 -> listaClientes();
                    case 2 -> listaEquipos();
                    case 3 -> listaArriendos();
                    case 4 -> listaDetallesArriendo();
                    case 5 -> listaArriendospagados();
                    case 6 -> listaPagosDeUnArriendo();
                    case 7 -> {
                        return;
                    }
                    default -> System.out.println("***Opcion invalida intentelo de nuevo***");
                }
            }catch (Exception e){
                System.out.println("Opcion debe ser un numero!");
            }

        }while(true);
    }


    private void creaCliente(){
        Scanner tcld = getTcld();
        String rut,nom, dir, tel;

        System.out.println("Creando un nuevo cliente...");

        System.out.print("Rut: ");
        rut = tcld.next();
        if (!validaRut(rut)) {
            System.out.println("El rut entregado no es valido");
            return;
        }
        rut = formatearRUT(rut);

        System.out.print("Nombre: ");
        nom = tcld.next();
        if (nom.equals("")) {
            System.out.println("No puede dejar dato en blanco");
            return;
        }

        System.out.print("Direccion: ");
        dir = tcld.next();
        if (dir.equals("")) {
            System.out.println("No puede dejar dato en blanco");
            return;
        }

        System.out.print("Telefono: ");
        tel = tcld.next();
        if (tel.equals("")) {
            System.out.println("No puede dejar dato en blanco");
            return;
        }

        try {
            ControladorArriendoEquipos.getInstance().creaCliente(rut,nom,dir,tel);
            System.out.println("Cliente creado exitosamente :)");
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
        }
    }


    private void creaEquipo(){// entendiendo que ya no existe el creaEquipo, se entiende a que hace referencia al implemento
        String [] equipos;
        int cantidad;
        System.out.println("Creando un nuevo equipo...");
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        Scanner tcld = getTcld();

        System.out.print("Codigo: ");
        String cod = tcld.next();
        try {
            int codigoNumerico = Integer.parseInt(cod);
            if (codigoNumerico < 0) {
                System.out.println("Valor no puede ser negativo");
                return;
            }
        } catch (Exception e) {
            System.out.println("Codigo debe ser un numero");
            return;
        }

        System.out.print("Descripcion: ");
        String descripcion = tcld.next();
        if (descripcion.replace(" ", "").equals("")) {
            System.out.println("Valor no puede estar vacio");
            return;
        }

        System.out.print("\tPrecio arriendo por dia");
        long precio;
        try {
            precio = getTcld().nextLong();

        }catch(Exception e){
            System.out.println("El valor debe ser numerico");
            return;
        }

        System.out.print("Tipo equipo (1: Implemento, 2:Conjunto");
        int tipoEquipo;
        try{
            tipoEquipo = tcld.nextInt();
        }catch (Exception e){
            System.out.println("El valor debe ser numerico");
            return;
        }
        if(tipoEquipo==2){
            System.out.print("\tNumero de equipos componentes:");

            try{
                cantidad= tcld.nextInt();
                equipos=new String[cantidad];
            }catch (Exception e){
                System.out.println("El valor debe ser numerico");
                return;
            }



            int cont=0;

            do{
                cont=cont+1;
                System.out.print("\tcodigo de equipo "+cont+" de "+cantidad+":");
                try{
                    String comp=getTcld().next();
                    equipos[cont-1]=comp;

                }catch (Exception e){
                    System.out.println("El valor debe ser numerico");
                    return;
                }
            }while(cont!=cantidad);

            for(int i=0;i<equipos.length;i++){
                try {
                    ControladorArriendoEquipos.getInstance().creaImplemento(Long.parseLong(equipos[i]),descripcion,precio);
                } catch (EquipoException e) {
                    throw new RuntimeException(e);
                }
            }
        }



    }






    private void arriendaEquipos() {
        System.out.println("Arrendando equipos...");
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();

        System.out.print("Rut Cliente: ");
        Scanner sc = getTcld();
        String rut = sc.next();
        if (!validaRut(rut)) {
            System.out.println("El rut ingresado no es valido");
            return;
        }
        rut = formatearRUT(rut);

        String opcion;
        long arriendo;

        try {
            String[] cliente = controlador.consultaCliente(rut);
            arriendo = controlador.creaArriendo(rut);
            System.out.println("Nombre cliente: " + cliente[1]);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
            return;
        }

        do {
            System.out.print("\nCodigo equipo: ");

            try {
                String codStr = sc.next();
                long codigo = Long.parseLong(codStr);
                controlador.agregaEquipoToArriendo(arriendo, codigo);
                String[] equipo = controlador.consultaEquipo(codigo);
                System.out.println("Se ha agregado " + equipo[1] + " al arriendo");
            } catch (EquipoException e) {
                System.out.println(e.getMessage());
            } catch (ArriendoException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("No se pudo agregar equipo, el codigo debe ser un numero");
            }
            System.out.print("\nDesea agregar otro equipo al arriendo?(s/n): ");
            opcion= sc.next().toLowerCase();
            while (!opcion.equals("s") && !opcion.equals("n")) {
                System.out.println("Opcion invalida");
                System.out.print("\nDesea agregar otro equipo al arriendo?(s/n): ");
                opcion= sc.next().toLowerCase();
            }
        }while(opcion.equals("s"));

        try {
            long precio = controlador.cierraArriendo(arriendo);
            System.out.println("\nMonto total por dia de arriendo --> $"+precio);
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void devuelveEquipos(){
        //Muestra que el cliente no tiene arriendos por devolver cuando antes se habia arrendado 1 al rut ingresado
        Scanner tcld = getTcld();
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();

        System.out.println("Devolviendo equipos arrendados...");
        System.out.print("Rut Cliente: ");
        String rut= tcld.next();
        if (!validaRut(rut)) {
            System.out.println("El rut ingresado no es valido");
            return;
        }
        rut = formatearRUT(rut);

        try {
            // Obtener datos
            String [] cliente= controlador.consultaCliente(rut);
            String [][] listaArriendosPorDevolver = controlador.listaArriendosPorDevolver(rut);
            if (listaArriendosPorDevolver.length == 0) {
                System.out.println("El cliente no presenta arriendos por devolver");
                return;
            }
            System.out.println("Nombre cliente: " + cliente[1]);

            System.out.println("Los arriendos por devolver son =>> ");
            System.out.printf("%-13s %-23s %-23s %-13s %8s %-12s%n","Codigo","Fecha inicio","Fecha devol.","Estado","Rut cliente","Monto total");
            for(int i=0;i< listaArriendosPorDevolver.length;i++){
                System.out.printf("%-13s %-23s %-23s %-13s %8s %-12s%n",
                        listaArriendosPorDevolver[i][0],listaArriendosPorDevolver[i][1],listaArriendosPorDevolver[i][2],
                        listaArriendosPorDevolver[i][3],listaArriendosPorDevolver[i][4],listaArriendosPorDevolver[i][6]);
            }
            System.out.println("Codigo arriendo a devolver: ");
            String arriendo = tcld.next();
            boolean encontrado = false;
            for (int i=0; i<listaArriendosPorDevolver.length; i++) {
                if (listaArriendosPorDevolver[i][0].equals(arriendo)) {
                    encontrado = true;
                }
            }
            if (!encontrado) {
                System.out.println("El arriendo especificado no existe o no pertenece a este cliente");
                return;
            }

            String[][] listaDetalles = ControladorArriendoEquipos.getInstance().listaDetallesArriendo(Long.parseLong(arriendo));
            System.out.println("Ingrese codigo y estado en que se devuelve cada equipo que se indica >>>");
            EstadoEquipo[] estadoEquipos = new EstadoEquipo[listaDetalles.length];
            for (int i = 0; i < listaDetalles.length; i++) {
                System.out.printf("%s(%s) -> Estado (1: Operativo, 2: En reparacion, 3: Dado de baja): ",
                        listaDetalles[i][1] , listaDetalles[i][0]);
                boolean validezEstado = false;
                do {
                    String estado = tcld.next();

                    switch (estado) {
                        case "1" -> {
                            estadoEquipos[i] = EstadoEquipo.OPERATIVO;
                            validezEstado = true;
                        } case "2" -> {
                            estadoEquipos[i] = EstadoEquipo.EN_REPARACION;
                            validezEstado = true;
                        } case "3" -> {
                            estadoEquipos[i] = EstadoEquipo.DADO_DE_BAJA;
                            validezEstado = true;
                        } default -> System.out.println("Opcion no valida intente de nuevo");
                    }
                } while (!validezEstado);
            }
            controlador.devuelveEquipos(Long.parseLong(arriendo), estadoEquipos);
            System.out.printf("%d equipos(s) fue(ron) devuelto(s) exitosamente%n", listaDetalles.length);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
        }
    }


    private void pagaArriendo() {//nuevo metodo
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        Scanner tcld = getTcld();
        System.out.println("Pagando de arriendo...");
        System.out.print("Codigo arriendo a pagar: ");
        long codigo =tcld.nextLong();

        String[] datosArriendo = controlador.consultaArriendoAPagar(codigo);
        if (datosArriendo.length == 0) {
            System.out.println("El arriendo indicado no existe o no esta devuelto");
            return;
        }

        System.out.println("----- ANTECEDENTES DEL ARRIENDO -----");
        System.out.println("Codigo: " + datosArriendo[0]);
        System.out.println("Estado: " + datosArriendo[1]);
        System.out.println("Rut cliente: " + datosArriendo[2]);
        System.out.println("Nombre cliente: " + datosArriendo[3]);
        System.out.println("Monto total: $" + datosArriendo[4]);
        System.out.println("Monto pagado: $" + datosArriendo[5]);
        System.out.println("Saldo adeudado: $" + datosArriendo[6]);
        System.out.println();

        System.out.println("----- ANTECEDENTES DEL PAGO -----");
        int opcionPago;
        do {
            System.out.println("Medio de pago (1: Contado, 2: Debito, 3: Credito)");
            opcionPago = tcld.nextInt();
        }while (opcionPago != 1 && opcionPago != 2 && opcionPago != 3);


        System.out.print("Monto: ");
        int monto = tcld.nextInt();

        if (opcionPago == 1) {
            try {
                ControladorArriendoEquipos.getInstance().pagaArriendoContado(codigo, monto);
            } catch (ArriendoException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        System.out.print("Codigo transaccion: ");
        String codigoTransaccion = tcld.next();

        System.out.print("Numero tarjeta: ");
        String numTarjeta = tcld.next();

        if (opcionPago == 2) {
            try {
                controlador.pagaArriendoDebito(codigo, monto, codigoTransaccion, numTarjeta);
            } catch (ArriendoException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        System.out.print("Numero de cuotas: ");
        int numCuotas = tcld.nextInt();

        try {
            controlador.pagaArriendoCredito(codigo, monto, codigoTransaccion, numTarjeta, numCuotas);
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
        }
    }


    private void cambiaEstadoCliente(){
        System.out.println("Cambiando el  estado a un cliente... ");
        System.out.print("Rut cliente: ");
        Scanner sc = getTcld();
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        String rut = sc.next();
        if (!validaRut(rut)) {
            System.out.println("El rut entregado no es valido");
            return;
        }
        rut = formatearRUT(rut);
        try {
            controlador.cambiaEstadoCliente(rut);
            String[] cliente = controlador.consultaCliente(rut);
            System.out.printf("Se ha cambiado exitosamente el estado del cliente \"%s\" a \"%s\"%n", cliente[1], cliente[4]);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
        }
    }



    private void listaClientes(){
        String [][] listaclientes= ControladorArriendoEquipos.getInstance().listaClientes();
        if (listaclientes.length == 0) {
            System.out.println("No hay clientes que mostrar");
            return;
        }

        System.out.println("LISTADO DE CLIENTES");
        System.out.println("-------------------\n");
        System.out.printf("%-13s %-23s %-23s %-13s %-8s %-13s%n","RUT","Nombre","Direccion","Telefono","Estado","Nro.Arr.Pdtes");
        String [][] listaclientes2= ControladorArriendoEquipos.getInstance().listaClientes();
        for (String[] listacliente : listaclientes2) {
            System.out.printf("%-13s %-23s %-23s %-13s %-8s %-13s%n", listacliente[0], listacliente[1], listacliente[2], listacliente[3], listacliente[4],listacliente[5]);
        }
    }

    private void listaEquipos(){
        String [][] listaequipos= ControladorArriendoEquipos.getInstance().listaEquipos();
        if (listaequipos.length == 0) {
            System.out.println("No hay equipos que mostrar");
            return;
        }

        System.out.println("LISTADO DE EQUIPOS");
        System.out.println("------------------");
        System.out.printf("%-12s %-45s %-7s %-12s %-10s%n","Codigo","Descripcion","Precio","Estado","Situacion");
        String [][] listaequipos2= ControladorArriendoEquipos.getInstance().listaEquipos();
        for (String[] listaequipo : listaequipos2) {
            System.out.printf("%-12s %-45s %-7s %-12s %-10s%n", listaequipo[0], listaequipo[1], listaequipo[2], listaequipo[3],listaequipo[4]);
        }

    }
    private void listaArriendos(){
        Scanner sc=new Scanner(System.in);
        String inicio, fin;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio, fechaFin;
        try {
            System.out.println("Fecha inicio periodo (dd/MM/yyyy): ");
            inicio = sc.next();
            fechaInicio= LocalDate.parse(inicio, formatter);
            System.out.println("Fecha fin periodo (dd/MM/yyyy): ");
            fin = sc.next();
            fechaFin= LocalDate.parse(fin, formatter);
        } catch (Exception e) {
            System.out.println("Formato de fecha ingresado no es valido");
            return;
        }
        String [][] arriendos=ControladorArriendoEquipos.getInstance().listaArriendos(fechaInicio,fechaFin);
        System.out.println("LISTADO DE ARRIENDOS");
        System.out.println("--------------------");
        System.out.printf("%-7s %-13s %-13s %-10s %-12s %-12s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Monto total");
        for(int i =0;i< arriendos.length;i++){
            System.out.printf("%-7s %-13s %-13s %-10s %-12s %-12s%n",
                    arriendos[i][0],arriendos[i][1],arriendos[i][2],
                    arriendos[i][3],arriendos[i][4],arriendos[i][5]);
        }

    }

    private void listaDetallesArriendo(){
        Scanner tcld = getTcld();
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        System.out.println("Codigo arriendo: ");
        String[]datosArriendo=new String[0];
        long codigoArriendo = tcld.nextLong();

        try {
            datosArriendo = controlador.consultaArriendo(codigoArriendo);
        }catch(Exception e){
            System.out.println("Arriendo vacio");
        }
        if(datosArriendo.length == 0){
            System.out.println("No existe un arriendo con el codigo dado ");
            return;
        }


        String[][] detallesArriendo = controlador.listaDetallesArriendo(codigoArriendo);

        System.out.println("-------------------------------------------------------");
        System.out.println("Codigo:" + datosArriendo[0]);
        System.out.println("Fecha inicio:"+ datosArriendo[1]);
        System.out.println("Fecha devolucion:"+ datosArriendo[2]);
        System.out.println("Estado:"+ datosArriendo[3]);
        System.out.println("Rut cliente:"+ datosArriendo[4]);
        System.out.println("Nombre cliente:"+ datosArriendo[5]);
        System.out.println("Monto total:"+ datosArriendo[6]);
        System.out.println("-------------------------------------------------------");
        System.out.println("\t\t\tDETALLE DEL ARRIENDO");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-13s %-19s %-24s%n", "Codigo Equipo", "Descripcion equipo", "Precio arriendo por dia");
        for (String[] detalle: detallesArriendo) {
            System.out.printf("%-13s %-19s %-24s%n", detalle[0], detalle[1], detalle[2]);
        }

        }


    private void listaArriendospagados(){
        String [][] Arriendospagados= ControladorArriendoEquipos.getInstance().listaArriendosPagados();
        System.out.println("LISTADO DE ARRIENDOS");
        System.out.println("--------------------");
        System.out.printf("%-7s %-13s %-13s %-10s %-12s %-12s%n", "Codigo", "Estado", "Rut cliente", "Nombre cliente", "Monto deuda", "Monto pagado","Saldo adeudado");
        for(int i =0;i< Arriendospagados.length;i++){
            System.out.printf("%-7s %-13s %-13s %-10s %-12s %-12s%n",
                    Arriendospagados[i][0],Arriendospagados[i][1],Arriendospagados[i][2],
                    Arriendospagados[i][3],Arriendospagados[i][4],Arriendospagados[i][5],
                    Arriendospagados[i][6],Arriendospagados[i][7]);
        }


    }
    private void listaPagosDeUnArriendo() throws ArriendoException {
        System.out.print("\t Codigo arriendo");
        long cod=0;

        try {
            cod = getTcld().nextInt();
        } catch (Exception e) {
            System.out.println("Opcion no valida, intente nuevamente");
        }

        String[][] pagosDeUnArriendo = ControladorArriendoEquipos.getInstance().listaPagosDeArriendo(cod);
        if(pagosDeUnArriendo==null){
            System.out.printf("El arriendono tiene pagos asociados");
        }else {
            System.out.print(">>>>>>>>>>>   PAGOS REALIZADOS    <<<<<<<<<<<");
            System.out.printf("%-7s %-13s %-13s", "Monto", "Fecha", "Tipo de pago");
            for (int i = 0; i < pagosDeUnArriendo.length; i++) {
                System.out.printf("%-7s %-13s %-13s",
                        pagosDeUnArriendo[i][0], pagosDeUnArriendo[i][1], pagosDeUnArriendo[i][2]);
            }
        }



    }







    private Scanner getTcld() {
        Scanner tcld = new Scanner(System.in);
        tcld.useDelimiter("\t|\r\n|[\n\r\u2028\u2029\u0085]");
        return tcld;
    }

    private boolean validaRut(String rut) {
        if (rut.equals("")) {
            return false;
        }



        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        String rutSinDigito = rut.substring(0, rut.length()-1);
        int digitoVerificador;

        // Verificar que el rut sean solo datos numericos
        try {
            if (rut.charAt(rut.length()-1) == 'K' || rut.charAt(rut.length()-1) == 'k') {
                digitoVerificador = 10;
            } else {
                digitoVerificador = Integer.parseInt(rut.charAt(rut.length()-1) + "");
            }
        } catch (Exception e) {
            return false;
        }

        int sumadorRut = 0;
        for (int i=rutSinDigito.length() - 1; i >= 0; i--) {
            int digito = Integer.parseInt(rutSinDigito.charAt(i) + "");

            int multiplicaRut = ((rutSinDigito.length() - i - 1) % 6) + 2;

            sumadorRut += digito * multiplicaRut;
        }

        if (11 - (sumadorRut % 11) == digitoVerificador) {
            return true;
        }
        return false;
    }


    private String formatearRUT(String rut) {
        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        format = "-" + rut.substring(rut.length() - 1);
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
    }

}

