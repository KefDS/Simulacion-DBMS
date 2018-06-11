package simulacion.estadisticas;

import dominio.modulos.Modulo;
import dominio.enumeraciones.TipoMudulo;
import simulacion.PromedioTiempo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Estadisticas {
    private PromedioTiempo promedioVidaConexion;
    private int numeroConexionesDescartadas;
    private int numeroConexionesExpiradas;
    private int numeroConexionesCompletadas;

    public Estadisticas() {
        promedioVidaConexion = new PromedioTiempo();
        numeroConexionesDescartadas = 0;
    }

    public void anadirTiempoConsultaFinalizada(double tiempo) {
        promedioVidaConexion.anadirTiempoAcumulado(tiempo);
    }

    public void anadirConexionDescartada() {
        numeroConexionesDescartadas++;
    }

    public int getNumeroConexionesDescartadas() {
        return numeroConexionesDescartadas;
    }

    public int getNumeroConexionesExpiradas() {
        return numeroConexionesExpiradas;
    }

    public void anadirNumeroConexionesExpiradas() {
        this.numeroConexionesExpiradas++;
    }

    public int getNumeroConexionesCompletadas() {
        return numeroConexionesCompletadas;
    }

    public void anadirNumeroConexionesCompletadas() {
        this.numeroConexionesCompletadas++;
    }

    public Resultados obtenerResultados(Map<TipoMudulo, EstadisticasModulo> estadisticasModulos) {
        // TODO
        return null;
    }
}
