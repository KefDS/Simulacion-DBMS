package dominio.modulos;

import dominio.Consulta;
import dominio.enumeraciones.TipoConsulta;
import simulacion.Simulacion;

public class ModuloEjecucion extends Modulo {

    public ModuloEjecucion(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        double tiempo = Math.pow(consulta.getNumeroBloques(), 2);
        if (consulta.getTipoConsulta() == TipoConsulta.DDL) {
            tiempo += 500;
        }
        else if (consulta.getTipoConsulta() == TipoConsulta.UPDATE) {
            tiempo += 1000;
        }
        return tiempo;
    }
}
