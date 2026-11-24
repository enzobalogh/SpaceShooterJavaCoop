package com.enzo.spaceshooter.rede;

import java.io.Serializable;

public class PlayerEstado implements Serializable {
    private int x, y;
    private int vidas;

    public PlayerEstado(int x, int y, int vidas) {
        this.x = x;
        this.y = y;
        this.vidas = vidas;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVidas() {
        return vidas;
    }

}
