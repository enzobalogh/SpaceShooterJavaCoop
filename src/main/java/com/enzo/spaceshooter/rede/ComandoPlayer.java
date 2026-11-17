package com.enzo.spaceshooter.rede;

import java.io.Serializable;

public class ComandoPlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    private final AcaoPlayer acao;
    private final boolean pressionado;

    public ComandoPlayer(AcaoPlayer acao, boolean pressionado) {
        this.acao = acao;
        this.pressionado = pressionado;
    }

    public AcaoPlayer getAcao() {
        return acao;
    }

    public boolean isPressionado() {
        return pressionado;
    }
}
