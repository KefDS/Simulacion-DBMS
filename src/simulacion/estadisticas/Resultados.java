package simulacion.estadisticas;

import dominio.enumeraciones.TipoConsulta;
import dominio.modulos.Modulo;
import dominio.enumeraciones.TipoMudulo;

import java.util.Map;

public class Resultados {
    public final Map<Modulo, Integer> tiempoPromedioCola;
    public final double tiempoPromedioVidaConexion;
    public final int numeroConexionesDescartadas;
    public final Map<TipoConsulta, Map<TipoMudulo, Double>> tiempoPromedioPorTipoConsultas;

    public Resultados(Map<Modulo, Integer> tiempoPromedioCola,
                      double tiempoPromedioVidaConexion,
                      int numeroConexionesDescartadas,
                      Map<TipoConsulta, Map<TipoMudulo, Double>> tiempoPromedioPorTipoConsultas) {
        this.tiempoPromedioCola = tiempoPromedioCola;
        this.tiempoPromedioVidaConexion = tiempoPromedioVidaConexion;
        this.numeroConexionesDescartadas = numeroConexionesDescartadas;
        this.tiempoPromedioPorTipoConsultas = tiempoPromedioPorTipoConsultas;
    }
}
