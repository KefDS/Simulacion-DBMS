package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Simulacion;

public class ModuloClientes extends Modulo {

    private final int timeout;

    public ModuloClientes(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores, int timeout) {
        super(simulacion, siguienteModulo, numeroServidores);
        this.timeout = timeout;
    }

    public void generarPrimerEvento() {
        // TODO
    }

    public void cerrarConexion(int R) {
        // TODO: Pensar acerca de este metodo
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
