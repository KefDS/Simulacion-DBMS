package interfazGrafica;

import simulacion.estadisticas.DatosParciales;
import simulacion.estadisticas.ResultadosFinales;

import java.nio.file.*;
import java.io.IOException;

public class Log {
    private static String rutaArchivo = "/logSimulacion.txt";

    Log() {
        Path path = Paths.get(rutaArchivo);
        // TODO: Abrir archivo
    }

    public void escribirDatosParciales(DatosParciales datosParciales) {
       Path path = Paths.get(rutaArchivo);

       String datosParcialesFormato =
               "Reloj: " + datosParciales.reloj + "\n" +
               "Conexiones Completadas: " + datosParciales.numeroConexionesCompletadas + "\n" +
               "Conexiones Descartadas: " + datosParciales.numeroConexionesDescartadas + "\n" +
               "Conexiones Expiradas" + datosParciales.numeroConexionesExpiradas + "\n";

        try {
            Files.write(path, datosParcialesFormato.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void escribirResultadosFinales(ResultadosFinales resultadosFinales) throws IOException {
        Path path = Paths.get(rutaArchivo);

        String resultadosFinalesFormato =
                "Conexiones Completadas: " + resultadosFinales.numeroConexionesCompletadas + "\n" +
                "Conexiones Descartadas: " + resultadosFinales.numeroConexionesDescartadas + "\n" +
                "Conexiones Expiradas: " + resultadosFinales.numeroConexionesExpiradas + "\n" +
                "Promedio de Vida Conexión: " + resultadosFinales.tiempoPromedioVidaConexion + "ms" + "\n";

        try {
        Files.write(path, resultadosFinalesFormato.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrarArchivo() {
        // TODO
    }
}
