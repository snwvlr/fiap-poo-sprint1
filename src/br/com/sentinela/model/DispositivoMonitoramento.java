package br.com.sentinela.model;

public class DispositivoMonitoramento {

    private String codigoIdentificador;
    private ZonaRestrita localizacao;
    private boolean emOperacao;

    public DispositivoMonitoramento(String codigoIdentificador, ZonaRestrita localizacao) {
        this.setCodigoIdentificador(codigoIdentificador);
        this.setLocalizacao(localizacao);
        this.emOperacao = true;
    }

    public String getCodigoIdentificador() {
        return this.codigoIdentificador;
    }

    public ZonaRestrita getLocalizacao() {
        return this.localizacao;
    }

    public boolean isEmOperacao() {
        return this.emOperacao;
    }

    public void ativar() {
        this.emOperacao = true;
    }

    public void desativar() {
        this.emOperacao = false;
    }

    protected void atualizarLocalizacao(ZonaRestrita localizacao) {
        this.setLocalizacao(localizacao);
    }

    private void setCodigoIdentificador(String codigoIdentificador) {
        if (codigoIdentificador == null || codigoIdentificador.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: o dispositivo precisa de um código identificador.");
        }
        this.codigoIdentificador = codigoIdentificador.trim();
    }

    private void setLocalizacao(ZonaRestrita localizacao) {
        if (localizacao == null) {
            throw new IllegalArgumentException("Erro: o dispositivo precisa de uma zona de localização.");
        }
        this.localizacao = localizacao;
    }
}
