package simulacion;

import interfazGrafica.interfaces.Observable;
import interfazGrafica.interfaces.Observer;
import javafx.util.Pair;
import simulacion.estadisticas.Resultados;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EjecutorSimulacion implements Observable {
    private final Simulacion simulacion;
    private List<Resultados> resultados;
    private final int veces;
    private final Queue<Observer> observersQueue;
    private Resultados ultimosResultados;
    private int numeroEjecuccion;

    public EjecutorSimulacion(int veces, int tiempoTotal, int conexionesMaximas, int timeout,
                              int servidoresProcesamiento, int servidoresTransaccion,
                              int servidoresEjecuccion, boolean modoLento) {
        this.veces = veces;
        simulacion = new Simulacion(tiempoTotal, conexionesMaximas, timeout,
                servidoresProcesamiento, servidoresTransaccion,
                servidoresEjecuccion, modoLento);
        resultados = new LinkedList<>();
        observersQueue = new LinkedList<>();
        numeroEjecuccion = 0;
    }

    public Simulacion getSimulacion() {
        return simulacion;
    }

    public Pair<Resultados, Double> realizarEjecucciones() {
        for(int i = 0; i < veces; i++) {
            ultimosResultados = simulacion.realizarSimulacion();

            this.resultados.add(ultimosResultados);
            numeroEjecuccion = i + 1;
            observersQueue.forEach(observer -> observer.notify(this));
        }
        // TODO: Intervalo de confianza
        return new Pair<>(getPromediosTodasEjecuciones(), 2.0);
    }

    public Resultados getUltimosResultados() {
        return ultimosResultados;
    }

    public int getNumeroEjecuccion() {
        return numeroEjecuccion;
    }

    public double getIntervaloConfianzaTiempoVidaConexion() {
        // TODO
        return 0;
    }

    public Resultados getPromediosTodasEjecuciones() {
        // TODO
        return null;
    }

    @Override
    public void addObserver(Observer obs) {
        observersQueue.add(obs);
    }
}
