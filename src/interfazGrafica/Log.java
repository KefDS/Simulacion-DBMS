package interfazGrafica;

import simulacion.estadisticas.DatosParciales;
import simulacion.estadisticas.Resultados;
import simulacion.estadisticas.ResultadosFinales;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Log {
    private Path path;
    private boolean archivoCreado;

    Log() {
        path = Paths.get("logPintoDB.txt");
        try {
            Files.write(path, "".getBytes());
            archivoCreado = true;
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo");
            archivoCreado = false;
        }
    }

    public void escribirDatosParciales(DatosParciales datosParciales) {
        if (archivoCreado) {
            String datosParcialesFormato =
                    "Reloj: " + datosParciales.reloj + "\n" +
                            "Conexiones Completadas: " + datosParciales.numeroConexionesCompletadas + "\n" +
                            "Conexiones Descartadas: " + datosParciales.numeroConexionesDescartadas + "\n" +
                            "Conexiones Expiradas: " + datosParciales.numeroConexionesExpiradas + "\n\n";

            try {
                Files.write(path, datosParcialesFormato.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {
            }
        }
    }

    public void escribirResultadosFinales(Resultados resultados) {
        if (archivoCreado) {
            String resultadosFinalesFormato =
                    "Conexiones Completadas: " + resultados.numeroConexionesCompletadas + "\n" +
                            "Conexiones Descartadas: " + resultados.numeroConexionesDescartadas + "\n" +
                            "Conexiones Expiradas: " + resultados.numeroConexionesExpiradas + "\n" +
                            "Promedio de Vida Conexión: " + resultados.tiempoPromedioVidaConexion + "ms" + "\n" +
                            "Resultados por Módulo: " + "\n" + resultados.tiempoPromedioPorTipoConsultas + "\n\n";

            try {
                Files.write(path, resultadosFinalesFormato.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ignored) {
            }
        }
    }
}
