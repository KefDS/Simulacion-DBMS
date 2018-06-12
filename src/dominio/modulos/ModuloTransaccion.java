package dominio.modulos;

import dominio.Consulta;
import dominio.enumeraciones.TipoConsulta;
import simulacion.Simulacion;
import simulacion.ValoresAleatorios;

import java.util.PriorityQueue;

public class ModuloTransaccion extends Modulo {

    private boolean prioridadDDL;

    public ModuloTransaccion(Simulacion simulacion, Modulo siguienteModulo, int numeroServidores) {
        super(simulacion, siguienteModulo, numeroServidores);
        prioridadDDL = false;

        colaConsultas = new PriorityQueue<>((consulta1, consulta2) -> {
            if (consulta1.getTipoConsulta() == TipoConsulta.DDL) {
                if (consulta2.getTipoConsulta() == TipoConsulta.DDL) {
                    return Double.compare(consulta1.getEstadisticaConsulta().getTiempoLlegadaModulo(),
                            consulta2.getEstadisticaConsulta().getTiempoLlegadaModulo());
                } else {
                    return -1;
                }
            }
            return Double.compare(consulta1.getEstadisticaConsulta().getTiempoLlegadaModulo(),
                    consulta2.getEstadisticaConsulta().getTiempoLlegadaModulo());
        });
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
                    if (numeroServidores == numeroServidoresTotales) {
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

    private void atender(Consulta consulta) {
        numeroServidores--;
        generarSalida(consulta);
    }

    @Override
    public void procesarSalida(Consulta consulta) {
        // La consulta DDL se termino de procesar?
        if (prioridadDDL && consulta.getTipoConsulta() == TipoConsulta.DDL) prioridadDDL = false;

        super.procesarSalida(consulta);
    }

    @Override
    protected Consulta getSiguienteConsulta() {
        if (colaConsultas.peek() == null) return null;
        // Consulta DDL esperando?
        if (prioridadDDL) {
            if (numeroServidores == numeroServidoresTotales - 1) {
                return colaConsultas.poll();
            } else {
                return null;
            }
        } else {
            // Siguiente consulta es DDL?
            if (colaConsultas.peek().getTipoConsulta() == TipoConsulta.DDL) {
                prioridadDDL = true;
                // Todos los servidores libres?
                return (numeroServidores == numeroServidoresTotales - 1) ? colaConsultas.poll() : null;
            } else {
                return colaConsultas.poll();
            }
        }
    }

    @Override
    protected double getTiempoSalida(Consulta consulta) {
        int numeroBloques = 0;
        double tiempo = numeroServidoresTotales * 30;
        if (consulta.getTipoConsulta() == TipoConsulta.JOIN) {
            numeroBloques = (int) Math.round(ValoresAleatorios.generarValorDistribucionUniforme(1, 64));
        } else if (consulta.getTipoConsulta() == TipoConsulta.SELECT) {
            numeroBloques = 1;
        }
        tiempo += numeroBloques * 100;
        consulta.setNumeroBloques(numeroBloques);
        return tiempo;
    }

    @Override
    public void limpiarModulo() {
        prioridadDDL = false;
        super.limpiarModulo();
    }
}
