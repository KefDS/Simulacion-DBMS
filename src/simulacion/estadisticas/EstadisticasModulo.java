package simulacion.estadisticas;

import dominio.enumeraciones.TipoConsulta;
import simulacion.PromedioTiempo;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EstadisticasModulo {
    private PromedioTiempo promedioTiempoEnCola;
    private Map<TipoConsulta, PromedioTiempo> promedioTiempoPorTipoSentencia;

    public EstadisticasModulo() {
        promedioTiempoEnCola = new PromedioTiempo();
        promedioTiempoPorTipoSentencia = new EnumMap<>(TipoConsulta.class);
        Stream.of(TipoConsulta.values()).forEach(tipoConsulta ->
                promedioTiempoPorTipoSentencia.put(tipoConsulta, new PromedioTiempo()));
    }

    public void anadirTiempoClienteEnCola(double tiempo) {
        promedioTiempoEnCola.anadirTiempoAcumulado(tiempo);
    }

    public void anadirTiempoServicio(TipoConsulta tipo, double tiempo) {
        promedioTiempoPorTipoSentencia.get(tipo).anadirTiempoAcumulado(tiempo);
    }

    public double sacarTiempoPromedioCola() {
        return promedioTiempoEnCola.getPromedio();
    }

    public Map<TipoConsulta, Double> sacarTiempoServicioTipoConsulta() {
        return promedioTiempoPorTipoSentencia.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getPromedio()
        ));
    }
}
