package com.enzo.spaceshooter.rede;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSessionImpl extends UnicastRemoteObject implements IGameSession {

    private static final long serialVersionUID = 1L;
    private static final int MAX_JOGADORES = 2;

    // Estado dos jogadores: id -> posição e vidas
    private final ConcurrentHashMap<Integer, int[]> posicoes = new ConcurrentHashMap<>();
    // id -> nome
    private final ConcurrentHashMap<Integer, String> jogadores = new ConcurrentHashMap<>();

    private final AtomicInteger proximoId = new AtomicInteger(1);
    private volatile StatusPartida statusPartida = StatusPartida.AGUARDANDO_JOGADORES;
    private volatile int pontuacaoCompartilhada = 0;

    // Lista de inimigos gerenciada pelo servidor
    private final List<InimigoEstado> inimigos = new ArrayList<>();
    private boolean inimigosInicializados = false;

    public GameSessionImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized int joinGame(String playerName) throws RemoteException {
        if (jogadores.size() >= MAX_JOGADORES) {
            System.out.println("[Servidor] Partida cheia. " + playerName + " não pode entrar.");
            return -1;
        }

        int id = proximoId.getAndIncrement();
        jogadores.put(id, playerName);

        // Posição inicial: player1 começa em (100,300), player2 em (100,500)
        int yInicial = (id == 1) ? 300 : 500;
        posicoes.put(id, new int[] { 100, yInicial, 3 }); // {x, y, vidas}

        System.out.println("[Servidor] Jogador '" + playerName + "' entrou com id=" + id
                + ". Total: " + jogadores.size());

        if (jogadores.size() == MAX_JOGADORES) {
            statusPartida = StatusPartida.EM_ANDAMENTO;
            inicializarInimigos();
            System.out.println("[Servidor] Partida iniciada!");
        }

        return id;
    }

    @Override
    public synchronized void leaveGame(int playerId) throws RemoteException {
        String nome = jogadores.remove(playerId);
        posicoes.remove(playerId);

        if (nome != null) {
            System.out.println("[Servidor] Jogador id=" + playerId + " (" + nome + ") saiu.");
        }

        if (statusPartida == StatusPartida.EM_ANDAMENTO && jogadores.isEmpty()) {
            statusPartida = StatusPartida.FINALIZADA;
            System.out.println("[Servidor] Partida finalizada (sem jogadores).");
        }
    }

    @Override
    public void sendInput(int playerId, PlayerInputData input) throws RemoteException {
        int[] pos = posicoes.get(playerId);
        if (pos == null || input == null)
            return;

        // Aplica movimento com limites de tela (1920 x 1080)
        int novoX = Math.max(0, Math.min(1870, pos[0] + input.getDx()));
        int novoY = Math.max(0, Math.min(1030, pos[1] + input.getDy()));
        pos[0] = novoX;
        pos[1] = novoY;

        // Disparo: apenas incrementa pontuação como placeholder
        if (input.isShooting()) {
            verificarColisaoTiro(playerId, novoX, novoY);
        }
    }

    @Override
    public GameStateSnapshot getGameState() throws RemoteException {
        int[] pos1 = posicoes.getOrDefault(1, new int[] { 0, 0, 0 });
        int[] pos2 = posicoes.getOrDefault(2, new int[] { 0, 0, 0 });

        PlayerEstado estadoP1 = new PlayerEstado(pos1[0], pos1[1], pos1[2]);
        PlayerEstado estadoP2 = new PlayerEstado(pos2[0], pos2[1], pos2[2]);

        // Remove inimigos não visíveis
        List<InimigoEstado> snapshot;
        synchronized (inimigos) {
            snapshot = new ArrayList<>(inimigos);
        }

        // Verifica condição de vitória
        if (statusPartida == StatusPartida.EM_ANDAMENTO && snapshot.isEmpty() && inimigosInicializados) {
            statusPartida = StatusPartida.FINALIZADA;
        }

        return new GameStateSnapshot(estadoP1, estadoP2, snapshot, pontuacaoCompartilhada, statusPartida);
    }

    @Override
    public boolean isGameRunning() throws RemoteException {
        return statusPartida == StatusPartida.EM_ANDAMENTO;
    }

    @Override
    public int getPlayerCount() throws RemoteException {
        return jogadores.size();
    }

    // -------------------------------------------------------------------------
    // Métodos internos
    // -------------------------------------------------------------------------

    private void inicializarInimigos() {
        synchronized (inimigos) {
            inimigos.clear();
            for (int i = 0; i < 8; i++) {
                int x = (int) (Math.random() * 8000 + 1920);
                int y = (int) (Math.random() * 650 + 50);
                inimigos.add(new InimigoEstado(x, y));
            }
            inimigosInicializados = true;
        }
        System.out.println("[Servidor] " + inimigos.size() + " inimigos inicializados.");
    }

    /**
     * Verifica se o tiro do jogador acertou algum inimigo.
     * Remove inimigos atingidos e incrementa pontuação.
     */
    private void verificarColisaoTiro(int playerId, int xPlayer, int yPlayer) {
        synchronized (inimigos) {
            inimigos.removeIf(inimigo -> {
                int dx = Math.abs(inimigo.getX() - xPlayer);
                int dy = Math.abs(inimigo.getY() - yPlayer);
                boolean acertou = dx < 60 && dy < 60;
                if (acertou) {
                    pontuacaoCompartilhada += 10;
                    System.out.println("[Servidor] Inimigo abatido por player " + playerId
                            + "! Pontuação: " + pontuacaoCompartilhada);
                }
                return acertou;
            });
        }
    }
}
