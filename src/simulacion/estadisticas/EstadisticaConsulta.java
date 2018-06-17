package simulacion.estadisticas;

public class EstadisticaConsulta {
    private final double tiempoInicial;

    private double tiempoLlegadaModulo;

    public EstadisticaConsulta(double tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public void setTiempoLlegadaModulo(double tiempoLlegadaModulo) { this.tiempoLlegadaModulo = tiempoLlegadaModulo; }

    public double getTiempoDeVida(double tiempo) {
        return tiempo - tiempoInicial;
    }

    public double getTiempoLlegadaModulo() { return tiempoLlegadaModulo; }

    public double getTiempoDesdeLlegadaModulo(double tiempo) { return tiempo - tiempoLlegadaModulo; }
}
