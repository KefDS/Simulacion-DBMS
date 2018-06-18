package simulacion;

import dominio.enumeraciones.TipoConsulta;
import dominio.enumeraciones.TipoModulo;
import interfazGrafica.interfaces.Observable;
import interfazGrafica.interfaces.Observer;
import javafx.util.Pair;
import simulacion.estadisticas.Resultados;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import simulacion.estadisticas.ResultadosFinales;

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

    public ResultadosFinales realizarEjecucciones() {
        for (int i = 0; i < veces; i++) {
            ultimosResultados = simulacion.realizarSimulacion();

            this.resultados.add(ultimosResultados);
            numeroEjecuccion = i + 1;
            observersQueue.forEach(observer -> observer.notify(this));
        }
        return new ResultadosFinales(getPromediosTodasEjecuciones(), getIntervaloConfianzaTiempoVidaConexion());
    }

    public Resultados getUltimosResultados() {
        return ultimosResultados;
    }

    public int getNumeroEjecuccion() {
        return numeroEjecuccion;
    }

    public Pair<Double, Double> getIntervaloConfianzaTiempoVidaConexion() {
        SummaryStatistics estadistica = new SummaryStatistics();
        resultados.forEach(resultado -> estadistica.addValue(resultado.tiempoPromedioVidaConexion));

        TDistribution tDist = new TDistribution(estadistica.getN() - 1);
        // Valor en tabla t
        double critVal = tDist.inverseCumulativeProbability(1.0 - (1 - 0.95) / 2);

        double intervaloConfianza = critVal * estadistica.getStandardDeviation() / Math.sqrt(estadistica.getN());
        return new Pair<>(estadistica.getMean() - intervaloConfianza, estadistica.getMean() + intervaloConfianza);
    }

    public Resultados getPromediosTodasEjecuciones() {
        Map<TipoModulo, Integer> tamanoPromColaAcm = Arrays.stream(TipoModulo.values())
                .collect(Collectors.toMap(Function.identity(), k -> 0));

        Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioAcm = new EnumMap<>(TipoModulo.class);
        Arrays.stream(TipoModulo.values()).forEach(modulo -> tiempoPromedioAcm.put(modulo, Arrays.stream(TipoConsulta.values())
                .collect(Collectors.toMap(Function.identity(), d -> 0.0))));

        double promedioPromediosVidaConexion = 0;
        int numeroConexionesDescartadas = 0;
        int numeroConexionesCompletadas = 0;
        int numeroConexionesExpiradas = 0;

        for (Resultados resultado : resultados) {
            promedioPromediosVidaConexion += resultado.tiempoPromedioVidaConexion;
            numeroConexionesDescartadas += resultado.numeroConexionesDescartadas;
            numeroConexionesCompletadas += resultado.numeroConexionesCompletadas;
            numeroConexionesExpiradas += resultado.numeroConexionesExpiradas;

            resultado.tamanoPromedioCola.forEach((k, v) -> tamanoPromColaAcm.put(k, tamanoPromColaAcm.get(k) + v));

            resultado.tiempoPromedioPorTipoConsultas.forEach((modulo, map) ->
                    map.forEach((consulta, valor) -> {
                        Map<TipoConsulta, Double> mapAcm = tiempoPromedioAcm.get(modulo);
                        mapAcm.put(consulta, mapAcm.get(consulta) + valor);
                    }));
        }

        promedioPromediosVidaConexion /= resultados.size();
        numeroConexionesDescartadas /= resultados.size();
        numeroConexionesCompletadas /= resultados.size();
        numeroConexionesExpiradas /= resultados.size();

        tamanoPromColaAcm.forEach((k, v) -> tamanoPromColaAcm.put(k, v / resultados.size()));
        tiempoPromedioAcm.forEach((modulo, map) -> map.forEach((consulta, map2) -> map.put(consulta, map2 / resultados.size())));

        return new Resultados(numeroConexionesCompletadas, numeroConexionesDescartadas,
                numeroConexionesExpiradas, promedioPromediosVidaConexion,
                tamanoPromColaAcm, tiempoPromedioAcm);
    }

    @Override
    public void addObserver(Observer obs) {
        observersQueue.add(obs);
    }
}
