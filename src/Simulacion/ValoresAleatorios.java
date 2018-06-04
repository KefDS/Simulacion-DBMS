package Simulacion;

import Dominio.Enumeraciones.TipoConsulta;

import java.util.EnumMap;
import java.util.Map;

public class ValoresAleatorios {
    private static final Map<TipoConsulta, Double> distribucionTipoConsulta = new EnumMap<>(TipoConsulta.class);

    public static double generarValorDistribucionNormal(int varianza, int media) {
        // TODO
        return 0;
    }

    public static double generarValorDistribucionUniforme(int inicioIntervalo, int finalIntervalo) {
        // TODO
        return 0;
    }

    public static double generarValorDistibucionExponencial(int lambda) {
        // TODO
        return 0;
    }

    public static TipoConsulta generarTipoConsulta() {
        // TODO
        return TipoConsulta.SELECT;
    }
}
