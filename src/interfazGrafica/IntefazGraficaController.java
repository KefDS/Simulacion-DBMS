package interfazGrafica;

import interfazGrafica.bibliotecas.IntegerStringConverter;
import simulacion.EjecucionesSimulacion;
import simulacion.Simulacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class IntefazGraficaController implements Initializable {
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

    private List<Spinner<Integer>> listaSpinners;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaSpinners = Arrays.asList(numeroEjecucciones, duracionSegSpinner, timeoutSpinner,
                conexionesMaximasSpinner, servidoresProcesamientoSpinner, servidoresTransaccionesSpinner, servidoresEjecuccionSpinner);

        numeroEjecucciones.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
        duracionSegSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
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
        ejecucionesSimulacion.empezarEjecucciones();
    }
}
