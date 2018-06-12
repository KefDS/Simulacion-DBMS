package interfazGrafica;

import dominio.enumeraciones.TipoModulo;
import interfazGrafica.bibliotecas.IntegerStringConverter;
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
        empezarSimulacionButton.setText("Corriendo simulacion...");
        empezarSimulacionButton.setDisable(true);
        listaSpinners.forEach(item -> item.setDisable(true));

        EjecucionesSimulacion ejecucionesSimulacion = new EjecucionesSimulacion(
                numeroEjecucciones.getValue(),
                duracionSegSpinner.getValue(),
                conexionesMaximasSpinner.getValue(),
                timeoutSpinner.getValue(),
                servidoresProcesamientoSpinner.getValue(),
                servidoresTransaccionesSpinner.getValue(),
                servidoresEjecuccionSpinner.getValue(),
                modoLentoCheckbox.isSelected()
        );

        // Observadores
        ejecucionesSimulacion.addObserver(data -> {
            Resultados resultados = (Resultados) data;

            Platform.runLater(() -> {
                promVidaConex.setText(Double.toString(resultados.tiempoPromedioVidaConexion));
                conxCompletadas.setText(Integer.toString(resultados.numeroConexionesCompletadas));
                conxDescartadas.setText(Integer.toString(resultados.numeroConexionesDescartadas));
                conxExpiradas.setText(Integer.toString(resultados.numeroConexionesExpiradas));

                tamColaProcesos.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.PROCESOS)));
                tamColaProcesamiento.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.PROCESAMINETO)));
                tamColaTransaccion.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.TRANSACCION)));
                tamColaEjecuccion.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.EJECUCCION)));
            });
        });

        ejecucionesSimulacion.getSimulacion().addObserver(data -> {
            DatosParciales datos = (DatosParciales) data;
            //Platform.runLater(() -> relojLabel.setText(Double.toString(datos.reloj)));
        });

        Task<Pair<Resultados, Double>> task = new Task<Pair<Resultados, Double>>() {
            @Override
            protected Pair<Resultados, Double> call() throws Exception {
                return ejecucionesSimulacion.realizarEjecucciones();
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
}
