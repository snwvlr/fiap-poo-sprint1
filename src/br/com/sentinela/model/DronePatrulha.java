package br.com.sentinela.model;

public class DronePatrulha extends DispositivoMonitoramento {

    private String rotaPatrulha;
    private int bateriaPercentual;
    private boolean veiculoNaImagemSimulada;
    private boolean veiculoNaoIdentificado;

    public DronePatrulha(String codigoIdentificador, ZonaRestrita localizacao, String rotaPatrulha) {
        super(codigoIdentificador, localizacao);
        this.setRotaPatrulha(rotaPatrulha);
        this.setBateriaPercentual(100);
    }

    public void iniciarPatrulha() {
        this.verificarDisponibilidade();
        this.veiculoNaImagemSimulada = false;
        this.veiculoNaoIdentificado = false;
        System.out.println("Drone " + this.getCodigoIdentificador() + " em patrulha: " + this.rotaPatrulha);
        System.out.println("Câmera simulada: nenhuma detecção.");
    }

    public void registrarImagemSimulada(boolean veiculoNaImagemSimulada) {
        // Imagem simulada que o drone vai analisar na inspeção.
        this.veiculoNaImagemSimulada = veiculoNaImagemSimulada;
    }

    public void inspecionarZona(ZonaRestrita zonaInspecionada) {
        this.verificarDisponibilidade();
        this.atualizarLocalizacao(zonaInspecionada);
        System.out.println("Drone " + this.getCodigoIdentificador() + " enviado para "
                + zonaInspecionada.getIdentificador() + " - " + zonaInspecionada.getDescricao());

        this.veiculoNaoIdentificado = this.veiculoNaImagemSimulada;
        if (this.veiculoNaoIdentificado) {
            System.out.println("Câmera simulada: veículo não identificado detectado.");
        } else {
            System.out.println("Câmera simulada: nenhuma detecção.");
        }
    }

    public void atualizarBateria(int bateriaPercentual) {
        this.setBateriaPercentual(bateriaPercentual);
    }

    public String getRotaPatrulha() {
        return this.rotaPatrulha;
    }

    public int getBateriaPercentual() {
        return this.bateriaPercentual;
    }

    public boolean isVeiculoNaoIdentificado() {
        return this.veiculoNaoIdentificado;
    }

    private void verificarDisponibilidade() {
        if (!this.isEmOperacao() || this.bateriaPercentual == 0) {
            throw new IllegalStateException("Erro: o drone precisa estar em operação e ter bateria.");
        }
    }

    private void setRotaPatrulha(String rotaPatrulha) {
        if (rotaPatrulha == null || rotaPatrulha.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: o drone precisa de uma rota de patrulha.");
        }
        this.rotaPatrulha = rotaPatrulha.trim();
    }

    private void setBateriaPercentual(int bateriaPercentual) {
        if (bateriaPercentual < 0 || bateriaPercentual > 100) {
            throw new IllegalArgumentException("Erro: a bateria deve ficar entre 0 e 100%.");
        }
        this.bateriaPercentual = bateriaPercentual;
    }
}
