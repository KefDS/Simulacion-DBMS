package simulacion;

public class PromedioTiempo {
    private int conteo;
    private double tiempo;

    public PromedioTiempo() {
        conteo = 0;
        tiempo = 0;
    }

    public void anadirTiempoAcumulado(double tiempo) {
        conteo++;
        this.tiempo += tiempo;
    }

    public double getPromedio() {
        return tiempo / conteo;
    }
}
