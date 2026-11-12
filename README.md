# Challenge Sprint 1 - POO

FIAP, 2º ano, 3º semestre. Programação Orientada a Objetos.

- João Vitor Anunciação Oliveira - RM567539
- Miguel Silva Bezerra - RM566763
- Paulo Ribeiro Marinho - RM567459

O SENTINELA é um programa em Java que simula o monitoramento de duas zonas restritas. A central analisa as leituras dos sensores e do drone e gera alertas.

O `main` mostra os três cenários na ordem do enunciado: rotina normal, pico de radiação e acesso indevido. No último, o drone é enviado ao local e a câmera simulada detecta um veículo não identificado.

## Como executar

Com um JDK 8 ou superior, abra o projeto no IntelliJ, marque `src` como **Sources Root**, se necessário, e execute a classe `br.com.sentinela.main.Principal`.

Pelo terminal, na pasta do projeto:

```sh
javac -encoding UTF-8 -d bin src/br/com/sentinela/model/*.java src/br/com/sentinela/main/Principal.java
java -cp bin br.com.sentinela.main.Principal
```

Como o enunciado não define as faixas, a simulação usa limite de 1,0 µSv/h: abaixo de 0,8 é `NORMAL`, de 0,8 até 1,0 é `ATENCAO` e acima de 1,0 é `CRITICO`. São valores fictícios para o exercício.
