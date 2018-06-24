package interfazGrafica.observadores;

import interfazGrafica.Log;
import interfazGrafica.interfaces.Observer;
import simulacion.Simulacion;
import simulacion.estadisticas.DatosParciales;

public class LogFinalTick implements Observer {
    private final Log log;

    public LogFinalTick(Log log) {
        this.log = log;
    }

    @Override
    public void notify(Object data) {
        DatosParciales datosParciales = ((Simulacion) data).getResultadosParcialesMasRecientes();
        log.escribirDatosParciales(datosParciales);
    }
}
