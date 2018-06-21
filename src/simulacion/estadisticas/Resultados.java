package simulacion.estadisticas;

import dominio.enumeraciones.TipoConsulta;
import dominio.enumeraciones.TipoModulo;

import java.util.Map;

public class Resultados {
    public final Map<TipoModulo, Double> tamanoPromedioCola;
    public final double tiempoPromedioVidaConexion;
    public final int numeroConexionesDescartadas;
    public final int numeroConexionesCompletadas;
    public final int numeroConexionesExpiradas;
    public final Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioPorTipoConsultas;

    public Resultados(int numeroConexionesCompletadas, int numeroConexionesDescartadas,
                      int numeroConexionesExpiradas, double tiempoPromedioVidaConexion,
                      Map<TipoModulo, Double> tamanoPromedioCola,
                      Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioPorTipoConsultas) {
        this.tamanoPromedioCola = tamanoPromedioCola;
        this.tiempoPromedioVidaConexion = tiempoPromedioVidaConexion;
        this.numeroConexionesCompletadas = numeroConexionesCompletadas;
        this.numeroConexionesDescartadas = numeroConexionesDescartadas;
        this.numeroConexionesExpiradas = numeroConexionesExpiradas;
        this.tiempoPromedioPorTipoConsultas = tiempoPromedioPorTipoConsultas;
    }
}
