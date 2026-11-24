package com.enzo.spaceshooter.rede;

import java.io.Serializable;
import java.util.List;

public class GameStateSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PlayerEstado player1;
    private final PlayerEstado player2;
    private final List<InimigoEstado> inimigos;
    private final int pontuacaoCompartilhada;
    private final StatusPartida statusPartida;

    public GameStateSnapshot(PlayerEstado player1,
            PlayerEstado player2,
            List<InimigoEstado> inimigos,
            int pontuacaoCompartilhada,
            StatusPartida statusPartida) {
        this.player1 = player1;
        this.player2 = player2;
        this.inimigos = inimigos;
        this.pontuacaoCompartilhada = pontuacaoCompartilhada;
        this.statusPartida = statusPartida;
    }

    public PlayerEstado getPlayer1() {
        return player1;
    }

    public PlayerEstado getPlayer2() {
        return player2;
    }

    public List<InimigoEstado> getInimigos() {
        return inimigos;
    }

    public int getPontuacaoCompartilhada() {
        return pontuacaoCompartilhada;
    }

    public StatusPartida getStatusPartida() {
        return statusPartida;
    }
}
