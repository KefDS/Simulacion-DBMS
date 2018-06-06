package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Estadisticas.EstadisticasModulo;
import Simulacion.Simulacion;

public class ModuloClientes extends Modulo {

    private final int timeout;

    public ModuloClientes(Simulacion simulacion, int numeroServidores, int timeout) {
        super(simulacion, numeroServidores);
        this.timeout = timeout;
    }

    public void setSiguienteModulo(Modulo siguienteModulo) {
        this.siguienteModulo = siguienteModulo;
    }

    public void generarPrimerEvento() {
        // TODO
    }

    public void liberarConexion() {
        numeroServidores++;
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
        // TODO
    }

    @Override
    public void procesarSalida(Consulta consulta) {
        // TODO
    }

    @Override
    protected void generarSalida(Consulta consulta) {
        // TODO
    }
}
