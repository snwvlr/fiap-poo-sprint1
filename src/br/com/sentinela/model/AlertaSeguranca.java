package br.com.sentinela.model;

public class AlertaSeguranca {

    private DispositivoMonitoramento sensorOrigem;
    private ZonaRestrita zonaAfetada;
    private String severidade;
    private String mensagem;

    public AlertaSeguranca(DispositivoMonitoramento sensorOrigem, String severidade, String mensagem) {
        this.setSensorOrigem(sensorOrigem);
        this.zonaAfetada = sensorOrigem.getLocalizacao();
        this.setSeveridade(severidade);
        this.setMensagem(mensagem);
    }

    public DispositivoMonitoramento getSensorOrigem() {
        return this.sensorOrigem;
    }

    public ZonaRestrita getZonaAfetada() {
        return this.zonaAfetada;
    }

    public String getSeveridade() {
        return this.severidade;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    private void setSensorOrigem(DispositivoMonitoramento sensorOrigem) {
        if (sensorOrigem == null) {
            throw new IllegalArgumentException("Erro: o alerta precisa de um dispositivo de origem.");
        }
        this.sensorOrigem = sensorOrigem;
    }

    private void setSeveridade(String severidade) {
        if (!ZonaRestrita.RISCO_ATENCAO.equals(severidade) && !ZonaRestrita.RISCO_CRITICO.equals(severidade)) {
            throw new IllegalArgumentException("Erro: a severidade do alerta deve ser ATENCAO ou CRITICO.");
        }
        this.severidade = severidade;
    }

    private void setMensagem(String mensagem) {
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: a mensagem do alerta não pode ficar vazia.");
        }
        this.mensagem = mensagem.trim();
    }
}
