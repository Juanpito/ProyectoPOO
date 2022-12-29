package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//Parte de Fabian Bravo y Gonzalo Inostroza

public class Arriendo implements Serializable {
    private long codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private EstadoArriendo estado;
    private ArrayList<DetalleArriendo> detalles;
    private Cliente cliente;
    private ArrayList<Pago> pagos;

    public Arriendo(long codigo, LocalDate fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaDevolucion = null;
        this.cliente = cliente;
        this.detalles = new ArrayList<DetalleArriendo>();
        this.estado = EstadoArriendo.INICIADO;
        this.pagos = new ArrayList<>();
        cliente.addArriendo(this);
    }

    public int getCodigo() {
        return (int) codigo;
        // AAAAAAAAAAAA PORQUE LO GUARDAS COMO UN LONG PARA DESPUES MANDARLO COMO INT
        // NADA TIENE SENTIDO, ESTAS GASTANDO BITS DE MÄS AAAAAAAAAAAAAAAAAAAAAAAAa
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
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }

    public void addDetalleArriendo(Equipo equipo) {
        DetalleArriendo detalle = new DetalleArriendo(equipo.getPrecioArriendoDia(), equipo, this);
        detalles.add(detalle);
    }

    public int getNumeroDiasArriendo() {
        int dias;

        if(fechaDevolucion == null){
            return 0;
        }

        Period periodo = Period.between(fechaInicio, fechaDevolucion);
        //dias = ((fechaDevolucion.getYear() - fechaInicio.getYear()) * 365) + (fechaDevolucion.getDayOfYear() - fechaInicio.getDayOfYear());
        // Se le suma 1 en todos los casos, ya que si resulta en 0 dias, es 1 luego cuando ya pase un dia va a ser el primero (que se
        // contabilizo como 0 y el segundo)
        dias = periodo.getDays() + 1;

        return dias;
    }

    public long getMontoTotal() {
        // TODO
        long monto = 0;

        if (estado == EstadoArriendo.INICIADO) {
            return monto;
        }

        for(DetalleArriendo detalle : detalles){
            monto += detalle.getPrecioAplicado();
        }

        if (EstadoArriendo.ENTREGADO == estado) {
            return monto;
        } else {
            return monto * getNumeroDiasArriendo();
        }
    }

    public String[][] getDetallesToString() {
        String[][] detallesArriendo;
        Equipo equipo;

        // TODO: Porque && y no ||
        if(EstadoArriendo.INICIADO == this.estado && detalles.size() == 0){
            return new String[0][0];
        }

        detallesArriendo = new String[detalles.size()][4];

        for(int i = 0; i < detalles.size(); i++){
            equipo = detalles.get(i).getEquipo();
            detallesArriendo[i][0] = String.valueOf(equipo.getCodigo());
            detallesArriendo[i][1] = equipo.getDescripcion();
            detallesArriendo[i][2] = String.valueOf(detalles.get(i).getPrecioAplicado());
        }

        return detallesArriendo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Equipo[] getEquipos() {
        Equipo[] equipos = new Equipo[detalles.size()];
        for (int i=0; i<equipos.length; i++) {
            equipos[i] = detalles.get(i).getEquipo();
        }
        return equipos;
    }

    public void addPagoContado(Contado contado) {
        pagos.add(contado);
        if (getSaldoAdeudado() == 0) {
            estado = EstadoArriendo.PAGADO;
        }
    }

    public void addPagoDebito(Debito debito) {
        pagos.add(debito);
        if (getSaldoAdeudado() == 0) {
            estado = EstadoArriendo.PAGADO;
        }
    }

    public void addPagoCredito(Credito credito) {
        pagos.add(credito);
        if (getSaldoAdeudado() == 0) {
            estado = EstadoArriendo.PAGADO;
        }
    }

    public long getMontoPagado() {
        if (pagos.isEmpty()) {
            return 0;
        }
        return pagos.stream().
                map(Pago::getMonto).            // Mapear los valores del monto paa no trabajar con los objetos
                        reduce(0L, Long::sum);  // Sumar los valores
    }

    public long getSaldoAdeudado() {
        return getMontoTotal() - getMontoPagado();
    }

    public String[][] getPagosToString() {
        if (pagos.isEmpty()) {
            return new String[0][0];
        }

        String[][] resultado = new String[pagos.size()][3];
        for (int i=0; i<pagos.size(); i++) {
            Pago pago = pagos.get(i);
            resultado[i][0] = pago.getMonto() + "";
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            resultado[i][1] = pago.getFecha().format(formato);
            resultado[i][2] = pago.getClass().getSimpleName() + "";

        }
        return resultado;
    }
}

