package br.com.sentinela.model;

public class SensorPerimetral extends DispositivoMonitoramento {

    private String setorVigiado;
    private boolean cercaViolada;

    public SensorPerimetral(String codigoIdentificador, ZonaRestrita localizacao, String setorVigiado) {
        super(codigoIdentificador, localizacao);
        this.setSetorVigiado(setorVigiado);
        this.cercaViolada = false;
    }

    public void registrarViolacao(boolean cercaViolada) {
        if (!this.isEmOperacao()) {
            throw new IllegalStateException("Erro: o sensor perimetral está fora de operação.");
        }
        this.cercaViolada = cercaViolada;
    }

    public String getSetorVigiado() {
        return this.setorVigiado;
    }

    public boolean isCercaViolada() {
        return this.cercaViolada;
    }

    private void setSetorVigiado(String setorVigiado) {
        if (setorVigiado == null || setorVigiado.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: o setor vigiado não pode ficar vazio.");
        }
        this.setorVigiado = setorVigiado.trim();
    }
}
