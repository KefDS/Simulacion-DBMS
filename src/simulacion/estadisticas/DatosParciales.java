package simulacion.estadisticas;

import dominio.enumeraciones.TipoModulo;
import javafx.util.Pair;

import java.util.Map;

public class DatosParciales {
    public final double reloj;
    public final int numeroConexionesDescartadas;
    public final int numeroConexionesExpiradas;
    public final int numeroConexionesCompletadas;
    public final Map<TipoModulo, Pair<Integer, Integer>> infoModulo;

    public DatosParciales(double reloj, int numeroConexionesCompletadas, int numeroConexionesDescartadas,
                          int numeroConexionesExpiradas, Map<TipoModulo, Pair<Integer, Integer>> infoModulo) {
        this.reloj = reloj;
        this.numeroConexionesCompletadas = numeroConexionesCompletadas;
        this.numeroConexionesDescartadas = numeroConexionesDescartadas;
        this.numeroConexionesExpiradas = numeroConexionesExpiradas;
        this.infoModulo = infoModulo;
    }
}
