package interfazGrafica.observadores;

import interfazGrafica.Log;
import interfazGrafica.interfaces.Observer;
import simulacion.EjecutorSimulacion;
import simulacion.estadisticas.Resultados;

public class LogFinalEjecucion implements Observer {
    private final Log log;

    public LogFinalEjecucion(Log log) {
        this.log = log;
    }

    @Override
    public void notify(Object data) {
        Resultados resultados = ((EjecutorSimulacion) data).getUltimosResultados();
        log.escribirResultadosFinales(resultados);
    }
}
