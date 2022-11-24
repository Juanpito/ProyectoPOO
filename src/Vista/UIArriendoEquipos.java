package Vista;
import java.sql.SQLOutput;
import java.util.Scanner;
import Controlador.*;
import Modelo.*;
public class UIArriendoEquipos {
    private static UIArriendoEquipos instancia = null;
    private final Scanner tcld;

    private UIArriendoEquipos() {
        tcld = new Scanner(System.in);
        tcld.useDelimiter("[\t|\r\n]+");
    }

    public static UIArriendoEquipos getInstancia() {
        if (instancia == null) {
            instancia = new UIArriendoEquipos();
        }
        return instancia;
    }

    public void menu() {
        int opcion;

        do {
            System.out.println("*** MENU DE OPCIONES ***");
            System.out.println("1. Crear un nuevo cliente");
            System.out.println("2. Crear un nuevo equipo");
            System.out.println("3. Lista todos los clientes");
            System.out.println("4. Lista todos los equipos");
            System.out.println("5. Salir");
            System.out.println("");
            System.out.print("Ingrese opcion: ");
            opcion = tcld.nextInt();

            switch (opcion) {
                case 1 -> creaCliente();
                case 2 -> creaEquipo();
                case 3 -> listaClientes();
                case 4 -> listaEquipos();
                case 5 -> {
                }
                default -> System.out.println("\nOpcion erronea!");
            }
        } while (opcion != 5);
        tcld.close();
    }

    private void creaCliente() {
        String nombre, domicilio, rut, numerotelefono;
        System.out.print("Ingrese rut: ");
        rut = tcld.next();
        if(validaRut(rut)==false){
            System.out.println("Error. Rut no valido");
            menu();
        }
        System.out.print("Ingrese nombre: ");
        nombre = tcld.next();
        if(validaNombre(nombre)==false){
            System.out.println("Error. Nombre no valido");
            menu();
        }
        System.out.print("Ingrese domicilio ");
        domicilio = tcld.next();
        if(validaNombre(domicilio)==false){
            System.out.println("Error. Domicilio no valido");
            menu();
        }
        System.out.print("Ingrese numero del telefono (11 digitos)");
        numerotelefono = tcld.next();
        if(validaNombre(numerotelefono)==false){
            System.out.println("Error. numero no valido");
            menu();
        }
        if(numerotelefono.length()!=11){
            System.out.println("numero no valido");
            menu();
        }
        System.out.println("");
        if(ControladorArriendoEquipos.getInstance().buscaCliente(rut).equals(rut)){
            throw new ClienteExcepcion("El cliente ya esta registrado");
        }else{
            ControladorArriendoEquipos.getInstance().creaCliente(rut, nombre, domicilio, numerotelefono);
            System.out.println("Cliente creado exitosamente");
        }
        menu();



    }

    private void creaEquipo() {
        String descripcion;
        int codigo, precio;
        System.out.print("Ingrese codigo: ");
        codigo = tcld.nextInt();
        System.out.print("Ingrese descripcion del sistema operativo: ");
        descripcion = tcld.next();
        System.out.print("Ingrese precio del arriendo por dia: ");
        precio = tcld.nextInt();
        if(ControladorArriendoEquipos.getInstance().getCodigo!= codigo){
        ControladorArriendoEquipos.getInstance().creaEquipo(descripcion, codigo, precio);
        System.out.print("Equipo creado exitosamente");
        }else{
        throw new EquipoExcepcion("Ya existe un equipo con el codigo dado");
        }
        System.out.println("");
        
        menu();
    }

    private void listaClientes() {
        String[][] datosClientes = ControladorArriendoEquipos.getInstance().listaClientes();
        System.out.println("\nLISTADO DE CLIENTES");
        System.out.println("------------");
        System.out.printf("%-25s%-25s%-25s%-25s%-25s%n", "RUT", "Nombre", "Direccion", "telefono","Estado");

        for (int i = 0; i < datosClientes.length; i++) {
            System.out.printf("%-25s%-25s%-25s%-25s%-25s%n", datosClientes[i][0], datosClientes[i][1], datosClientes[i][2],
                    datosClientes[i][3],datosClientes[i][4]);

        }

        System.out.println("");
    }

    private void listaEquipos() {
        String[][] datosEquipos = ControladorArriendoEquipos.getInstance().listaEquipos();
        System.out.println("\nLISTADO DE EQUIPOS");
        System.out.println("------------");
        System.out.printf("%-25s%-25s%-25s%-25s%n", "Codigo", " Descripcion", "Precio","Estado");
        for (int i = 0; i < datosEquipos.length; i++) {
            System.out.printf("%-25s%-25s%-25s%-25s%n", datosEquipos[i][0], datosEquipos[i][1], datosEquipos[i][2],datosEquipos[i][3]);
        }
        System.out.println("");
    }
    private void listaArriendos(){
        String[][] datosArriendos = ControladorArriendoEquipos.getInstance().listaArriendos();
        //falta colocar un perido que sea fecha inicio periodo, fecha fin periodo
        System.out.println("\nLISTADO DE ARRIENDOS");
        System.out.println("------------");
        System.out.printf("%-25s%-25s%-25s%-25s%n", "Codigo", "Fecha inicio", "Fecha devol.","Estado","Rut cliente","Monto total");
        for (int i = 0; i < datosArriendos.length; i++) {
            System.out.printf("%-25s%-25s%-25s%-25s%n", datosArriendos[i][0], datosArriendos[i][1], datosArriendos[i][2],datosArriendos[i][3],datosArriendos[i][4],datosArriendos[i][5]);
        }
        System.out.println("");
    }
    private static boolean validaRut(String rut){
return false; //es momentaneo,hay que cambiarlo
    }
    private static boolean validaNombre(String var){
        for(int i =0; i<var.length(); i++)
            if(var.charAt(i) != ' ')
                return false;

        return true;
    }
}
