package interfazGrafica;

import dominio.enumeraciones.TipoModulo;
import interfazGrafica.bibliotecas.IntegerStringConverter;
import interfazGrafica.interfaces.Observer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.util.Pair;
import simulacion.EjecucionesSimulacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import simulacion.estadisticas.DatosParciales;
import simulacion.estadisticas.Resultados;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class IntefazGraficaController implements Initializable {
    // Parametros
    @FXML
    private Button empezarSimulacionButton;
    @FXML
    private Spinner<Integer> numeroEjecucciones;
    @FXML
    private Spinner<Integer> duracionSegSpinner;
    @FXML
    private Spinner<Integer> timeoutSpinner;
    @FXML
    private Spinner<Integer> conexionesMaximasSpinner;
    @FXML
    private Spinner<Integer> servidoresProcesamientoSpinner;
    @FXML
    private Spinner<Integer> servidoresTransaccionesSpinner;
    @FXML
    private Spinner<Integer> servidoresEjecuccionSpinner;
    @FXML
    private CheckBox modoLentoCheckbox;

    // Ejecuccion
    @FXML
    private Label relojLabel;

    @FXML
    private Label conxCompletadasEje;
    @FXML
    private Label conxDescartadasEje;
    @FXML
    private Label conxExpiradasEje;

    @FXML
    private Label procesosNumServ;
    @FXML
    private Label procesosCola;

    @FXML
    private Label procesamientoNumServ;
    @FXML
    private Label procesamientoCola;

    @FXML
    private Label transaccionNumServ;
    @FXML
    private Label transaccionCola;

    @FXML
    private Label ejecuccionNumServ;
    @FXML
    private Label ejecuccionCola;

    // Resultados
    @FXML
    private Label promVidaConex;
    @FXML
    private Label conxDescartadas;
    @FXML
    private Label conxCompletadas;
    @FXML
    private Label conxExpiradas;
    @FXML
    private Label tamColaProcesos;
    @FXML
    private Label tamColaProcesamiento;
    @FXML
    private Label tamColaTransaccion;
    @FXML
    private Label tamColaEjecuccion;

    private List<Spinner<Integer>> listaSpinners;

    // Bandera para no sobrecargar envio de datos al hilo del GUI
    final AtomicInteger count = new AtomicInteger(-1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaSpinners = Arrays.asList(numeroEjecucciones, duracionSegSpinner, timeoutSpinner,
                conexionesMaximasSpinner, servidoresProcesamientoSpinner, servidoresTransaccionesSpinner, servidoresEjecuccionSpinner);

        numeroEjecucciones.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
        duracionSegSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 5));
        timeoutSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        servidoresTransaccionesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 5));
        servidoresEjecuccionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 5));
        conexionesMaximasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        servidoresProcesamientoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 3));

        listaSpinners.forEach(IntegerStringConverter::createFor);
    }


    public void handleEmpezarSimulacion(ActionEvent actionEvent) {
        deshabilitarControlesParamteros();

        EjecucionesSimulacion ejecucionesSimulacion = creaEjecucionesSimulacion();

        // Observador final cada ejecuccion de simulacion
        ejecucionesSimulacion.addObserver(observadorFinalCadaEjecuccion());

        // Observador cada final de reloj
        ejecucionesSimulacion.getSimulacion().addObserver(observadorFinalCadaTick());

        Task<Pair<Resultados, Double>> task = new Task<Pair<Resultados, Double>>() {
            @Override
            protected Pair<Resultados, Double> call() throws Exception {
                return ejecucionesSimulacion.realizarEjecucciones();
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    private Observer observadorFinalCadaEjecuccion() {
        return data -> {
            Resultados resultados = (Resultados) data;

            Platform.runLater(() -> {
                promVidaConex.setText(Long.toString(Math.round(resultados.tiempoPromedioVidaConexion)) + " ms");
                conxCompletadas.setText(Integer.toString(resultados.numeroConexionesCompletadas));
                conxDescartadas.setText(Integer.toString(resultados.numeroConexionesDescartadas));
                conxExpiradas.setText(Integer.toString(resultados.numeroConexionesExpiradas));

                tamColaProcesos.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.PROCESOS)));
                tamColaProcesamiento.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.PROCESAMINETO)));
                tamColaTransaccion.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.TRANSACCION)));
                tamColaEjecuccion.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.EJECUCCION)));
            });
        };
    }

    private Observer observadorFinalCadaTick() {
        return data -> {
            DatosParciales datos = (DatosParciales) data;
            // Truco para no atestar la cola de tareas asincronas por hacer
            if (count.getAndSet(1) == -1) {
                Platform.runLater(() -> {
                   count.getAndSet(-1);
                   relojLabel.setText(Long.toString(Math.round(datos.reloj)) + " ms");
                   conxCompletadasEje.setText(Integer.toString(datos.numeroConexionesCompletadas));
                    conxDescartadasEje.setText(Integer.toString(datos.numeroConexionesDescartadas));
                    conxExpiradasEje.setText(Integer.toString(datos.numeroConexionesExpiradas));

                    procesosNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESOS).getKey()));
                    procesosCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESOS).getValue()));

                    procesamientoNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESAMINETO).getKey()));
                    procesamientoCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESAMINETO).getValue()));

                    transaccionNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.TRANSACCION).getKey()));
                    transaccionCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.TRANSACCION).getValue()));

                    ejecuccionNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.EJECUCCION).getKey()));
                    ejecuccionCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.EJECUCCION).getValue()));
                });
            }
        };
    }

    private EjecucionesSimulacion creaEjecucionesSimulacion() {
        return new EjecucionesSimulacion(
                    numeroEjecucciones.getValue(),
                    duracionSegSpinner.getValue(),
                    conexionesMaximasSpinner.getValue(),
                    timeoutSpinner.getValue(),
                    servidoresProcesamientoSpinner.getValue(),
                    servidoresTransaccionesSpinner.getValue(),
                    servidoresEjecuccionSpinner.getValue(),
                    modoLentoCheckbox.isSelected()
            );
    }

    private void deshabilitarControlesParamteros() {
        empezarSimulacionButton.setText("Corriendo simulacion...");
        empezarSimulacionButton.setDisable(true);
        listaSpinners.forEach(item -> item.setDisable(true));
    }
}
