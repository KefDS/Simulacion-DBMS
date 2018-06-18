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
        consulta.setModuloActual(this);
        // Servidores disponibles?
        if (numeroServidoresDisponibles > 0) {
            // El modulo necesita esperar que se desocupen todos los servidores
            // ya que una consulta DDL necesita correr sola, por eso aunque hayan servidores
            // disponibles, la consulta entrante debe esperar en cola
            if (prioridadDDL) {
                colaConsultas.add(consulta);
            } else {
                // Si la consulta entrante es un DDL, debe fijarse si es el unico
                // que va a ser atendido
                if (consulta.getTipoConsulta() == TipoConsulta.DDL) {
                    prioridadDDL = true;
                    // Todos los servidores disponibles?
                    if (numeroServidoresDisponibles == numeroServidoresTotales) {
                        atender(consulta);
                    } else {
                        // Se agrega a la cola, con la prioridad DDL
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
        numeroServidoresDisponibles--;
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
        // Consulta DDL esperando en cola?
        if (prioridadDDL) {
            // Todos los servidores disponibles?
            if (numeroServidoresDisponibles == numeroServidoresTotales - 1) {
                return colaConsultas.poll();
            } else {
                return null;
            }
        } else {
            // Nota: Si la consulta que acaba de salir era un DDL, desactivó la prioridad
            // por lo que se debe preguntar si el siguiente es un DDL
            // Siguiente consulta es DDL?
            if (colaConsultas.peek().getTipoConsulta() == TipoConsulta.DDL) {
                prioridadDDL = true;
                // Todos los servidores libres?
                return (numeroServidoresDisponibles == numeroServidoresTotales - 1) ? colaConsultas.poll() : null;
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
    protected void terminarConsulta(Consulta consulta) {
        // Si la consulta que va a ser sacada del sistema es un DDL
        // se elimina la prioridad de DDL que tenia el sistema
        if(consulta.getTipoConsulta() == TipoConsulta.DDL) prioridadDDL = false;
        super.terminarConsulta(consulta);
    }

    @Override
    public void limpiarModulo() {
        prioridadDDL = false;
        super.limpiarModulo();
    }
}
