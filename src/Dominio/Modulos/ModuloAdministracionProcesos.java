package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Simulacion;

public class ModuloAdministracionProcesos extends Modulo {

    public ModuloAdministracionProcesos(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
    }

    @Override
    public void procesarEntrada(Consulta consulta) {

    }

    @Override
    public void procesarSalida(Consulta consulta) {

    }

    @Override
    public void procesarTimeout(Consulta consulta) {

    }
}
