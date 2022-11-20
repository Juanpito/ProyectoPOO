package Modelo;


import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;

public class Arriendo {
    private long codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private EstadoArriendo estado;
    ArrayList<DetalleArriendo>detalleArriendos;
    Cliente cliente;


    public Arriendo(long codigo, LocalDate fechaInicio,Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio =fechaInicio;
        this.cliente=cliente;
        cliente.addArriendo(this);
        this.estado=EstadoArriendo.INICIADO;
        this.detalleArriendos=new ArrayList<>();
        fechaDevolucion=null;//aqui es null porque como sabemos es un atributo que aun no existe sino hasta cuando se devuelva

    }

    public int getCodigo(){
        return (int) codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public EstadoArriendo getEstado() {
        return estado;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion=fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }

    public void addDetalleArriendo(Equipo equipo){
        DetalleArriendo detalle=new DetalleArriendo(equipo.getPrecioArriendoDia(),equipo,this);
        detalleArriendos.add(detalle);
    }



    public int getNumeroDiasArriendo(){
      Period periodo=Period.between(fechaInicio,fechaDevolucion);
      int dias=periodo.getDays();
      return dias+1;
      //dias sumamos 1 porque si se parte desde el dia presente, sera mi 0 mi primer dia y así sucesivamente//
    }

    public long getMontoTotal(){
        long monto=0;
        if (this.getEstado()== EstadoArriendo.INICIADO) {
            return monto;
        }
        //En esta parte un cliente puede tener muchos arriendos, por tanto cada detalle de arriendo (en su arraylist) con su respectivo precio aplicado deberá sumarse al monto
        for (DetalleArriendo detalle:detalleArriendos){
            monto=monto+detalle.getPrecioAplicado();
        }

        //para esta parte retornamos el monto para el caso de entregado, retorna monto de lo que le saldrá al cliente. Mientras que al devolverlo, se cobrará por los dias respectivos
        if (this.getEstado()==EstadoArriendo.ENTREGADO){
                return monto;
        }else{
            //else para decir que esta devuelto y que por tanto se cobrará segun los dias
            return monto*getNumeroDiasArriendo();
        }



    }



    public String[][]getDetallesToString(){
        //aqui pregunto no solo si detalleArriendo es null, sino tambien si es iniciado(tenor del proyecto word)
        if (detalleArriendos==null&&this.estado.equals(EstadoArriendo.INICIADO)){
            String vacio[][]=new String [0][0];
            return vacio;
        }



        String[][] detallesStr = new String[detalleArriendos.size()][3];
        int i = 0;
        for (DetalleArriendo detalle : detalleArriendos) {
                detallesStr[i][0] = String.valueOf(detalle.getEquipo().getCodigo());
                detallesStr[i][1] = detalle.getEquipo().getDescripcion();
                detallesStr[i][2] = String.valueOf(detalle.getPrecioAplicado());
                i++;
        }
        return detallesStr;



    }

    public Cliente getCliente(){
        return cliente;
    }



    public Equipo[]getEquipos(){
        Equipo[]equipos=new Equipo[detalleArriendos.size()];
        int i=0;
        for (DetalleArriendo detalle:detalleArriendos){
            equipos[i]=detalle.getEquipo();
            i++;
        }
        return equipos;



    }







}
