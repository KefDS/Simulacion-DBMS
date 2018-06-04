package Dominio;

import Dominio.Enumeraciones.TipoConsulta;
import Dominio.Modulos.Modulo;
import Simulacion.Estadisticas.EstadisticaConsulta;

public class Consulta {
    private Modulo moduloActual;
    private final TipoConsulta tipo;
    private boolean timeout;
    private final EstadisticaConsulta estadisticaConsulta;

    public Consulta(Modulo moduloActual, TipoConsulta tipo, double tiempoInicial) {
        this.moduloActual = moduloActual;
        this.tipo = tipo;
        timeout = false;
        estadisticaConsulta = new EstadisticaConsulta(tiempoInicial);
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
}
