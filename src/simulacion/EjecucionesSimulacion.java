package simulacion;

import interfazGrafica.interfaces.Observable;
import interfazGrafica.interfaces.Observer;
import javafx.util.Pair;
import simulacion.estadisticas.Resultados;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EjecucionesSimulacion implements Observable {
    private final Simulacion simulacion;
    private List<Resultados> resultados;
    private final int veces;
    private final Queue<Observer> observersQueue;

    public EjecucionesSimulacion(int veces, int tiempoTotal, int conexionesMaximas, int timeout,
                          int servidoresProcesamiento, int servidoresTransaccion,
                          int servidoresEjecuccion, boolean modoLento) {
        this.veces = veces;
        simulacion = new Simulacion(tiempoTotal, conexionesMaximas, timeout,
                servidoresProcesamiento, servidoresTransaccion,
                servidoresEjecuccion, modoLento);
        resultados = new LinkedList<>();
        observersQueue = new LinkedList<>();
    }

    public Simulacion getSimulacion() {
        return simulacion;
    }

    public Pair<Resultados, Double> empezarEjecucciones() {
        for(int i = 0; i < veces; i++) {
            Resultados resultados = simulacion.iniciarSimulacion();
            observersQueue.forEach(observer -> observer.notify(resultados));
            this.resultados.add(resultados);
        }
        // TODO: Intervalo de confianza
        return new Pair<>(getPromediosTodasEjecuciones(), 0.0);
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
