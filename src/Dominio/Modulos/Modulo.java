package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Estadisticas.EstadisticasModulo;
import Simulacion.Simulacion;

import java.util.PriorityQueue;
import java.util.Queue;

public abstract class Modulo {
    protected final Simulacion simulacion;
    protected final Queue<Consulta> colaConsultas;
    protected Modulo siguienteModulo;
    protected int numeroServidores;
    protected EstadisticasModulo estadisticasModulo;

    public Modulo(Simulacion simulacion, int numeroServidores) {
        this.simulacion = simulacion;
        this.numeroServidores = numeroServidores;

        estadisticasModulo = new EstadisticasModulo();
        colaConsultas = new PriorityQueue<>();
    }

    public Modulo(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        this(simulacion, numeroServidores);
        this.siguienteModulo = siguienteModulo;
    }

    public abstract void procesarEntrada(Consulta consulta);

    public void procesarSalida(Consulta consulta) {
        // Anade tiempo de servicio
        estadisticasModulo.anadirTiempoServicio(consulta.getTipoConsulta(),
                consulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));

        if (consulta.isTimeout()) {
            terminarConsulta(consulta);
        } else {
            siguienteModulo.procesarEntrada(consulta);
        }

        // Hay clientes esperando?
        Consulta siguienteConsulta = getSiguienteConsulta();
        if (siguienteConsulta != null) {
            estadisticasModulo.anadirTiempoClienteEnCola(
                    siguienteConsulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));

            generarSalida(siguienteConsulta);
        } else {
            numeroServidores++;
        }
    }

    protected abstract void generarSalida(Consulta consulta);

    protected Consulta getSiguienteConsulta() {
        return colaConsultas.poll();
    }

    public void procesarTimeout(Consulta consulta) {
        // Esta en la cola, o ya esta siendo atendido?
        if ((colaConsultas.remove(consulta))) {
            terminarConsulta(consulta);
        } else {
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
