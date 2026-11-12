package br.com.sentinela.test;

import br.com.sentinela.model.AlertaSeguranca;
import br.com.sentinela.model.CentralMonitoramento;
import br.com.sentinela.model.DispositivoMonitoramento;
import br.com.sentinela.model.DronePatrulha;
import br.com.sentinela.model.SensorPerimetral;
import br.com.sentinela.model.SensorRadiacao;
import br.com.sentinela.model.ZonaRestrita;

import java.util.List;

public class TesteSentinela {

    private static int verificacoes;

    public static void main(String[] args) {
        testarZonaRestrita();
        testarLeiturasInvalidas();
        testarConstrutor();
        testarValidacaoAlerta();
        testarCentral();
        System.out.println("\nTestes concluídos: " + verificacoes + " verificações passaram.");
    }

    private static void testarZonaRestrita() {
        ZonaRestrita casaReator = new ZonaRestrita("ZNR-01", "Casa do Reator", 1.0);
        verificar(casaReator != null, "A zona deve existir após a instanciação.");

        casaReator.registrarLeituraRadiacao(0.2);
        verificar("NORMAL".equals(casaReator.classificarRisco()), "0,2 deve ser NORMAL.");
        casaReator.registrarLeituraRadiacao(4.7);
        verificar(casaReator.getNivelRadiacaoAtual() == 4.7, "A leitura deve ser atualizada.");
        verificar("CRITICO".equals(casaReator.classificarRisco()), "4,7 deve ser CRITICO.");

        double[] leituras = {0.0, 0.79, 0.8, 1.0, 1.01, 1000000.0};
        String[] riscos = {"NORMAL", "NORMAL", "ATENCAO", "ATENCAO", "CRITICO", "CRITICO"};
        for (int indice = 0; indice < leituras.length; indice++) {
            casaReator.registrarLeituraRadiacao(leituras[indice]);
            verificar(riscos[indice].equals(casaReator.classificarRisco()),
                    "Classificação incorreta para a leitura " + leituras[indice]);
        }
    }

    private static void testarLeiturasInvalidas() {
        ZonaRestrita casaReator = new ZonaRestrita("ZNR-01", "Casa do Reator", 1.0);
        SensorRadiacao sensorReator = new SensorRadiacao("RAD-01", casaReator);
        sensorReator.registrarLeitura(0.2);

        double[] leiturasInvalidas = {-5.0, 1000001.0};
        for (double leituraInvalida : leiturasInvalidas) {
            boolean rejeitou = false;
            try {
                sensorReator.registrarLeitura(leituraInvalida);
            } catch (IllegalArgumentException erro) {
                rejeitou = true;
            }
            verificar(rejeitou, "A leitura inválida deve ser rejeitada: " + leituraInvalida);
            verificar(casaReator.getNivelRadiacaoAtual() == 0.2, "A zona deve manter o último valor válido.");
            verificar(sensorReator.getLeituraAtual() == 0.2, "O sensor deve manter o último valor válido.");
        }

        sensorReator.desativar();
        boolean rejeitou = false;
        try {
            sensorReator.registrarLeitura(4.7);
        } catch (IllegalStateException erro) {
            rejeitou = true;
        }
        verificar(rejeitou && sensorReator.getLeituraAtual() == 0.2, "Sensor desligado não deve registrar leitura.");
    }

    private static void testarConstrutor() {
        String[] identificadoresInvalidos = {"", "   ", null};
        for (String identificadorInvalido : identificadoresInvalidos) {
            boolean rejeitou = false;
            try {
                new ZonaRestrita(identificadorInvalido, "Casa do Reator", 1.0);
            } catch (IllegalArgumentException erro) {
                rejeitou = true;
            }
            verificar(rejeitou, "Não pode existir zona sem identificação.");
        }

        double[] limitesInvalidos = {0.0, -1.0, 1000001.0};
        for (double limiteInvalido : limitesInvalidos) {
            boolean rejeitou = false;
            try {
                new ZonaRestrita("ZNR-01", "Casa do Reator", limiteInvalido);
            } catch (IllegalArgumentException erro) {
                rejeitou = true;
            }
            verificar(rejeitou, "O construtor deve rejeitar limite inválido.");
        }
    }

    private static void testarValidacaoAlerta() {
        ZonaRestrita casaReator = new ZonaRestrita("ZNR-01", "Casa do Reator", 1.0);
        SensorRadiacao sensorReator = new SensorRadiacao("RAD-01", casaReator);

        String[] severidadesInvalidas = {"ALTO", "NORMAL", "", null};
        for (String severidadeInvalida : severidadesInvalidas) {
            boolean rejeitou = false;
            try {
                new AlertaSeguranca(sensorReator, severidadeInvalida, "Radiação acima do limite.");
            } catch (IllegalArgumentException erro) {
                rejeitou = true;
            }
            verificar(rejeitou, "O alerta só pode aceitar ATENCAO ou CRITICO.");
        }

        boolean rejeitou = false;
        try {
            new AlertaSeguranca(null, "CRITICO", "Radiação acima do limite.");
        } catch (IllegalArgumentException erro) {
            rejeitou = true;
        }
        verificar(rejeitou, "O alerta precisa de um dispositivo de origem.");

        String[] mensagensInvalidas = {"   ", null};
        for (String mensagemInvalida : mensagensInvalidas) {
            rejeitou = false;
            try {
                new AlertaSeguranca(sensorReator, "CRITICO", mensagemInvalida);
            } catch (IllegalArgumentException erro) {
                rejeitou = true;
            }
            verificar(rejeitou, "O alerta precisa de uma mensagem.");
        }
    }

    private static void testarCentral() {
        ZonaRestrita casaReator = new ZonaRestrita("ZNR-01", "Casa do Reator", 1.0);
        ZonaRestrita patioArmazenamento = new ZonaRestrita("ZNR-02", "Pátio de Armazenamento", 1.0);
        SensorRadiacao sensorReator = new SensorRadiacao("RAD-01", casaReator);
        SensorRadiacao sensorPatio = new SensorRadiacao("RAD-02", patioArmazenamento);
        SensorPerimetral sensorCerca = new SensorPerimetral("PER-01", patioArmazenamento, "Cerca do pátio");
        DronePatrulha dronePatrulha = new DronePatrulha("DRN-01", casaReator, "Reator até o pátio");

        DispositivoMonitoramento[] dispositivos = {dronePatrulha, sensorCerca, sensorReator, sensorPatio};
        String[] codigos = {"DRN-01", "PER-01", "RAD-01", "RAD-02"};
        for (int indice = 0; indice < dispositivos.length; indice++) {
            verificar(codigos[indice].equals(dispositivos[indice].getCodigoIdentificador()),
                    "As subclasses devem usar o identificador herdado.");
        }

        CentralMonitoramento central = new CentralMonitoramento(dispositivos);
        sensorReator.registrarLeitura(0.2);
        sensorPatio.registrarLeitura(0.3);
        dronePatrulha.iniciarPatrulha();
        verificar(central.analisarLeituras().size() == 0, "Rotina normal não deve gerar alerta.");

        sensorReator.registrarLeitura(4.7);
        List<AlertaSeguranca> alertasRadiacao = central.analisarLeituras();
        verificar(alertasRadiacao.size() == 1, "O pico deve gerar um alerta.");
        verificar("CRITICO".equals(alertasRadiacao.get(0).getSeveridade()), "O pico deve gerar alerta crítico.");
        verificar(alertasRadiacao.get(0).getSensorOrigem() == sensorReator, "O alerta deve guardar o mesmo sensor.");
        verificar(alertasRadiacao.get(0).getZonaAfetada() == casaReator, "O alerta deve guardar a mesma zona.");

        sensorReator.registrarLeitura(0.8);
        verificar("ATENCAO".equals(central.analisarLeituras().get(0).getSeveridade()), "A central deve emitir atenção na faixa intermediária.");
        sensorReator.registrarLeitura(0.2);
        sensorCerca.registrarViolacao(true);
        dronePatrulha.registrarImagemSimulada(true);
        verificar(!dronePatrulha.isVeiculoNaoIdentificado(), "A confirmação deve ocorrer só durante a inspeção.");

        List<AlertaSeguranca> alertasAcesso = central.analisarLeituras();
        verificar(alertasAcesso.size() == 2, "A violação e a confirmação devem gerar dois alertas.");
        verificar(alertasAcesso.get(0).getSensorOrigem() == sensorCerca, "A cerca deve ser a origem do primeiro alerta.");
        verificar("ATENCAO".equals(alertasAcesso.get(0).getSeveridade()), "A violação deve iniciar a inspeção com atenção.");
        verificar(alertasAcesso.get(1).getSensorOrigem() == dronePatrulha, "O drone deve ser a origem da confirmação.");
        verificar("CRITICO".equals(alertasAcesso.get(1).getSeveridade()), "A confirmação deve gerar alerta crítico.");
        verificar(alertasAcesso.get(1).getZonaAfetada() == patioArmazenamento, "O alerta da câmera deve indicar o pátio.");
        verificar(dronePatrulha.getLocalizacao() == patioArmazenamento, "A central deve enviar o drone até a zona afetada.");

        dronePatrulha.iniciarPatrulha();
        List<AlertaSeguranca> alertasSemVeiculo = central.analisarLeituras();
        verificar(alertasSemVeiculo.size() == 1, "Cerca violada sem veículo deve gerar apenas o alerta perimetral.");
        verificar(!dronePatrulha.isVeiculoNaoIdentificado(), "A câmera não deve inventar uma confirmação.");

        dronePatrulha.atualizarBateria(0);
        dronePatrulha.registrarImagemSimulada(true);
        verificar(central.analisarLeituras().size() == 1, "Drone sem bateria não deve confirmar o acesso.");
        sensorCerca.registrarViolacao(false);
        verificar(central.analisarLeituras().size() == 0, "A central deve encerrar sem alertas quando as leituras voltarem ao normal.");
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
        verificacoes++;
    }
}
