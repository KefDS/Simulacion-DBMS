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


    public void liberarConexion() {
        numeroServidores++;
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
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
    }

    @Override
    public void procesarSalida(Consulta consulta) {
        // TODO
    }

    @Override
    protected void generarSalida(Consulta consulta) {
        // TODO
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        // TODO: No se usa, buscar mejor herencia
        return 0;
    }

    public void generarEntrada() {
        double tiempo = simulacion.getReloj() + ValoresAleatorios.generarValorDistibucionExponencial(lambda);
        Consulta consulta = new Consulta(this,
                ValoresAleatorios.generarTipoConsulta(),
                tiempo);
        simulacion.anadirEvento(new Evento(tiempo, this, TipoEvento.LLEGADA, consulta));
    }

    private void generarTimeout(Consulta consulta) {
        simulacion.anadirEvento(new Evento(
                simulacion.getReloj() + timeout,
                null,
                TipoEvento.TIMEOUT,
                consulta));
    }
}
