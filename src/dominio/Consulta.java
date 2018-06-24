package dominio;

import dominio.enumeraciones.TipoConsulta;
import dominio.modulos.Modulo;
import simulacion.estadisticas.EstadisticaConsulta;

public class Consulta {
    private Modulo moduloActual;
    private final TipoConsulta tipo;
    private int numeroBloques;
    private boolean timeout;
    private final EstadisticaConsulta estadisticaConsulta;

    public Consulta(Modulo moduloActual, TipoConsulta tipo, double tiempoInicial) {
        this.moduloActual = moduloActual;
        this.tipo = tipo;
        timeout = false;
        estadisticaConsulta = new EstadisticaConsulta(tiempoInicial);
        numeroBloques = -1;
    }

    public void setModuloActual(Modulo moduloActual) {
        this.moduloActual = moduloActual;
    }

    public EstadisticaConsulta getEstadisticaConsulta() {
        return estadisticaConsulta;
    }

    public TipoConsulta getTipoConsulta() {
        return tipo;
    }

    public Modulo getModuloActual() {
        return moduloActual;
    }

    public void seVencioTimeout() {
        timeout = true;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public int getNumeroBloques() {
        return numeroBloques;
    }

    public void setNumeroBloques(int numeroBloques) {
        this.numeroBloques = numeroBloques;
    }
}
