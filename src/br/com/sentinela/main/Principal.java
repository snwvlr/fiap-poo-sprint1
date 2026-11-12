package br.com.sentinela.main;

import br.com.sentinela.model.AlertaSeguranca;
import br.com.sentinela.model.CentralMonitoramento;
import br.com.sentinela.model.DispositivoMonitoramento;
import br.com.sentinela.model.DronePatrulha;
import br.com.sentinela.model.SensorPerimetral;
import br.com.sentinela.model.SensorRadiacao;
import br.com.sentinela.model.ZonaRestrita;

import java.util.List;

public class Principal {

    public static void main(String[] args) {
        System.out.println("SENTINELA - Sprint 1");
        System.out.println("Instalação fictícia. Leituras e imagens simuladas.\n");

        ZonaRestrita casaReator = new ZonaRestrita("ZNR-01", "Casa do Reator", 1.0);
        ZonaRestrita patioArmazenamento = new ZonaRestrita("ZNR-02", "Pátio de Armazenamento de Combustível Irradiado", 1.0);

        // Sem os três argumentos, esta linha não compila.
        // ZonaRestrita zonaFantasma = new ZonaRestrita();

        SensorRadiacao sensorReator = new SensorRadiacao("RAD-01", casaReator);
        SensorRadiacao sensorPatio = new SensorRadiacao("RAD-02", patioArmazenamento);
        SensorPerimetral sensorCerca = new SensorPerimetral("PER-01", patioArmazenamento, "Pátio");
        DronePatrulha dronePatrulha = new DronePatrulha("DRN-01", casaReator, "Casa do Reator até o pátio");

        DispositivoMonitoramento[] dispositivos = {sensorReator, sensorPatio, sensorCerca, dronePatrulha};
        CentralMonitoramento central = new CentralMonitoramento(dispositivos);

        System.out.println("1. ROTINA NORMAL");
        sensorReator.registrarLeitura(0.2);
        sensorPatio.registrarLeitura(0.3);
        sensorCerca.registrarViolacao(false);
        exibirLeituraRadiacao(sensorReator);
        exibirLeituraRadiacao(sensorPatio);
        System.out.println("Sensor " + sensorCerca.getCodigoIdentificador() + ": perímetro íntegro.");
        dronePatrulha.iniciarPatrulha();
        System.out.println("Bateria do drone: " + dronePatrulha.getBateriaPercentual() + "%");
        exibirAlertas(central.analisarLeituras());

        System.out.println("\n2. PICO DE RADIAÇÃO");
        sensorReator.registrarLeitura(4.7);
        exibirLeituraRadiacao(sensorReator);
        exibirLeituraRadiacao(sensorPatio);
        exibirAlertas(central.analisarLeituras());

        System.out.println("\n3. ACESSO INDEVIDO");
        // A radiação volta ao normal antes do teste da cerca.
        sensorReator.registrarLeitura(0.2);
        exibirLeituraRadiacao(sensorReator);
        exibirLeituraRadiacao(sensorPatio);
        sensorCerca.registrarViolacao(true);
        System.out.println("Sensor " + sensorCerca.getCodigoIdentificador()
                + ": cerca violada no setor " + sensorCerca.getSetorVigiado() + ".");
        dronePatrulha.registrarImagemSimulada(true);
        exibirAlertas(central.analisarLeituras());
    }

    private static void exibirLeituraRadiacao(SensorRadiacao sensorRadiacao) {
        ZonaRestrita zonaMonitorada = sensorRadiacao.getLocalizacao();
        System.out.println("Sensor " + sensorRadiacao.getCodigoIdentificador() + " | "
                + zonaMonitorada.getIdentificador() + " - " + zonaMonitorada.getDescricao()
                + " | Leitura: " + sensorRadiacao.getLeituraAtual() + " µSv/h"
                + " | Risco: " + zonaMonitorada.classificarRisco());
    }

    private static void exibirAlertas(List<AlertaSeguranca> alertas) {
        System.out.println("Alertas gerados pela central: " + alertas.size());
        for (AlertaSeguranca alerta : alertas) {
            System.out.println("[" + alerta.getSeveridade() + "] " + alerta.getMensagem());
            System.out.println("Origem: " + alerta.getSensorOrigem().getCodigoIdentificador()
                    + " | Zona: " + alerta.getZonaAfetada().getIdentificador()
                    + " - " + alerta.getZonaAfetada().getDescricao());
        }
    }
}
