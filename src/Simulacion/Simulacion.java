package Simulacion;

import Dominio.Modulos.Modulo;
import Dominio.Enumeraciones.TipoMudulo;
import Simulacion.Estadisticas.Estadistica;
import Simulacion.Estadisticas.Estadisticas;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Simulacion {
    // TODO: GUI
    private double reloj;
    private Queue<Evento> colaEventos;
    private Map<TipoMudulo, Modulo> modulos;
    private Estadisticas estadisticas;

    public Simulacion() {
        reloj = 0;
        colaEventos = new PriorityQueue<Evento>();
        estadisticas = new Estadisticas();
        // TODO: Inicializar modulos
    }

    public Estadistica iniciarSimulacion(int veces, int tiempoTotal,
                                         int maximasConexiones,
                                         int numeroDeProcesos,
                                         int numConsultasSimultaneas) {
        // TODO
        return null;
    }

    public double getReloj() {
        return reloj;
    }

    public void anadirEvento(Evento evento) {
        // TODO
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }
}
