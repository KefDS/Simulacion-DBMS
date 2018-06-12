package simulacion;

import dominio.Consulta;
import dominio.modulos.*;
import dominio.enumeraciones.TipoModulo;
import interfazGrafica.interfaces.Observable;
import interfazGrafica.interfaces.Observer;
import javafx.util.Pair;
import simulacion.estadisticas.DatosParciales;
import simulacion.estadisticas.Estadisticas;
import simulacion.estadisticas.Resultados;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class Simulacion implements Observable {
    private double reloj;
    private final Queue<Evento> colaEventos;
    private final Map<TipoModulo, Modulo> modulos;
    private Estadisticas estadisticas;
    private final long tiempoTotal;
    private boolean modoLento;

    private final Queue<Observer> observersQueue;

    public Simulacion(int tiempoTotal, int conexionesMaximas, int timeout,
                      int servidoresProcesamiento, int servidoresTransaccion,
                      int servidoresEjecuccion, boolean modoLento) {
        reloj = 0;
        this.tiempoTotal = tiempoTotal * 1000;
        colaEventos = new PriorityQueue<>();
        estadisticas = new Estadisticas();
        modulos = new EnumMap<>(TipoModulo.class);
        inicializadorModulos(conexionesMaximas, timeout,
                servidoresProcesamiento, servidoresTransaccion, servidoresEjecuccion);
        this.modoLento = modoLento;
        observersQueue = new LinkedList<>();
    }

    private void inicializadorModulos(int conexionesMaximas, int timeout, int servidoresProcesamiento, int servidoresTransaccion, int servidoresEjecuccion) {
        Modulo moduloClientes = new ModuloClientes(this, conexionesMaximas, timeout);
        Modulo moduloEjecucion = new ModuloEjecucion(this, moduloClientes, servidoresEjecuccion); // 5
        Modulo moduloTransaccion = new ModuloTransaccion(this, moduloEjecucion, servidoresTransaccion); // 4
        Modulo moduloProcesamiento = new ModuloProcesamiento(this, moduloTransaccion, servidoresProcesamiento); // 3
        Modulo moduloProcesos = new ModuloAdministracionProcesos(this, moduloProcesamiento, 1); // 2
        ((ModuloClientes) moduloClientes).setSiguienteModulo(moduloProcesos); // 1

        modulos.put(TipoModulo.CLIENTES, moduloClientes);
        modulos.put(TipoModulo.PROCESOS, moduloProcesos);
        modulos.put(TipoModulo.PROCESAMINETO, moduloProcesamiento);
        modulos.put(TipoModulo.TRANSACCION, moduloTransaccion);
        modulos.put(TipoModulo.EJECUCCION, moduloEjecucion);
    }

    public Resultados realizarSimulacion() {
        long limiteTiempo = System.currentTimeMillis() + tiempoTotal;
        ((ModuloClientes) modulos.get(TipoModulo.CLIENTES)).generarEntrada(); // Primera llegada

        while (System.currentTimeMillis() < limiteTiempo) {
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
            // Retorna datos por ciclo de reloj
            observersQueue.forEach(observer -> observer.notify(retornarDatosParciales()));
            pausaSimulacion(modoLento);
        }
        
        return estadisticas.obtenerResultados(modulos.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getEstadisticasModulo()
        )));
    }

    private DatosParciales retornarDatosParciales() {
        Map<TipoModulo, Pair<Integer, Integer>> informacionPorModulo = new EnumMap<>(TipoModulo.class);
        Arrays.stream(TipoModulo.values()).forEach(tipo ->
                informacionPorModulo.put(tipo, modulos.get(tipo).datosActuales()));
        return new DatosParciales(reloj, estadisticas.getNumeroConexionesCompletadas(),
                estadisticas.getNumeroConexionesDescartadas(),
                estadisticas.getNumeroConexionesExpiradas(),
                informacionPorModulo);
    }

    private void pausaSimulacion(boolean modoLento) {
        if (modoLento) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getReloj() {
        return reloj;
    }

    public void anadirEvento(Evento evento) {
        colaEventos.add(evento);
    }

    public void eliminarEvento(Consulta consulta) {
        colaEventos.removeIf(evento -> evento.getConsulta() == consulta);
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public void liberarConexion() {
        ((ModuloClientes) modulos.get(TipoModulo.CLIENTES)).liberarConexion();
    }

    @Override
    public void addObserver(Observer obs) {
        observersQueue.add(obs);
    }
}
