package simulacion.estadisticas;

import dominio.enumeraciones.TipoMudulo;
import javafx.util.Pair;

import java.util.Map;

public class DatosParciales {
    public final double reloj;
    public final int numeroConexionesDescartadas;
    public final int getNumeroConexionesExpiradas;
    public final int numeroConexionesCompletadas;
    public final Map<TipoMudulo, Pair<Integer, Integer>> infoModulo;

    public DatosParciales(double reloj, int numeroConexionesCompletadas, int numeroConexionesDescartadas,
                          int numeroConexionesExpiradas, Map<TipoMudulo, Pair<Integer, Integer>> infoModulo) {
        this.reloj = reloj;
        this.numeroConexionesCompletadas = numeroConexionesCompletadas;
        this.numeroConexionesDescartadas = numeroConexionesDescartadas;
        this.getNumeroConexionesExpiradas = numeroConexionesExpiradas;
        this.infoModulo = infoModulo;
    }
}
