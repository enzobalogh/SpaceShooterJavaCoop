package com.enzo.spaceshooter.rede;

import java.io.Serializable;

public class PlayerInputData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int dx;
    private final int dy;
    private final boolean shooting;

    public PlayerInputData(int dx, int dy, boolean shooting) {
        this.dx = dx;
        this.dy = dy;
        this.shooting = shooting;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public boolean isShooting() {
        return shooting;
    }
}