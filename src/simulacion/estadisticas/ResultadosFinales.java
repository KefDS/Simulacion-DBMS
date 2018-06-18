package simulacion.estadisticas;

import javafx.util.Pair;

public class ResultadosFinales extends Resultados {
    public final double interavaloConfianzaPiso;
    public final double getInteravaloConfianzaTecho;

    public ResultadosFinales(Resultados resultados, Pair<Double, Double> intevaloConfianza) {
        super(resultados.numeroConexionesCompletadas, resultados.numeroConexionesDescartadas,
                resultados.numeroConexionesExpiradas, resultados.tiempoPromedioVidaConexion,
                resultados.tamanoPromedioCola, resultados.tiempoPromedioPorTipoConsultas);
        this.interavaloConfianzaPiso = intevaloConfianza.getKey();
        this.getInteravaloConfianzaTecho = intevaloConfianza.getValue();
    }
}
