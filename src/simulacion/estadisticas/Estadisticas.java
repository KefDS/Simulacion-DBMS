package simulacion.estadisticas;

import dominio.modulos.Modulo;
import dominio.enumeraciones.TipoMudulo;
import simulacion.PromedioTiempo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Estadisticas {
    private List<Estadistica> listaEstadisticas;
    private PromedioTiempo promedioVidaConexion;
    private int numeroConexionesDescartadas;

    public Estadisticas() {
        listaEstadisticas = new ArrayList<Estadistica>();
        promedioVidaConexion = new PromedioTiempo();
        numeroConexionesDescartadas = 0;
    }

    public void anadirTiempoConsultaFinalizada(double tiempo) {
        promedioVidaConexion.anadirTiempoAcumulado(tiempo);
    }

    public void anadirConexionDescartada() {
        numeroConexionesDescartadas++;
    }

    public Estadistica obtenerEstadistica(Map<TipoMudulo, Modulo> modulos) {
        // TODO
        return null;
    }

    public double getIntervaloConfianzaTiempoVidaConexion() {
        // TODO
        return 0;
    }

    public Estadistica getPromediosTodasEjecuciones() {
        // TODO
        return null;
    }
}
