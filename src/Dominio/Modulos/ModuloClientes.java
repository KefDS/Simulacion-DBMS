package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.ControladorSimulacion;
import Simulacion.Enumeraciones.TipoEvento;
import Simulacion.Evento;
import Simulacion.ValoresAleatorios;

public class ModuloClientes extends Modulo {

    private final int timeout;
    private final double lambda;

    public ModuloClientes(ControladorSimulacion simulacion, int numeroServidores, int timeout) {
        super(simulacion, numeroServidores);
        this.timeout = timeout * 1000; // 1 segundos -> 1000 milisegundos
        lambda = 0.0005; // 30 conexiones por minuto -> 0.0005 conexiones por milisegundo
    }

    public void setSiguienteModulo(Modulo siguienteModulo) {
        this.siguienteModulo = siguienteModulo;
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
        // TODO: mejorar logica
        // Nota: Para no romper la interface, la entrada que le envia
        // modulo ejecucion sera procesada aqui tambien
        if (consulta.getNumeroBloques() == -1) {
            // Servidores disponibles?
            if (numeroServidores > 0) {
                numeroServidores--;
                generarTimeout(consulta);
                siguienteModulo.procesarEntrada(consulta);
            } else {
                // Se rechaza conexion
                simulacion.getEstadisticas().anadirConexionDescartada();
            }
            generarEntrada();
        } else {
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
        simulacion.getEstadisticas().anadirTiempoConsultaFinalizada(
                consulta.getEstadisticaConsulta().getTiempoDeVida(simulacion.getReloj()));
        // Borra evento TIMEOUT de la cola eventos
        simulacion.eliminarEvento(consulta);
        liberarConexion();
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        // TODO: Numero de bloques es discreto?
        return Math.round(((double) consulta.getNumeroBloques()) / 64) * 1000;
    }

    private void generarTimeout(Consulta consulta) {
        simulacion.anadirEvento(new Evento(
                simulacion.getReloj() + timeout,
                null,
                TipoEvento.TIMEOUT,
                consulta));
    }

    public void liberarConexion() {
        numeroServidores++;
    }
}
