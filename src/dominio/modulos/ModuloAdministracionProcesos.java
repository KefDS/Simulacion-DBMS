package dominio.modulos;

import dominio.Consulta;
import simulacion.Simulacion;
import simulacion.ValoresAleatorios;

public class ModuloAdministracionProcesos extends Modulo {

    private final int media;
    private final double desviacionEstandar;

    public ModuloAdministracionProcesos(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);

        media = 1000; // 1 seg -> 1000 milisegundos
        desviacionEstandar = Math.sqrt(10000); // 0.01 segundos^2 -> 10000 milsegundos^2
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        return ValoresAleatorios.generarValorDistribucionNormal(media, desviacionEstandar);
    }
}
