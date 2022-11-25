package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.EstadoEquipo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//Test y fixes Gonzalo Inostroza y Fabian Bravo

//Parte de Luis Riquelme
public class UIArriendoEquipos {
    private static UIArriendoEquipos instance=null;

    // Crear el escaner como atributo simplificaria mucho más el codigo pero no se puede
    // debido a que no esta especificado en el diagrama de clases
    // private Scanner tlcd;

    private UIArriendoEquipos(){
        // En caso de poner el Scanner como atributo se debiería inicializar en el constructor
        // tcld = new Scanner(System.in).useDelimiter("\t|\r\n|[\n\r\u2028\u2029\u0085]");
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
            System.out.println("6.  Lista todos los clientes");
            System.out.println("7.  Lista todos los equipos");
            System.out.println("8.  Lista todos los arriendos");
            System.out.println("9.  Lista detalles de un arriendo");
            System.out.println("10. Salir");
            System.out.print("\tIngrese opcion: ");
                opcion = tcld.nextInt();

                switch (opcion){
                    case 1 -> creaCliente();
                    case 2 -> creaEquipo();
                    case 3 -> arriendaEquipos();
                    case 4 -> devuelveEquipos();
                    case 5 -> cambiaEstadoCliente();
                    case 6 -> listaClientes();
                    case 7 -> listaEquipos();
                    case 8 -> listaArriendos();
                    case 9 -> listaDetallesArriendo();


                    default -> System.out.println("***Opcion invalida intentelo de nuevo***");
                }

            }
        while(true);

    }

    private void creaCliente(){
        // Sigue preguntando datos incluso si el cliente ya existe
        // al final muestra el mensaje de error, pero no estos seguro si es la intencion de la pauta TODO
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


    private void creaEquipo(){
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

        System.out.print("Precio arriendo por dia: ");
        long precioArriendoDia;
        try {
            precioArriendoDia = tcld.nextLong();
        } catch (Exception e) {
            System.out.println("El valor debe ser numerico");
            return;
        }

        try {
            controlador.creaEquipo(cod, descripcion, precioArriendoDia);
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
            // No es necesario verificar que arriendo sea un numero ya que sabemos que los codigo de los arriendo
            // mostrados son correctos y en caso de que no este presente ya se devuelve al menu
            String[][] listaDetalles = ControladorArriendoEquipos.getInstance().listaDetallesArriendo(Long.parseLong(arriendo));
            // No tiene mucho sentido que esto diga "Ingrese codigo y estado..." pero eso dice la pauta
            // es mas probable que sea ingrese codigo de estado, de todas maneras lo marco como TODO
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
    private void cambiaEstadoCliente(){
        //Creo que no esta en el uml o las instrucciones, pero si se cambia el
        //estado de un cliente, lo hace sin importar si tiene un arriendo sin devolver
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
        System.out.println("LISTADO DE CLIENTES");
        System.out.println("-------------------\n");
        System.out.printf("%-13s %-23s %-23s %-13s %-8s %-13s%n","RUT","Nombre","Direccion","Telefono","Estado","Nro.Arr.Pdtes");
        String [][] listaclientes= ControladorArriendoEquipos.getInstance().listaClientes();
        for (String[] listacliente : listaclientes) {
            System.out.printf("%-13s %-23s %-23s %-13s %-8s %-13s%n", listacliente[0], listacliente[1], listacliente[2], listacliente[3], listacliente[4],listacliente[5]);
        }
    }

    private void listaEquipos(){
        System.out.println("LISTADO DE EQUIPOS");
        System.out.println("------------------");
        System.out.printf("%-12s %-45s %-7s %-12s %-10s%n","Codigo","Descripcion","Precio","Estado","Situacion");
        String [][] listaequipos= ControladorArriendoEquipos.getInstance().listaEquipos();
        for (String[] listaequipo : listaequipos) {
            System.out.printf("%-12s %-45s %-7s %-12s %-10s%n", listaequipo[0], listaequipo[1], listaequipo[2], listaequipo[3],listaequipo[4]);
        }

    }
    private void listaArriendos(){
        Scanner sc=new Scanner(System.in);
        String inicio, fin;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio, fechaFin;
        try {
            System.out.println("Fecha inicio periodo (dd/mm/aaaa): ");
            inicio = sc.next();
            fechaInicio= LocalDate.parse(inicio, formatter);
            System.out.println("Fecha fin periodo (dd/mm/aaaa): ");
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

        try {
            long codigoArriendo = tcld.nextLong();
            String[] datosArriendo = controlador.consultaArriendo(codigoArriendo);

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
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        }

    // De acuerdo con la guia del avance se pueden agregar metodos privados para simplificar el codigo
    //      "Solo se permite agregar métodos privados, si ello lleva a una mejora del código o se logra mayor simplicidad o legibilidad"
    private Scanner getTcld() {
        Scanner tcld = new Scanner(System.in);
        tcld.useDelimiter("\t|\r\n|[\n\r\u2028\u2029\u0085]");
        return tcld;
    }

    private boolean validaRut(String rut) {
        if (rut.equals("")) {
            return false;
        }
        // Para validar el rut se toman todos lso digitos (sin el digito verificador)
        // cada digito de multiplica (en orden inverso) con uno de los siguente numero [2,3,4,5,6,7]
        // luego se suma y se calcula el modulo 11 de lo anterior, el resultado es el digito verificador
        // si el digito verficador
        // (Nota: si el resultado es 10 se remplaza el numero con un K)

        // Eliminar . y -
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

            // Se puede calcular el numero de la secuencia usadando la posicion y el modulo 0
            // Si el primer digito la posicion es 0 (en sentido inverso) el modulo de 6 es 0 sumando 2 es 2
            // con el resto de numeros va a continuar aumentando en 1 hasta llegar al 6 donde vuelve a 0
            // de ese modo se tiene el minimo de 2 y maximo de 7, con una vuelta cada 6 digitos
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

