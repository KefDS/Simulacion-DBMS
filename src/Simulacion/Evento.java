package Simulacion;

import Dominio.Consulta;
import Simulacion.Enumeraciones.TipoEvento;
import Dominio.Modulos.Modulo;

public class Evento implements Comparable<Evento> {
    private final double tiempoEvento;
    private final Modulo modulo;
    private final TipoEvento tipoEvento;
    private final Consulta consulta;

    public Evento(double tiempoEvento, Modulo modulo, TipoEvento tipoEvento, Consulta consulta) {
        this.tiempoEvento = tiempoEvento;
        this.modulo = modulo;
        this.tipoEvento = tipoEvento;
        this.consulta = consulta;
    }

    public double getTiempoEvento() {
        return tiempoEvento;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    @Override
    public int compareTo(Evento o) {
        return Double.compare(tiempoEvento, o.tiempoEvento);
    }
}
