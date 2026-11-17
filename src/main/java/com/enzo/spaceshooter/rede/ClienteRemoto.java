package com.enzo.spaceshooter.rede;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteRemoto extends Remote {
    void atualizarEstado(EstadoJogo jogo) throws RemoteException;

}
