package simulacion;

import dominio.enumeraciones.TipoConsulta;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class ValoresAleatorios {
    private static final Map<TipoConsulta, Double> distribucionTipoConsulta = new EnumMap<>(TipoConsulta.class);

    static {
        distribucionTipoConsulta.put(TipoConsulta.SELECT, 0.30);
        distribucionTipoConsulta.put(TipoConsulta.UPDATE, 0.55);
        distribucionTipoConsulta.put(TipoConsulta.JOIN, 0.90);
        distribucionTipoConsulta.put(TipoConsulta.DDL, (double) 1);
    }

    private static Random rand = new Random();

    public static double generarValorDistribucionNormal(int media, double desviacionEstandar) {
        return Math.abs(rand.nextGaussian() * media + desviacionEstandar);
    }

    public static double generarValorDistribucionUniforme(int inicioIntervalo, int finalIntervalo) {
        return (finalIntervalo - inicioIntervalo) * rand.nextDouble() + inicioIntervalo;
    }

    public static double generarValorDistibucionExponencial(double lambda) {
        return Math.log(1 - rand.nextDouble()) / -lambda;
    }

    public static TipoConsulta generarTipoConsulta() {
        double random = rand.nextDouble();
        for (Map.Entry<TipoConsulta, Double> entrada :
                distribucionTipoConsulta.entrySet()) {
            if (random < entrada.getValue()) return entrada.getKey();
        }
        return TipoConsulta.SELECT;
    }
}
