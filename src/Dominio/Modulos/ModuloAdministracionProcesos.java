package Dominio.Modulos;

import Dominio.Consulta;
import Simulacion.ControladorSimulacion;
import Simulacion.Enumeraciones.TipoEvento;
import Simulacion.Evento;
import Simulacion.ValoresAleatorios;

public class ModuloAdministracionProcesos extends Modulo {

    private final int media;
    private final double desviacionEstandar;

    public ModuloAdministracionProcesos(ControladorSimulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);

        media = 1000; // 1 seg -> 1000 milisegundos
        desviacionEstandar = Math.sqrt(10000); // 0.01 segundos^2 -> 10000 milsegundos^2
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        return ValoresAleatorios.generarValorDistribucionNormal(media, desviacionEstandar);
    }
}
