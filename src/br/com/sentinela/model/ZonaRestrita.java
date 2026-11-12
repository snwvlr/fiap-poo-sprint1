package br.com.sentinela.model;

public class ZonaRestrita {

    public static final String RISCO_NORMAL = "NORMAL";
    public static final String RISCO_ATENCAO = "ATENCAO";
    public static final String RISCO_CRITICO = "CRITICO";

    private static final double LEITURA_MAXIMA = 1000000.0;
    private static final double FAIXA_ATENCAO = 0.8;

    private String identificador;
    private String descricao;
    private double nivelRadiacaoAtual;
    private double limiteRadiacao;

    public ZonaRestrita(String identificador, String descricao, double limiteRadiacao) {
        this.setIdentificador(identificador);
        this.setDescricao(descricao);
        this.setLimiteRadiacao(limiteRadiacao);
        this.setNivelRadiacaoAtual(0.0);
    }

    public void registrarLeituraRadiacao(double leitura) {
        this.setNivelRadiacaoAtual(leitura);
    }

    public String classificarRisco() {
        if (this.nivelRadiacaoAtual > this.limiteRadiacao) {
            return RISCO_CRITICO;
        }
        if (this.nivelRadiacaoAtual >= this.limiteRadiacao * FAIXA_ATENCAO) {
            return RISCO_ATENCAO;
        }
        return RISCO_NORMAL;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public double getNivelRadiacaoAtual() {
        return this.nivelRadiacaoAtual;
    }

    public double getLimiteRadiacao() {
        return this.limiteRadiacao;
    }

    private void setIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: a zona precisa de um identificador.");
        }
        this.identificador = identificador.trim();
    }

    private void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: a descrição da zona não pode ficar vazia.");
        }
        this.descricao = descricao.trim();
    }

    private void setNivelRadiacaoAtual(double leitura) {
        if (leitura < 0 || leitura > LEITURA_MAXIMA) {
            throw new IllegalArgumentException("Erro: falha no sensor. A leitura deve ficar entre 0 e 1.000.000 µSv/h.");
        }
        this.nivelRadiacaoAtual = leitura;
    }

    private void setLimiteRadiacao(double limiteRadiacao) {
        if (limiteRadiacao <= 0 || limiteRadiacao > LEITURA_MAXIMA) {
            throw new IllegalArgumentException("Erro: o limite de radiação deve ser maior que zero e até 1.000.000 µSv/h.");
        }
        this.limiteRadiacao = limiteRadiacao;
    }
}
