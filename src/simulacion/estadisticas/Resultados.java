package simulacion.estadisticas;

import dominio.enumeraciones.TipoConsulta;
import dominio.enumeraciones.TipoModulo;

import java.util.Map;

public class Resultados {
    public final Map<TipoModulo, Integer> tamanoPromedioCola;
    public final double tiempoPromedioVidaConexion;
    public final int numeroConexionesDescartadas;
    public final Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioPorTipoConsultas;

    public Resultados(Map<TipoModulo, Integer> tamanoPromedioCola,
                      double tiempoPromedioVidaConexion,
                      int numeroConexionesDescartadas,
                      Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioPorTipoConsultas) {
        this.tamanoPromedioCola = tamanoPromedioCola;
        this.tiempoPromedioVidaConexion = tiempoPromedioVidaConexion;
        this.numeroConexionesDescartadas = numeroConexionesDescartadas;
        this.tiempoPromedioPorTipoConsultas = tiempoPromedioPorTipoConsultas;
    }
}
