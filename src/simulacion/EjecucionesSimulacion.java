package simulacion;

import javafx.util.Pair;
import simulacion.estadisticas.Resultados;

import java.util.LinkedList;
import java.util.List;

public class EjecucionesSimulacion {
    private final Simulacion simulacion;
    private List<Resultados> resultados;
    private final int veces;

    public EjecucionesSimulacion(int veces, int tiempoTotal, int conexionesMaximas, int timeout,
                          int servidoresProcesamiento, int servidoresTransaccion,
                          int servidoresEjecuccion, boolean modoLento) {
        this.veces = veces;
        simulacion = new Simulacion(tiempoTotal, conexionesMaximas, timeout,
                servidoresProcesamiento, servidoresTransaccion,
                servidoresEjecuccion, modoLento);
        resultados = new LinkedList<>();
    }

    public Pair<Resultados, Double> empezarEjecucciones() {
        for(int i = 0; i < veces; i++) {
            resultados.add(simulacion.iniciarSimulacion());
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
}
