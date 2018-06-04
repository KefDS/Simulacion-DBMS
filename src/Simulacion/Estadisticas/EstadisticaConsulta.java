package Simulacion.Estadisticas;

public class EstadisticaConsulta {
    private final double tiempoInicial;
    private double tiempoLlegadaModulo;

    public EstadisticaConsulta(double tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public double getTiempoInicial() {
        return tiempoInicial;
    }

    public double getTiempoLlegadaModulo() {
        return tiempoLlegadaModulo;
    }

    public void setTiempoLlegadaModulo(double tiempoLlegadaModulo) {
        this.tiempoLlegadaModulo = tiempoLlegadaModulo;
    }
}
