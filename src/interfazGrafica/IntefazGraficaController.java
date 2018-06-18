package interfazGrafica;

import dominio.enumeraciones.TipoConsulta;
import dominio.enumeraciones.TipoModulo;
import interfazGrafica.bibliotecas.IntegerStringConverter;
import interfazGrafica.interfaces.Observer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import simulacion.EjecutorSimulacion;
import simulacion.Simulacion;
import simulacion.estadisticas.DatosParciales;
import simulacion.estadisticas.Resultados;
import simulacion.estadisticas.ResultadosFinales;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private Label conxActivas;

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
    @FXML
    private ProgressBar estatusTiempo;

    // Resultados
    @FXML
    private Label resultadosLabel;
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
    @FXML
    private AnchorPane select;
    @FXML
    private AnchorPane update;
    @FXML
    private AnchorPane join;
    @FXML
    private AnchorPane ddl;
    @FXML
    private GridPane intervaloConfianzaPane;
    @FXML
    private Label intevaloConfianzaLabel;

    private List<Spinner<Integer>> listaSpinners;
    private Map<TipoConsulta, AnchorPane> asociaConsultaTab;

    // Bandera para no sobrecargar envio de datos al hilo del GUI
    private final AtomicInteger count = new AtomicInteger(-1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaSpinners = Arrays.asList(numeroEjecucciones, duracionSegSpinner, timeoutSpinner,
                conexionesMaximasSpinner, servidoresProcesamientoSpinner, servidoresTransaccionesSpinner, servidoresEjecuccionSpinner);

        intervaloConfianzaPane.managedProperty().bind(intervaloConfianzaPane.visibleProperty());
        intervaloConfianzaPane.setVisible(false);

        asociaConsultaTab = new EnumMap<>(TipoConsulta.class);
        asociaConsultaTab.put(TipoConsulta.SELECT, select);
        asociaConsultaTab.put(TipoConsulta.UPDATE, update);
        asociaConsultaTab.put(TipoConsulta.JOIN, join);
        asociaConsultaTab.put(TipoConsulta.DDL, ddl);

        numeroEjecucciones.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 3));
        duracionSegSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 5));
        timeoutSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        servidoresTransaccionesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 2));
        servidoresEjecuccionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 2));
        conexionesMaximasSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        servidoresProcesamientoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 2));

        listaSpinners.forEach(IntegerStringConverter::createFor);
    }

    public void handleEmpezarSimulacion(ActionEvent actionEvent) {
        intervaloConfianzaPane.setVisible(false);
        deshabilitacionControlesParamteros(true);

        EjecutorSimulacion ejecutorSimulacion = creaEjecucionesSimulacion();

        // Observador final cada ejecuccion de simulacion
        ejecutorSimulacion.addObserver(observadorFinalCadaEjecuccion());

        // Observador cada final de reloj
        ejecutorSimulacion.getSimulacion().addObserver(observadorFinalCadaTick());

        Task<ResultadosFinales> task = new Task<ResultadosFinales>() {
            @Override
            protected ResultadosFinales call() throws Exception {
                Simulacion simulacion = ejecutorSimulacion.getSimulacion();
                simulacion.getProgressProperty().addListener((obs, oldProgress, newProgress) ->
                        updateProgress((Long) newProgress, simulacion.getTiempoTotal())
                );
                return ejecutorSimulacion.realizarEjecucciones();
            }
        };

        // Callback final de ejecucion
        task.valueProperty().addListener((obs, oldvalue, resultadosFinales) -> {
            // Duerme el hilo, para porder observar los datos de la ultima ejecuccion
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resultadosLabel.setText("Resultados Finales");
            deshabilitacionControlesParamteros(false);
            setResultadosEjecucion(resultadosFinales);
            intervaloConfianzaPane.setVisible(true);
            intevaloConfianzaLabel.setText(Long.toString(Math.round(resultadosFinales.interavaloConfianzaPiso)) + " ms - "
            + Long.toString(Math.round(resultadosFinales.getInteravaloConfianzaTecho)) + " ms");
        });

        Thread thread = new Thread(task);
        estatusTiempo.progressProperty().bind(task.progressProperty());
        thread.start();
    }

    private Observer observadorFinalCadaEjecuccion() {
        return ejecuccionSimulacion -> {
            Resultados resultados = ((EjecutorSimulacion) ejecuccionSimulacion).getUltimosResultados();

            Platform.runLater(() -> {
                resultadosLabel.setText("Resultados de ejecucción número " + ((EjecutorSimulacion) ejecuccionSimulacion).getNumeroEjecuccion());
                setResultadosEjecucion(resultados);
            });
        };
    }

    private void setResultadosEjecucion(Resultados resultados) {
        promVidaConex.setText(Long.toString(Math.round(resultados.tiempoPromedioVidaConexion)) + " ms");
        conxCompletadas.setText(Integer.toString(resultados.numeroConexionesCompletadas));
        conxDescartadas.setText(Integer.toString(resultados.numeroConexionesDescartadas));
        conxExpiradas.setText(Integer.toString(resultados.numeroConexionesExpiradas));

        tamColaProcesos.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.PROCESOS)));
        tamColaProcesamiento.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.PROCESAMINETO)));
        tamColaTransaccion.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.TRANSACCION)));
        tamColaEjecuccion.setText(Integer.toString(resultados.tamanoPromedioCola.get(TipoModulo.EJECUCCION)));

        Map<TipoModulo, Map<TipoConsulta, Double>> tiempoPromedioPorTipoConsultas = resultados.tiempoPromedioPorTipoConsultas;

        Map<TipoConsulta, Map<TipoModulo, Double>> tiempoPromedioConsultaModulo = Arrays.stream(TipoConsulta.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        consulta -> new EnumMap<>(TipoModulo.class)));

        tiempoPromedioPorTipoConsultas.forEach((modulo, mapConsultaValor) -> {
            mapConsultaValor.forEach((consulta, valor) -> tiempoPromedioConsultaModulo.get(consulta).put(modulo, valor));
        });

        // Remueve tablas anteriores
        asociaConsultaTab.values().forEach(v -> v.getChildren().clear());

        tiempoPromedioConsultaModulo.forEach((consulta, map) -> {
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Milisegundos");
            BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), yAxis);
            barChart.setStyle("-fx-font-size: " + 11 + "px;");
            barChart.setLegendVisible(false);
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            for (Map.Entry<TipoModulo, Double> value : map.entrySet()) {
                serie.getData().add(new XYChart.Data<>(value.getKey().getName(), Math.round(value.getValue())));
            }
            barChart.getData().add(serie);
            AnchorPane.setTopAnchor(barChart, 0.0);
            AnchorPane.setBottomAnchor(barChart, 0.0);
            AnchorPane.setRightAnchor(barChart, 0.0);
            AnchorPane.setLeftAnchor(barChart, 0.0);
            asociaConsultaTab.get(consulta).getChildren().add(barChart);
        });
    }

    private void setDatosParciales(DatosParciales datos) {
        relojLabel.setText(Long.toString(Math.round(datos.reloj)) + " ms");
        conxCompletadasEje.setText(Integer.toString(datos.numeroConexionesCompletadas));
        conxDescartadasEje.setText(Integer.toString(datos.numeroConexionesDescartadas));
        conxExpiradasEje.setText(Integer.toString(datos.numeroConexionesExpiradas));
        conxActivas.setText(Integer.toString(datos.infoModulo.get(TipoModulo.CLIENTES).getKey()));

        procesosNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESOS).getKey()));
        procesosCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESOS).getValue()));

        procesamientoNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESAMINETO).getKey()));
        procesamientoCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.PROCESAMINETO).getValue()));

        transaccionNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.TRANSACCION).getKey()));
        transaccionCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.TRANSACCION).getValue()));

        ejecuccionNumServ.setText(Integer.toString(datos.infoModulo.get(TipoModulo.EJECUCCION).getKey()));
        ejecuccionCola.setText(Integer.toString(datos.infoModulo.get(TipoModulo.EJECUCCION).getValue()));
    }

    private Observer observadorFinalCadaTick() {
        return simulacion -> {
            DatosParciales datos = ((Simulacion) simulacion).getUltimosResultadosParciales();
            // Truco para no atestar la cola de tareas asincronas por hacer
            if (count.getAndSet(1) == -1) {
                Platform.runLater(() -> {
                    count.set(-1);
                    setDatosParciales(datos);
                });
            }
        };
    }

    private EjecutorSimulacion creaEjecucionesSimulacion() {
        return new EjecutorSimulacion(
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

    private void deshabilitacionControlesParamteros(final boolean estado) {
        empezarSimulacionButton.setText(estado ? "Corriendo Simulacion..." : "Empezar Simulacion");
        empezarSimulacionButton.setDisable(estado);
        listaSpinners.forEach(item -> item.setDisable(estado));
    }
}
