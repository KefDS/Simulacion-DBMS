package dominio.modulos;

import dominio.Consulta;
import simulacion.Simulacion;
import simulacion.enumeraciones.TipoEvento;
import simulacion.Evento;
import simulacion.ValoresAleatorios;

public class ModuloClientes extends Modulo {

    private final int timeout;
    private final double lambda;

    public ModuloClientes(Simulacion simulacion, int numeroServidores, int timeout) {
        super(simulacion, numeroServidores);
        this.timeout = timeout * 1000; // 1 segundos -> 1000 milisegundos
        lambda = 0.0005; // 30 conexiones por minuto -> 0.0005 conexiones por milisegundo
    }

    public void setSiguienteModulo(Modulo siguienteModulo) {
        this.siguienteModulo = siguienteModulo;
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
        // Nota: Para no romper la interface, la entrada que le envia
        // modulo ejecucion sera procesada aqui tambien
        if (consulta.getNumeroBloques() == -1) {
            // Servidores disponibles?
            if (numeroServidoresDisponibles > 0) {
                numeroServidoresDisponibles--;
                generarTimeout(consulta);
                siguienteModulo.procesarEntrada(consulta);
            } else {
                // Se rechaza conexion
                simulacion.getEstadisticas().anadirConexionDescartada();
            }
            generarEntrada();
        } else {
            consulta.getEstadisticaConsulta().setTiempoLlegadaModulo(simulacion.getReloj());
            consulta.setModuloActual(this);
            generarSalida(consulta);
        }
    }

    public void generarEntrada() {
        double tiempo = simulacion.getReloj() + ValoresAleatorios.generarValorDistibucionExponencial(lambda);
        Consulta consulta = new Consulta(this,
                ValoresAleatorios.generarTipoConsulta(),
                tiempo);
        simulacion.anadirEvento(new Evento(tiempo, this, TipoEvento.LLEGADA, consulta));
    }

    @Override
    public void procesarSalida(Consulta consulta) {
        estadisticasModulo.anadirTiempoServicio(consulta.getTipoConsulta(),
                consulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));
        simulacion.getEstadisticas().anadirNumeroConexionesCompletadas();
        simulacion.getEstadisticas().anadirTiempoConsultaFinalizada(
                consulta.getEstadisticaConsulta().getTiempoDeVida(simulacion.getReloj()));
        // Borra evento TIMEOUT de la cola eventos
        simulacion.eliminarEvento(consulta);
        liberarConexion();
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        return (double) consulta.getNumeroBloques() / 64 * 1000;
    }

    private void generarTimeout(Consulta consulta) {
        simulacion.anadirEvento(new Evento(
                simulacion.getReloj() + timeout,
                null,
                TipoEvento.TIMEOUT,
                consulta));
    }

    public void liberarConexion() {
        numeroServidoresDisponibles++;
    }
}
