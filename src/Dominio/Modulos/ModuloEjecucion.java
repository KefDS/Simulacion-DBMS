package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Simulacion;

public class ModuloEjecucion extends Modulo {

    public ModuloEjecucion(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
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
}
