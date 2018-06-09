package Simulacion;

import Dominio.Consulta;
import Dominio.Modulos.*;
import Dominio.Enumeraciones.TipoMudulo;
import Simulacion.Estadisticas.Estadisticas;

import java.util.EnumMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ControladorSimulacion {
    // TODO: GUI
    private double reloj;
    private Queue<Evento> colaEventos;
    private Map<TipoMudulo, Modulo> modulos;
    private Estadisticas estadisticas;

    public ControladorSimulacion() {
        reloj = 0;
        colaEventos = new PriorityQueue<Evento>();
        estadisticas = new Estadisticas();
        modulos = new EnumMap<>(TipoMudulo.class);
    }

    public void iniciarSimulacion(int veces,
                                  int tiempoTotal,
                                  boolean modoLento,
                                  int conexionesMaximas,
                                  int timeout,
                                  int servidoresProcesamiento,
                                  int servidoresTransaccion,
                                  int servidoresEjecuccion) {
        inicializadorModulos(conexionesMaximas, timeout,
                servidoresProcesamiento, servidoresTransaccion, servidoresEjecuccion);

        for (int i = 0; i < veces; i++) {
            long limiteTiempo = System.currentTimeMillis() + (tiempoTotal * 1000);
            ((ModuloClientes) modulos.get(TipoMudulo.CLIENTES)).generarEntrada();

            while (true /*System.currentTimeMillis() < limiteTiempo*/) {
                Evento eventoActual = colaEventos.poll();
                reloj = eventoActual.getTiempoEvento();

                switch (eventoActual.getTipoEvento()) {
                    case LLEGADA:
                        eventoActual.getModulo().procesarEntrada(eventoActual.getConsulta());
                        break;
                    case SALIDA:
                        eventoActual.getModulo().procesarSalida(eventoActual.getConsulta());
                        break;
                    case TIMEOUT:
                        eventoActual.getConsulta().getModuloActual().procesarTimeout(eventoActual.getConsulta());
                        break;
                }

                if (modoLento) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void inicializadorModulos(int conexionesMaximas, int timeout, int servidoresProcesamiento, int servidoresTransaccion, int servidoresEjecuccion) {
        Modulo moduloClientes = new ModuloClientes(this, conexionesMaximas, timeout);
        Modulo moduloEjecucion = new ModuloEjecucion(this, moduloClientes, servidoresEjecuccion); // 5
        Modulo moduloTransaccion = new ModuloTransaccion(this, moduloEjecucion, servidoresTransaccion); // 4
        Modulo moduloProcesamiento = new ModuloProcesamiento(this, moduloTransaccion, servidoresProcesamiento); // 3
        Modulo moduloProcesos = new ModuloAdministracionProcesos(this, moduloProcesamiento, 1); // 2
        ((ModuloClientes) moduloClientes).setSiguienteModulo(moduloProcesos); // 1

        modulos.put(TipoMudulo.CLIENTES, moduloClientes);
        modulos.put(TipoMudulo.PROCESOS, moduloProcesos);
        modulos.put(TipoMudulo.PROCESAMINETO, moduloProcesamiento);
        modulos.put(TipoMudulo.TRANSACCION, moduloTransaccion);
        modulos.put(TipoMudulo.EJECUCCION, moduloEjecucion);
    }

    public double getReloj() {
        return reloj;
    }

    public void anadirEvento(Evento evento) {
        colaEventos.add(evento);
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    // TODO: Mejor forma
    public void liberarConexion() {
        ((ModuloClientes) modulos.get(TipoMudulo.CLIENTES)).liberarConexion();
    }
}
