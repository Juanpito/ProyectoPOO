package Vista;
import java.util.Scanner;
import Controlador.ControladorClientes;
import Modelo.Cliente;
public class UIArriendoEquipos {
    private static UIArriendoEquipos instancia = null;
    private final Scanner tcld;

    private UIClientes() {
        tcld = new Scanner(System.in);
        tcld.useDelimiter("[\t|\r\n]+");
    }

    public static UIArriendoEquipos geUItInstancia() {
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
            System.out.println("\tIngrese opcion")
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
        System.out.print("\nIngrese nombre: ");
        nombre = tcld.next();
        System.out.print("Ingrese domicilio ");
        domicilio = tcld.next();
        System.out.print("Ingrese numero del telefono (11 digitos)");
        numerotelefono = tcld.next();
        ControladorArriendoEquipos.getInstancia().creaCliente(nombre, rut, numerotelefono, domicilio);
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
        ControladorArriendoEquipos.getInstancia().creaEquipo(descripcion, codigo, precio);
    }

    private void listaClientes() {
        String[][] datosClientes = ControladorArriendoEquipos.getInstancia().list();
        System.out.println("\nLISTADO DE CLIENTES");
        System.out.println("------------");
        System.out.printf("%-25s%-12s%10s%n", "RUT", "Nombre", "Direccion", "Estado");
        for (int i = 0; i < datosClientes.length; i++) {
            System.out.printf("%-25s%-12s%,10d%n", datosClientes[i][0], datosClientes[i][1], datosClientes[i][2],
                    datosClientes[i][3]);
        }
    }

    private void listaEquipos() {
        String[][] datosEquipos = ControladorArriendoEquipos.getInstancia().list();
        System.out.println("\nLISTADO DE EQUIPOS");
        System.out.println("------------");
        System.out.printf("%-25s%-12s%10s%n", "Codigo", "Descripcion", "Precio Estado");
        for (int i = 0; i < datosEquipos.length; i++) {
            System.out.printf("%-25s%-12s%,10d%n", datosEquipos[i][0], datosEquipos[i][1], datosEquipos[i][2]);
        }
    }
}
