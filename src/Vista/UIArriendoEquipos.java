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
    private static UIArriendoEquipos instance;

    private UIArriendoEquipos() {
    }

    public static UIArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }


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
            System.out.println("8.  Cargar datos desde archivo");
            System.out.println("9.  Guardar datos a archivo");
            System.out.println("10. Salir");
            System.out.print("\tIngrese opcion: ");
            opcion = getInt();
            switch (opcion) {
                case 1 -> creaCliente();
                case 2 -> creaEquipo();
                case 3 -> arriendaEquipos();
                case 4 -> devuelveEquipos();
                case 5 -> cambiaEstadoCliente();
                case 6 -> pagaArriendo();
                case 7 -> generaReportes();
                case 8 -> {
                    try {
                        ControladorArriendoEquipos.getInstance().readDatosSistema();
                        System.out.println("Datos cargados de forma exitosa");
                    } catch (ArriendoException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 9 -> {
                    try {
                        ControladorArriendoEquipos.getInstance().saveDatosSistemas();
                        System.out.println("Datos guardados de forma exitosa");
                    } catch (ArriendoException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 10 -> {
                    return;
                }
                default -> System.out.println("***Opcion invalida intentelo de nuevo***");
            }

        } while (true);

    }

    private void creaCliente() {

        Scanner tcld = getTcld();
        String rut, nom, dir, tel;

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
            ControladorArriendoEquipos.getInstance().creaCliente(rut, nom, dir, tel);

            System.out.println("Cliente creado exitosamente");
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void creaEquipo() {
        System.out.println("Creando un nuevo equipo...");
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        Scanner tcld = getTcld();

        System.out.print("Codigo: ");
        int cod = getInt();

        System.out.print("Descripcion: ");
        String descripcion = tcld.next();
        if (descripcion.trim().equals("")) {
            System.out.println("Valor no puede estar vacio");
            return;
        }

        int tipoEquipo;
        do {
            System.out.print("Tipo equipo (1: Implemento, 2: Conjunto): ");
            tipoEquipo = getInt();
        } while (tipoEquipo != 1 && tipoEquipo != 2);

        if (tipoEquipo == 1) {
            System.out.print("Precio arriendo por dia: ");
            long precioArriendoDia = getInt();

            try {
                controlador.creaImplemento(cod, descripcion, precioArriendoDia);
                System.out.println("Equipo creado exitosamente");
            } catch (EquipoException e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        System.out.print("\tNumero de equipos componentes: ");
        int nroComponente = getInt();
        long[] componentesCod = new long[nroComponente];

        for (int i = 0; i < nroComponente; i++) {
            System.out.print("\tCodigo de equipo " + (i+1) + " de " + nroComponente + ": ");
            long componente = getInt();
            componentesCod[i] = componente;
        }

        try {
            controlador.creaConjunto(cod, descripcion, componentesCod);
            System.out.println("Equipo creado exitosamente");
        } catch (EquipoException e) {
            System.out.println(e.getMessage());
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
                String descripcionEquipo = controlador.agregaEquipoToArriendo(arriendo, codigo);
                System.out.println("Se ha agregado " + descripcionEquipo + " al arriendo");

            } catch (EquipoException | ArriendoException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("No se pudo agregar equipo, el codigo debe ser un numero");
            }

            System.out.print("\nDesea agregar otro equipo al arriendo?(s/n): ");
            opcion = sc.next().toLowerCase();
            while (!opcion.equals("s") && !opcion.equals("n")) {
                System.out.println("Opcion invalida");
                System.out.print("\nDesea agregar otro equipo al arriendo?(s/n): ");
                opcion = sc.next().toLowerCase();
            }
        } while (opcion.equals("s"));

        try {
            long precio = controlador.cierraArriendo(arriendo);
            System.out.println("\nMonto total por dia de arriendo --> $" + precio);
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void devuelveEquipos() {

        Scanner tcld = getTcld();
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();

        System.out.println("Devolviendo equipos arrendados...");
        System.out.print("Rut Cliente: ");
        String rut = tcld.next();
        if (!validaRut(rut)) {
            System.out.println("El rut ingresado no es valido");
            return;
        }
        rut = formatearRUT(rut);

        try {
            // Obtener datos
            String[] cliente = controlador.consultaCliente(rut);
            String[][] listaArriendosPorDevolver = controlador.listaArriendosPorDevolver(rut);
            if (listaArriendosPorDevolver.length == 0) {
                System.out.println("El cliente no presenta arriendos por devolver");
                return;
            }
            System.out.println("Nombre cliente: " + cliente[1]);

            System.out.println("Los arriendos por devolver son =>> ");
            System.out.printf("%-13s %-23s %-23s %-13s %8s %-12s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Monto total");
            for (int i = 0; i < listaArriendosPorDevolver.length; i++) {
                System.out.printf("%-13s %-23s %-23s %-13s %8s %-12s%n",
                        listaArriendosPorDevolver[i][0], listaArriendosPorDevolver[i][1], listaArriendosPorDevolver[i][2],
                        listaArriendosPorDevolver[i][3], listaArriendosPorDevolver[i][4], listaArriendosPorDevolver[i][6]);
            }
            System.out.println("Codigo arriendo a devolver: ");
            String arriendo = tcld.next();
            boolean encontrado = false;
            for (int i = 0; i < listaArriendosPorDevolver.length; i++) {
                if (listaArriendosPorDevolver[i][0].equals(arriendo)) {
                    encontrado = true;
                }
            }
            if (!encontrado) {
                System.out.println("El arriendo especificado no existe o no pertenece a este cliente");
                return;
            }

            String[][] listaDetalles = controlador.listaDetallesArriendo(Long.parseLong(arriendo));

            System.out.println("Ingrese codigo y estado en que se devuelve cada equipo que se indica >>>");
            EstadoEquipo[] estadoEquipos = new EstadoEquipo[listaDetalles.length];
            for (int i = 0; i < listaDetalles.length; i++) {
                System.out.printf("%s(%s) -> Estado (1: Operativo, 2: En reparacion, 3: Dado de baja): ",
                        listaDetalles[i][1], listaDetalles[i][0]);
                boolean validezEstado = false;
                do {
                    String estado = tcld.next();

                    switch (estado) {
                        case "1" -> {
                            estadoEquipos[i] = EstadoEquipo.OPERATIVO;
                            validezEstado = true;
                        }
                        case "2" -> {
                            estadoEquipos[i] = EstadoEquipo.EN_REPARACION;
                            validezEstado = true;
                        }
                        case "3" -> {
                            estadoEquipos[i] = EstadoEquipo.DADO_DE_BAJA;
                            validezEstado = true;
                        }
                        default -> System.out.println("Opcion no valida intente de nuevo");
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

    private void pagaArriendo() {
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        Scanner tcld = getTcld();
        System.out.println("Pagando arriendo...");
        System.out.print("Codigo arriendo a pagar: ");
        long codigo = getInt();

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
            opcionPago = getInt();
        } while (opcionPago != 1 && opcionPago != 2 && opcionPago != 3);


        System.out.print("Monto: ");
        int monto = getInt();

        if (opcionPago == 1) {
            try {
                controlador.pagaArriendoContado(codigo, monto);
            } catch (ArriendoException e) {
                System.out.println(e.getMessage());
            }
            return;
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
            }
            return;
        }

        System.out.print("Numero de cuotas: ");
        int numCuotas = getInt();

        try {
            controlador.pagaArriendoCredito(codigo, monto, codigoTransaccion, numTarjeta, numCuotas);
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cambiaEstadoCliente() {

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

    private void generaReportes() {
        System.out.println("\n\n*** MENU DE REPORTES ***");
        System.out.println("1. Lista todos los clientes");
        System.out.println("2. Lista todos los equipos");
        System.out.println("3. Lista todos los arriendos");
        System.out.println("4. Lista detalles de un arriendo");
        System.out.println("5. Lista arriendo con pagos");
        System.out.println("6. Lista los pagos de un arriendo");
        System.out.println("7. Salir");
        System.out.print("\t Ingrese opcion: ");
        int opcion = getInt();
        switch (opcion) {
            case 1 -> listaClientes();
            case 2 -> listaEquipos();
            case 3 -> listaArriendos();
            case 4 -> listaDetallesArriendo();
            case 5 -> listaArriendosPagados();
            case 6 -> listaPagosDeUnArriendo();
            case 7 -> System.out.println("Saliendo...");
            default -> System.out.println("Opcion invalida");
        }
    }

    private void listaClientes() {
        String[][] listaclientes = ControladorArriendoEquipos.getInstance().listaClientes();
        if (listaclientes.length == 0) {
            System.out.println("No hay clientes que mostrar");
            return;
        }

        System.out.println("LISTADO DE CLIENTES");
        System.out.println("-------------------\n");
        System.out.printf("%-13s %-23s %-23s %-13s %-8s %-13s%n", "RUT", "Nombre", "Direccion", "Telefono", "Estado", "Nro.Arr.Pdtes");
        for (String[] listacliente : listaclientes) {
            System.out.printf("%-13s %-23s %-23s %-13s %-8s %-13s%n", listacliente[0], listacliente[1], listacliente[2], listacliente[3], listacliente[4], listacliente[5]);
        }
    }

    private void listaEquipos() {
        String[][] listaequipos = ControladorArriendoEquipos.getInstance().listaEquipos();
        if (listaequipos.length == 0) {
            System.out.println("No hay equipos que mostrar");
            return;
        }
        System.out.println("LISTADO DE EQUIPOS");
        System.out.println("------------------");
        System.out.printf("%-12s %-45s %-7s %-12s %-10s%n", "Codigo", "Descripcion", "Precio", "Estado", "Situacion");
        for (String[] listaequipo : listaequipos) {
            System.out.printf("%-12s %-45s %-7s %-12s %-10s%n", listaequipo[0], listaequipo[1], listaequipo[2], listaequipo[3], listaequipo[4]);
        }

    }

    private void listaArriendos() {
        Scanner sc = new Scanner(System.in);
        String inicio, fin;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio, fechaFin;
        try {
            System.out.println("Fecha inicio periodo (dd/mm/aaaa): ");
            inicio = sc.next();
            fechaInicio = LocalDate.parse(inicio, formatter);
            System.out.println("Fecha fin periodo (dd/mm/aaaa): ");
            fin = sc.next();
            fechaFin = LocalDate.parse(fin, formatter);
        } catch (Exception e) {
            System.out.println("Formato de fecha ingresado no es valido");
            return;
        }
        String[][] arriendos = ControladorArriendoEquipos.getInstance().listaArriendos(fechaInicio, fechaFin);
        if (arriendos.length == 0) {
            System.out.println("No hay arriendos que mostrar");
            return;
        }
        System.out.println("LISTADO DE ARRIENDOS");
        System.out.println("--------------------");
        System.out.printf("%-7s %-13s %-13s %-10s %-12s %-12s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Monto total");
        for (String[] arriendo : arriendos) {
            System.out.printf("%-7s %-13s %-13s %-10s %-12s %-12s%n",
                    arriendo[0], arriendo[1], arriendo[2],
                    arriendo[3], arriendo[4], arriendo[5]);
        }

    }

    private void listaDetallesArriendo() {
        Scanner tcld = getTcld();
        ControladorArriendoEquipos controlador = ControladorArriendoEquipos.getInstance();
        System.out.println("Codigo arriendo: ");

        // Check if valid
        long codigoArriendo = tcld.nextLong();
        String[] datosArriendo = controlador.consultaArriendo(codigoArriendo);

        if (datosArriendo.length == 0) {
            System.out.println("No existe un arriendo con el codigo dado ");
            return;
        }

        String[][] detallesArriendo = controlador.listaDetallesArriendo(codigoArriendo);

        System.out.println("-------------------------------------------------------");
        System.out.println("Codigo:" + datosArriendo[0]);
        System.out.println("Fecha inicio:" + datosArriendo[1]);
        System.out.println("Fecha devolucion:" + datosArriendo[2]);
        System.out.println("Estado:" + datosArriendo[3]);
        System.out.println("Rut cliente:" + datosArriendo[4]);
        System.out.println("Nombre cliente:" + datosArriendo[5]);
        System.out.println("Monto total:" + datosArriendo[6]);
        System.out.println("-------------------------------------------------------");
        System.out.println("\t\t\tDETALLE DEL ARRIENDO");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-13s %-19s %-24s%n", "Codigo Equipo", "Descripcion equipo", "Precio arriendo por dia");
        for (String[] detalle : detallesArriendo) {
            System.out.printf("%-13s %-19s %-24s%n", detalle[0], detalle[1], detalle[2]);
        }
    }

    private void listaArriendosPagados() {
        String[][] arriendosPagados = ControladorArriendoEquipos.getInstance().listaArriendosPagados();
        if (arriendosPagados.length == 0) {
            System.out.println("No hay arriendos pagados");
            return;
        }
        System.out.println("LISTADO DE ARRIENDOS");
        System.out.println("--------------------");
        System.out.printf("%-7s %-13s %-13s %-14s %-12s %-12s %-12s%n", "Codigo", "Estado", "Rut cliente", "Nombre cliente", "Monto deuda", "Monto pagado", "Saldo adeudado");
        for (String[] arriendosPagado : arriendosPagados) {
            System.out.printf("%-7s %-13s %-13s %-14s %-12s %-12s %-12s%n",
                    arriendosPagado[0], arriendosPagado[1], arriendosPagado[2],
                    arriendosPagado[3], arriendosPagado[4], arriendosPagado[5],
                    arriendosPagado[6]);
        }


    }

    private void listaPagosDeUnArriendo() {
        System.out.print("\t Codigo arriendo: ");
        long cod = getInt();

        String[][] pagosDeUnArriendo;
        try {
            pagosDeUnArriendo = ControladorArriendoEquipos.getInstance().listaPagosDeArriendo(cod);
        } catch (ArriendoException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (pagosDeUnArriendo.length == 0) {
            System.out.println("El arriendo tiene pagos asociados");
            return;
        }

        System.out.println(">>>>>>>>>>>   PAGOS REALIZADOS    <<<<<<<<<<<");
        System.out.printf("%-7s %-13s %-13s%n", "Monto", "Fecha", "Tipo de pago");
        for (String[] strings : pagosDeUnArriendo) {
            System.out.printf("%-7s %-13s %-13s%n",
                    strings[0], strings[1], strings[2]);
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
        String rutSinDigito = rut.substring(0, rut.length() - 1);
        int digitoVerificador;


        try {
            if (rut.charAt(rut.length() - 1) == 'K' || rut.charAt(rut.length() - 1) == 'k') {
                digitoVerificador = 10;
            } else {
                digitoVerificador = Integer.parseInt(rut.charAt(rut.length() - 1) + "");
            }
        } catch (Exception e) {
            return false;
        }

        int sumadorRut = 0;
        for (int i = rutSinDigito.length() - 1; i >= 0; i--) {
            int digito;
            try {
                digito = Integer.parseInt(rutSinDigito.charAt(i) + "");
            } catch (NumberFormatException e) {
                return false;
            }


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

    private int getInt() {
        int numero;
        Scanner tlcd = getTcld();
        boolean isNumero = true;
        do {
            try {
                String numeroString = tlcd.next();
                numero = Integer.parseInt(numeroString);
                isNumero = false;
            } catch (NumberFormatException e) {
                System.out.println("Opcion debe ser numerico");
                numero = 0; // Esto es solo para que el compilador no siga llorando >:(
            }
        } while (isNumero);
        return numero;
    }
}

