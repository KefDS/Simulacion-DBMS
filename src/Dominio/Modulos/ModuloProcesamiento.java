package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.ControladorSimulacion;
import Simulacion.ValoresAleatorios;

public class ModuloProcesamiento extends Modulo {

    public ModuloProcesamiento(ControladorSimulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        return 100 +
                ValoresAleatorios.generarValorDistribucionUniforme(0, 1000) +
                ValoresAleatorios.generarValorDistribucionUniforme(0, 2000) +
                ValoresAleatorios.generarValorDistibucionExponencial(((double) 1) / 700) +
                (consulta.getTipoConsulta().esReadOnly() ? 100 : 250);
    }
}
