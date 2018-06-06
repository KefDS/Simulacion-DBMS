package Dominio.Modulos;

import Dominio.Consulta;
import Dominio.Enumeraciones.TipoConsulta;
import Simulacion.Simulacion;

public class ModuloTransaccion extends Modulo {

    private boolean prioridadDDL;

    public ModuloTransaccion(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
        prioridadDDL = false;
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
        // TODO
    }

    @Override
    protected Consulta getSiguienteConsulta() {
        // TODO
        return new Consulta(null, TipoConsulta.SELECT, 0);
    }

    @Override
    protected void generarSalida(Consulta consulta) {
        // TODO
    }
}
