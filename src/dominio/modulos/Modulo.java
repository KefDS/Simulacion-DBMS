package dominio.modulos;

import dominio.Consulta;
import simulacion.Simulacion;
import simulacion.enumeraciones.TipoEvento;
import simulacion.estadisticas.EstadisticasModulo;
import simulacion.Evento;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Modulo {
    protected final Simulacion simulacion;
    protected Queue<Consulta> colaConsultas;
    Modulo siguienteModulo;
    int numeroServidores;
    private EstadisticasModulo estadisticasModulo;

    public Modulo(Simulacion simulacion, int numeroServidores) {
        this.simulacion = simulacion;
        this.numeroServidores = numeroServidores;

        estadisticasModulo = new EstadisticasModulo();
        colaConsultas = new LinkedList<>();
    }

    public Modulo(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        this(simulacion, numeroServidores);
        this.siguienteModulo = siguienteModulo;
    }

    public void procesarEntrada(Consulta consulta) {
        consulta.getEstadisticaConsulta().setTiempoLlegadaModulo(simulacion.getReloj());
        // Servidores disponibles?
        if (numeroServidores > 0) {
            numeroServidores--;
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
            numeroServidores++;
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

    private void terminarConsulta(Consulta consulta) {
        simulacion.getEstadisticas().anadirConexionDescartada();
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
    }
}
