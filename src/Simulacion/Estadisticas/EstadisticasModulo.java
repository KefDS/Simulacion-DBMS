package Simulacion.Estadisticas;

import Dominio.Enumeraciones.TipoConsulta;
import Simulacion.PromedioTiempo;

import java.util.EnumMap;
import java.util.Map;

public class EstadisticasModulo {
    private PromedioTiempo promedioTiempoEnCola;
    private Map<TipoConsulta, PromedioTiempo> promedioColaPorTipoSentencia;

    public EstadisticasModulo() {
        promedioTiempoEnCola = new PromedioTiempo();
        promedioColaPorTipoSentencia = new EnumMap<>(TipoConsulta.class);
        // TODO: Revisar
        promedioColaPorTipoSentencia.entrySet().forEach(entry -> entry.setValue(new PromedioTiempo()));
    }

    public void anadirTiempoClienteEnCola(double tiempo) {
        promedioTiempoEnCola.anadirTiempoAcumulado(tiempo);
    }

    public void anadirTiempoServicio(TipoConsulta tipo, double tiempo) {
        promedioColaPorTipoSentencia.get(tipo).anadirTiempoAcumulado(tiempo);
    }

    public double sacarTiempoPromedioCola() {
        return promedioTiempoEnCola.getPromedio();
    }

    public Map<TipoConsulta, Double> sacarTiempoServicioTipoConsulta() {
        EnumMap<TipoConsulta, Double> tablaTiempos = new EnumMap<>(TipoConsulta.class);
        promedioColaPorTipoSentencia.entrySet().forEach(entry ->
                tablaTiempos.put(entry.getKey(), entry.getValue().getPromedio()));
        return tablaTiempos;
    }
}
