package com.enzo.spaceshooter.rede;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServidorRemoto extends Remote {
    public int conectar(ClienteRemoto clienteRemoto) throws RemoteException; //retorno inteiro pois retorna o ID do usuário

    public boolean desconectar(int idJogador) throws RemoteException;

    public void enviarComando(int idJogador, ComandoPlayer comando) throws RemoteException;

}
