package com.enzo.spaceshooter.rede;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Cliente RMI do Space Shooter.
 * Conecta ao servidor, entra na partida e fica em loop
 * enviando inputs e lendo o estado do jogo.
 *
 * Como executar (substitua HOST e NOME pelo desejado):
 * java -cp target/classes com.enzo.spaceshooter.rede.ClienteRMI localhost
 * Player1
 *
 * Argumentos opcionais:
 * args[0] — host do servidor (padrão: localhost)
 * args[1] — nome do jogador (padrão: Player)
 */
public class ClienteRMI {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        String nomePlayer = args.length > 1 ? args[1] : "Player";

        try {
            // Localiza o RMI Registry no servidor
            Registry registry = LocateRegistry.getRegistry(host, ServidorRMI.PORTA);

            // Busca o stub remoto de IGameSession
            IGameSession sessao = (IGameSession) registry.lookup(ServidorRMI.NOME_SERVICO);

            System.out.println("[Cliente] Conectado ao servidor em " + host);
            System.out.println("[Cliente] Entrando na partida como '" + nomePlayer + "'...");

            // Tenta entrar na partida
            int playerId = sessao.joinGame(nomePlayer);

            if (playerId == -1) {
                System.out.println("[Cliente] Partida cheia ou não disponível. Encerrando.");
                return;
            }

            System.out.println("[Cliente] Entrou! id=" + playerId);
            System.out.println("[Cliente] Aguardando o início da partida...");

            // Aguarda a partida começar (máx. 60 s)
            long inicio = System.currentTimeMillis();
            while (!sessao.isGameRunning()) {
                if (System.currentTimeMillis() - inicio > 60_000) {
                    System.out.println("[Cliente] Tempo limite de espera atingido. Saindo.");
                    sessao.leaveGame(playerId);
                    return;
                }
                System.out.println("[Cliente] Aguardando o 2º jogador... ("
                        + sessao.getPlayerCount() + "/2)");
                Thread.sleep(2000);
            }

            System.out.println("[Cliente] Partida iniciada! Pressione Ctrl+C para sair.");

            // -----------------------------------------------------------------------
            // Loop principal do cliente: envia inputs e lê o estado do jogo
            // -----------------------------------------------------------------------
            // Neste exemplo o cliente envia um input neutro e imprime o estado.
            // Na integração com a Fase (UI), este loop deve rodar em thread separada
            // e os inputs reais (dx, dy, shooting) virão do KeyAdapter da Fase.
            // -----------------------------------------------------------------------
            int ciclo = 0;
            while (sessao.isGameRunning()) {
                // Exemplo de input: move ligeiramente para a direita e atira a cada 10 ciclos
                boolean atirar = (ciclo % 10 == 0);
                PlayerInputData input = new PlayerInputData(2, 0, atirar);
                sessao.sendInput(playerId, input);

                // Lê o estado atual do jogo
                GameStateSnapshot estado = sessao.getGameState();
                imprimirEstado(estado, playerId);

                ciclo++;
                Thread.sleep(100); // ~10 atualizações por segundo
            }

            System.out.println("[Cliente] Partida encerrada.");
            GameStateSnapshot estadoFinal = sessao.getGameState();
            System.out.println("[Cliente] Resultado final — Pontuação: "
                    + estadoFinal.getPontuacaoCompartilhada()
                    + " | Status: " + estadoFinal.getStatusPartida());

            sessao.leaveGame(playerId);
            System.out.println("[Cliente] Desconectado.");

        } catch (Exception e) {
            System.err.println("[Cliente] Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Utilitário: imprime estado resumido no console
    // -------------------------------------------------------------------------
    private static void imprimirEstado(GameStateSnapshot estado, int meuId) {
        if (estado == null)
            return;

        PlayerEstado eu = (meuId == 1) ? estado.getPlayer1() : estado.getPlayer2();
        PlayerEstado outro = (meuId == 1) ? estado.getPlayer2() : estado.getPlayer1();

        List<InimigoEstado> inimigos = estado.getInimigos();

        System.out.printf("[Cliente] Minha pos=(%d,%d) vidas=%d | Outro pos=(%d,%d) vidas=%d"
                + " | Inimigos=%d | Pontos=%d | Status=%s%n",
                eu != null ? eu.getX() : 0,
                eu != null ? eu.getY() : 0,
                eu != null ? eu.getVidas() : 0,
                outro != null ? outro.getX() : 0,
                outro != null ? outro.getY() : 0,
                outro != null ? outro.getVidas() : 0,
                inimigos != null ? inimigos.size() : 0,
                estado.getPontuacaoCompartilhada(),
                estado.getStatusPartida());
    }
}
