package dominio.enumeraciones;

public enum TipoModulo {
    CLIENTES("Cleintes"),
    PROCESOS("Procesos"),
    PROCESAMINETO("Procesam..."),
    TRANSACCION("Transaccion"),
    EJECUCCION("Ejecuccion");

    private String name;

    TipoModulo(String name) { this.name = name; }

    public String getName() { return name; }
}
