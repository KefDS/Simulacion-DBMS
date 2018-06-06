package Simulacion.Estadisticas;

public class EstadisticaConsulta {
    private final double tiempoInicial;
    private double tiempoLlegadaModulo;

    public EstadisticaConsulta(double tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public double getTiempoDeVida(double tiempo) {
        return tiempo - tiempoInicial;
    }

    public double getTiempoDesdeLlegadaModulo(double tiempo) {
        return tiempo - tiempoLlegadaModulo;
    }

    public void setTiempoLlegadaModulo(double tiempoLlegadaModulo) {
        this.tiempoLlegadaModulo = tiempoLlegadaModulo;
    }
}
