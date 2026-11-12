package br.com.sentinela.model;

import java.util.ArrayList;
import java.util.List;

public class CentralMonitoramento {

    private DispositivoMonitoramento[] dispositivos;

    public CentralMonitoramento(DispositivoMonitoramento[] dispositivos) {
        this.setDispositivos(dispositivos);
    }

    public List<AlertaSeguranca> analisarLeituras() {
        List<AlertaSeguranca> alertas = new ArrayList<>();

        for (DispositivoMonitoramento dispositivo : this.dispositivos) {
            if (!dispositivo.isEmOperacao()) {
                continue;
            }

            if (dispositivo instanceof SensorRadiacao) {
                SensorRadiacao sensorRadiacao = (SensorRadiacao) dispositivo;
                String risco = sensorRadiacao.getLocalizacao().classificarRisco();
                if (!ZonaRestrita.RISCO_NORMAL.equals(risco)) {
                    alertas.add(new AlertaSeguranca(sensorRadiacao, risco,
                            "Radiação em " + sensorRadiacao.getLeituraAtual() + " µSv/h. Limite da zona: "
                                    + sensorRadiacao.getLimiteSeguranca() + " µSv/h."));
                }
            } else if (dispositivo instanceof SensorPerimetral) {
                SensorPerimetral sensorPerimetral = (SensorPerimetral) dispositivo;
                if (sensorPerimetral.isCercaViolada()) {
                    alertas.add(new AlertaSeguranca(sensorPerimetral, ZonaRestrita.RISCO_ATENCAO,
                            "Violação da cerca no setor " + sensorPerimetral.getSetorVigiado() + "."));
                    DronePatrulha droneEnviado = this.enviarDrone(sensorPerimetral.getLocalizacao());
                    if (droneEnviado != null && droneEnviado.isVeiculoNaoIdentificado()) {
                        alertas.add(new AlertaSeguranca(droneEnviado, ZonaRestrita.RISCO_CRITICO,
                                "Acesso indevido: a câmera confirmou a presença de um veículo não identificado."));
                    }
                }
            }
        }
        return alertas;
    }

    private DronePatrulha enviarDrone(ZonaRestrita zonaAfetada) {
        for (DispositivoMonitoramento dispositivo : this.dispositivos) {
            if (dispositivo instanceof DronePatrulha) {
                DronePatrulha dronePatrulha = (DronePatrulha) dispositivo;
                if (dronePatrulha.isEmOperacao() && dronePatrulha.getBateriaPercentual() > 0) {
                    dronePatrulha.inspecionarZona(zonaAfetada);
                    return dronePatrulha;
                }
            }
        }
        System.out.println("Erro: nenhum drone disponível para inspecionar " + zonaAfetada.getIdentificador() + ".");
        return null;
    }

    private void setDispositivos(DispositivoMonitoramento[] dispositivos) {
        if (dispositivos == null) {
            throw new IllegalArgumentException("Erro: informe os dispositivos da central.");
        }
        for (DispositivoMonitoramento dispositivo : dispositivos) {
            if (dispositivo == null) {
                throw new IllegalArgumentException("Erro: o array não pode conter dispositivos nulos.");
            }
        }
        this.dispositivos = dispositivos;
    }
}
