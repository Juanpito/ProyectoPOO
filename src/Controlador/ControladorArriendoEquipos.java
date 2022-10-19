package Controlador;

import Modelo.Cliente;
import Modelo.Equipo;

import java.util.ArrayList;

public class ControladorArriendoEquipos {

    private static ControladorArriendoEquipos instancia = null;
    private final ArrayList<Cliente> todosClientes;
    private final ArrayList<Equipo> todosEquipos;

    private ControladorArriendoEquipos() {
        todosEquipos = new ArrayList<>();
        todosClientes = new ArrayList<>();
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
