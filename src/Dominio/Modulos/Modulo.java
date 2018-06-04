package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.Estadisticas.EstadisticasModulo;
import Simulacion.Simulacion;

import java.util.PriorityQueue;
import java.util.Queue;

public abstract class Modulo {
    protected final Simulacion simulacion;
    protected Queue<Consulta> colaConsultas;
    protected final Modulo siguienteModulo;
    protected int numeroServidores;
    protected final EstadisticasModulo estadisticasModulo;

    public Modulo(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        this.simulacion = simulacion;
        this.siguienteModulo = siguienteModulo;
        this.numeroServidores = numeroServidores;

        estadisticasModulo = new EstadisticasModulo();
        colaConsultas = new PriorityQueue<>();
    }

    public abstract void procesarEntrada(Consulta consulta);

    public abstract void procesarSalida(Consulta consulta);

    // TODO: Se puede hacer aqui?
    public abstract void procesarTimeout(Consulta consulta);

    protected void pasoSiguienteModulo(Consulta consulta) {
        siguienteModulo.procesarEntrada(consulta);
    }

    public EstadisticasModulo getEstadisticasModulo() {
        return estadisticasModulo;
    }
}
