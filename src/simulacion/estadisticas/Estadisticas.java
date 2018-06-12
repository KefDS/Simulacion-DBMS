package simulacion.estadisticas;

import dominio.enumeraciones.TipoConsulta;
import dominio.enumeraciones.TipoModulo;
import simulacion.PromedioTiempo;

import java.util.*;
import java.util.stream.Collectors;

public class Estadisticas {
    private double lambda;
    private PromedioTiempo promedioVidaConexion;
    private int numeroConexionesDescartadas;
    private int numeroConexionesExpiradas;
    private int numeroConexionesCompletadas;

    public Estadisticas() {
        lambda = 0.0005;
        promedioVidaConexion = new PromedioTiempo();
        numeroConexionesDescartadas = 0;
        numeroConexionesExpiradas = 0;
        numeroConexionesCompletadas = 0;
    }

    public void anadirTiempoConsultaFinalizada(double tiempo) {
        promedioVidaConexion.anadirTiempoAcumulado(tiempo);
    }

    public void anadirConexionDescartada() {
        numeroConexionesDescartadas++;
    }

    public int getNumeroConexionesDescartadas() {
        return numeroConexionesDescartadas;
    }

    public int getNumeroConexionesExpiradas() {
        return numeroConexionesExpiradas;
    }

    public void anadirNumeroConexionesExpiradas() {
        this.numeroConexionesExpiradas++;
    }

    public int getNumeroConexionesCompletadas() {
        return numeroConexionesCompletadas;
    }

    public void anadirNumeroConexionesCompletadas() {
        this.numeroConexionesCompletadas++;
    }

    public Resultados obtenerResultados(Map<TipoModulo, EstadisticasModulo> estadisticasModulos) {
        // TODO: Tamano promedio en cola se debe dar en numero entero?
        Map<TipoModulo, Integer> tamanoPromedioCola = estadisticasModulos.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (int) Math.ceil(entry.getValue().sacarTiempoPromedioCola() * lambda)
                ));

        Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioConsultaPorModulo = new EnumMap<>(TipoModulo.class);
        Arrays.stream(TipoModulo.values()).forEach(tipo ->
                tiempoPromedioConsultaPorModulo.put(tipo, estadisticasModulos.get(tipo).sacarTiempoServicioTipoConsulta()));

        return new Resultados(tamanoPromedioCola,
                promedioVidaConexion.getPromedio(),
                getNumeroConexionesDescartadas(),
                tiempoPromedioConsultaPorModulo);
    }
}
