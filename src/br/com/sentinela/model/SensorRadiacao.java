package br.com.sentinela.model;

public class SensorRadiacao extends DispositivoMonitoramento {

    private double leituraAtual;
    private double limiteSeguranca;

    public SensorRadiacao(String codigoIdentificador, ZonaRestrita localizacao) {
        super(codigoIdentificador, localizacao);
        this.limiteSeguranca = localizacao.getLimiteRadiacao();
        this.leituraAtual = localizacao.getNivelRadiacaoAtual();
    }

    public void registrarLeitura(double leitura) {
        if (!this.isEmOperacao()) {
            throw new IllegalStateException("Erro: o sensor de radiação está fora de operação.");
        }
        // Se a leitura for inválida, nenhum dos dois valores muda.
        this.getLocalizacao().registrarLeituraRadiacao(leitura);
        this.leituraAtual = leitura;
    }

    public double getLeituraAtual() {
        return this.leituraAtual;
    }

    public double getLimiteSeguranca() {
        return this.limiteSeguranca;
    }
}
