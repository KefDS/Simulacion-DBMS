package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Simulacion;

public class ModuloProcesamiento extends Modulo {

    public ModuloProcesamiento(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
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
