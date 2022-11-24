package Vista;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;
import Controlador.ControladorArriendoEquipos;
import Excepciones.ClienteExcepcion;
import Excepciones.EquipoExcepcion;
import Modelo.Cliente;
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
        System.out.println("BIENVENIDO");
        System.out.println("");
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

    private void creaCliente(ArrayList todosClientes) {
        String nombre, domicilio, rut, numerotelefono;
        System.out.print("Ingrese rut: ");
        rut = tcld.next();
        System.out.print("Ingrese nombre: ");
        nombre = tcld.next();
        System.out.print("Ingrese domicilio ");
        domicilio = tcld.next();
        System.out.print("Ingrese numero del telefono (11 digitos)");
        numerotelefono = tcld.next();
        while (numerotelefono.length()!=11) {
            if (numerotelefono.length() != 11) {
                System.out.println("Ingrese un numero de telefono correcto (11 digitos)");
                numerotelefono = tcld.next();
            }
        }
        try {
            for (Object nuevo : todosClientes) {
                if (nuevo != todosClientes){
                    System.out.println("Cliente creado");
                }else{
                    throw new ClienteExcepcion("Ya existe un cliente con el rut dado");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());

        }
        try {
            for (Object nuevo : todosClientes) {
                if (nuevo != todosClientes){
                    System.out.println("Cliente creado");
                }else{
                    throw new ClienteExcepcion("El cliente ya esta registrado");
                }
            }
        }catch (Exception e){
            System.out.println("");
        }
        UIArriendoEquipos.getInstancia().creaCliente(rut, nombre, domicilio, numerotelefono);
    }

    private void creaEquipo(ArrayList todosEquipos) {
        String descripcion;
        int codigo, precio;
        System.out.print("Ingrese codigo: ");
        codigo = tcld.nextInt();
        System.out.print("Ingrese descripcion del sistema operativo: ");
        descripcion = tcld.next();
        System.out.print("Ingrese precio del arriendo por dia: ");
        precio = tcld.nextInt();
        UIArriendoEquipos.getInstancia().creaEquipo(descripcion, codigo, precio);
        System.out.println("");
        try {
            for (Object nuevo : todosEquipos) {
                if (nuevo != todosEquipos){
                    System.out.println("Equipo creado");
                }else{
                    throw new EquipoExcepcion("Ya existe un equipo con el codigo dado");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void listaClientes() {
        String[][] datosClientes = UIArriendoEquipos.getInstancia().listaClientes();
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
        String[][] datosEquipos = UIArriendoEquipos.getInstancia().listaEquipos();
        System.out.println("\nLISTADO DE EQUIPOS");
        System.out.println("------------");
        System.out.printf("%-25s%-25s%-25s%-25s%n", "Codigo", "Descripcion", "Precio","Estado");
        for (int i = 0; i < datosEquipos.length; i++) {
            System.out.printf("%-25s%-25s%-25s%-25s%n", datosEquipos[i][0], datosEquipos[i][1], datosEquipos[i][2],datosEquipos[i][3]);
        }
        System.out.println("");
    }
    private void listaArriendos(){}
}
