package Simulacion.Estadisticas;

import Dominio.Enumeraciones.TipoConsulta;
import Dominio.Modulos.Modulo;
import Dominio.Enumeraciones.TipoMudulo;

import java.util.Map;

public class Estadistica {
    private final Map<Modulo, Integer> tamanoPromedioCola;
    private final double tiempoPromedioVidaConexion;
    private final int numeroConexionesDescartadas;
    private final Map<TipoConsulta, Map<TipoMudulo, Double>> tiempoPromedioPorTipoConsultas;

    public Estadistica(Map<Modulo, Integer> tamanoPromedioCola, double tiempoPromedioVidaConexion, int numeroConexionesDescartadas, Map<TipoConsulta, Map<TipoMudulo, Double>> tiempoPromedioPorTipoConsultas) {
        this.tamanoPromedioCola = tamanoPromedioCola;
        this.tiempoPromedioVidaConexion = tiempoPromedioVidaConexion;
        this.numeroConexionesDescartadas = numeroConexionesDescartadas;
        this.tiempoPromedioPorTipoConsultas = tiempoPromedioPorTipoConsultas;
    }

    public Map<Modulo, Integer> getTamanoPromedioCola() {
        return tamanoPromedioCola;
    }

    public double getTiempoPromedioVidaConexion() {
        return tiempoPromedioVidaConexion;
    }

    public int getNumeroConexionesDescartadas() {
        return numeroConexionesDescartadas;
    }

    public Map<TipoConsulta, Map<TipoMudulo, Double>> getTiempoPromedioPorTipoConsultas() {
        return tiempoPromedioPorTipoConsultas;
    }
}
