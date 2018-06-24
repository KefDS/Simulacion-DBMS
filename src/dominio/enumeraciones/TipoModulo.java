package dominio.enumeraciones;

public enum TipoModulo {
    CLIENTES("Clientes"),
    PROCESOS("Procesos"),
    PROCESAMINETO("Procesam..."),
    TRANSACCION("Transacción"),
    EJECUCCION("Ejecucción");

    private String name;

    TipoModulo(String name) { this.name = name; }

    public String getName() { return name; }
}
