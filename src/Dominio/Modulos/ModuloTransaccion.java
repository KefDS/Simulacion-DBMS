package Dominio.Modulos;

import Dominio.Consulta;
import Dominio.Enumeraciones.TipoConsulta;
import Simulacion.ControladorSimulacion;

public class ModuloTransaccion extends Modulo {

    private boolean prioridadDDL;
    private int numeroServidoresTotal;

    public ModuloTransaccion(ControladorSimulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
        prioridadDDL = false;
        numeroServidoresTotal = numeroServidores;
    }

    @Override
    public void procesarEntrada(Consulta consulta) {
        consulta.getEstadisticaConsulta().setTiempoLlegadaModulo(simulacion.getReloj());
        // Servidores disponibles?
        if (numeroServidores > 0) {
            // El modulo necesita esperar que se desocupen todos los servidores
            // ya que una consulta DDL necesita correr solo, por eso aunque hayan servidores
            // disponibles, la consulta entrante debe esperar en cola
            if (prioridadDDL) {
                colaConsultas.add(consulta);
            } else {
                // Si la consulta entrante es un DDL, debe fijarse si es el unico
                // que va a ser atendido
                if (consulta.getTipoConsulta() == TipoConsulta.DDL) {
                    // Todos los servidores disponibles?
                    if (numeroServidores == numeroServidoresTotal) {
                        atender(consulta);
                    } else {
                        prioridadDDL = true;
                        colaConsultas.add(consulta);
                    }
                }
                // Todas las demas consultas
                else {
                    atender(consulta);
                }
            }
        } else {
            colaConsultas.add(consulta);
        }
    }

    @Override
    public void procesarSalida(Consulta consulta) {
        // Anade tiempo de servicio
        estadisticasModulo.anadirTiempoServicio(consulta.getTipoConsulta(),
                consulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));

        // La consulta DDL se termino de procesar?
        if (prioridadDDL && consulta.getTipoConsulta() == TipoConsulta.DDL) prioridadDDL = false;

        revisarTimeout(consulta);

        // Consulta DDL esperando?
        if (prioridadDDL) {
            if (numeroServidores == numeroServidoresTotal - 1) {
                Consulta consultaDDL = colaConsultas.poll();
                estadisticasModulo.anadirTiempoClienteEnCola(
                        consultaDDL.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));
                generarSalida(consultaDDL);
            } else {
                numeroServidores++;
            }
        } else {
            Consulta siguienteConsulta = getSiguienteConsulta();
            if (siguienteConsulta != null) {
                if (siguienteConsulta.getTipoConsulta() == TipoConsulta.DDL) {
                    // Todos los servidores disponibles?
                    if (numeroServidores == numeroServidoresTotal - 1) {
                        prioridadDDL = true;
                        estadisticasModulo.anadirTiempoClienteEnCola(
                                siguienteConsulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));
                        generarSalida(siguienteConsulta);
                    } else {
                        prioridadDDL = true;
                        colaConsultas.add(consulta);
                    }
                }
                estadisticasModulo.anadirTiempoClienteEnCola(
                        siguienteConsulta.getEstadisticaConsulta().getTiempoDesdeLlegadaModulo(simulacion.getReloj()));
                generarSalida(siguienteConsulta);
            } else {
                numeroServidores++;
            }
        }
    }

    private void atender(Consulta consulta) {
        numeroServidores--;
        generarSalida(consulta);
    }

//    @Override
//    protected Consulta getSiguienteConsulta() {
//        // Consulta DDL esperando?
//        if (prioridadDDL) {
//            if (numeroServidores == numeroServidoresTotal - 1) {
//                return colaConsultas.poll();
//            } else {
//                return null;
//            }
//        } else {
//            return colaConsultas.poll();
//        }
//    }

    @Override
    protected void generarSalida(Consulta consulta) {
        // TODO
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        // TODO
        return 0;
    }
}
