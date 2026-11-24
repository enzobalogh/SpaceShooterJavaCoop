package com.enzo.spaceshooter.rede;

import java.io.Serializable;
import java.util.List;

public class EstadoJogo implements Serializable {

    private static final long serialVersionUID = 1L;
    private PlayerEstado player1;
    private PlayerEstado player2;
    private List<InimigoEstado> inimigos;
    private int pontuacaoCompartilhada;
    private StatusPartida estadoPartida;

    public EstadoJogo(PlayerEstado player1,
            PlayerEstado player2, List<InimigoEstado> inimigos, int pontuacaoCompartilhada,
            StatusPartida estadoPartida) {

        this.player1 = player1;
        this.player2 = player2;
        this.inimigos = inimigos;
        this.pontuacaoCompartilhada = pontuacaoCompartilhada;
        this.estadoPartida = estadoPartida;
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

}
