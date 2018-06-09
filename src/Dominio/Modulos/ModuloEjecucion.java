package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.ControladorSimulacion;

public class ModuloEjecucion extends Modulo {

    public ModuloEjecucion(ControladorSimulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
        // TODO
    }

    @Override
    protected void generarSalida(Consulta consulta) {
        // TODO
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        // TODO
        return 0;
    }
}
