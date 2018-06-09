package Dominio.Enumeraciones;

public enum TipoConsulta {
    SELECT(true),
    UPDATE(false),
    JOIN(true),
    DDL(false);

    private final boolean esReadOnly;

    TipoConsulta(boolean esReadOnly) {
        this.esReadOnly = esReadOnly;
    }

    public boolean esReadOnly() {
        return esReadOnly;
    }
}
