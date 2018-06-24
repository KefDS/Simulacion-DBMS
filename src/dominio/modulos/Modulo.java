package dominio.modulos;

import dominio.Consulta;
import javafx.util.Pair;
import simulacion.Simulacion;
import simulacion.enumeraciones.TipoEvento;
import simulacion.estadisticas.EstadisticasModulo;
import simulacion.Evento;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Modulo {
    protected final Simulacion simulacion;
    Queue<Consulta> colaConsultas;
    Modulo siguienteModulo;
    int numeroServidoresDisponibles;
    protected final int numeroServidoresTotales;
    protected EstadisticasModulo estadisticasModulo;

    public Modulo(Simulacion simulacion, int numeroServidores) {
        this.simulacion = simulacion;
        numeroServidoresTotales = numeroServidores;
        numeroServidoresDisponibles = numeroServidores;

        estadisticasModulo = new EstadisticasModulo();
        colaConsultas = new LinkedList<>();
    }

    public Modulo(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        this(simulacion, numeroServidores);
        this.siguienteModulo = siguienteModulo;
    }

    public void procesarEntrada(Consulta consulta) {
        consulta.getEstadisticaConsulta().setTiempoLlegadaModulo(simulacion.getReloj());
        consulta.setModuloActual(this);
        // Servidores disponibles?
        if (numeroServidoresDisponibles > 0) {
            numeroServidoresDisponibles--;
            generarSalida(consulta);
        } else {
            colaConsultas.add(consulta);
        }
    }

    public void procesarSalida(Consulta consulta) {
        // Anade tiempo de servicio
        estadisticasModulo.anadirTiempoServicio(consulta.getTipoConsulta(),
                consulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));

        finalizacionConsultaProcesada(consulta);
        siguienteConsulta();
    }

    private void siguienteConsulta() {
        // Hay clientes esperando en fila?
        Consulta siguienteConsulta = getSiguienteConsulta();
        if (siguienteConsulta != null) {
            estadisticasModulo.anadirTiempoClienteEnCola(
                    siguienteConsulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));
            generarSalida(siguienteConsulta);
        } else {
            numeroServidoresDisponibles++;
        }
    }

    private void finalizacionConsultaProcesada(Consulta consulta) {
        if (consulta.isTimeout()) {
            terminarConsulta(consulta);
        } else {
            siguienteModulo.procesarEntrada(consulta);
        }
    }

    protected void generarSalida(Consulta consulta) {
        double tiempo = getTiempoSalida(consulta);
        simulacion.anadirEvento(new Evento(
                simulacion.getReloj() + tiempo,
                this,
                TipoEvento.SALIDA,
                consulta
        ));
    }

    protected abstract double getTiempoSalida(Consulta consulta);

    protected Consulta getSiguienteConsulta() {
        return colaConsultas.poll();
    }

    public void procesarTimeout(Consulta consulta) {
        // Esta en la cola, o ya esta siendo atendido?
        if ((colaConsultas.remove(consulta))) {
            terminarConsulta(consulta);
        } else {
            // Cuando sea procesada la salida, se sacara del sistema
            consulta.seVencioTimeout();
        }
    }

    protected void terminarConsulta(Consulta consulta) {
        simulacion.getEstadisticas().anadirNumeroConexionesExpiradas();
        simulacion.getEstadisticas().anadirTiempoConsultaFinalizada(
                consulta.getEstadisticaConsulta().getTiempoDeVida(simulacion.getReloj()));
        simulacion.liberarConexion();
    }

    public EstadisticasModulo getEstadisticasModulo() {
        return estadisticasModulo;
    }

    public void limpiarModulo() {
        colaConsultas.clear();
        estadisticasModulo = new EstadisticasModulo();
        numeroServidoresDisponibles = numeroServidoresTotales;
    }

    public Pair<Integer, Integer> datosActuales() {
        // Pair<Consultas siendo atendidas, Tamano de la cola>
        return new Pair<>(numeroServidoresTotales - numeroServidoresDisponibles, colaConsultas.size());
    }
}
