package com.enzo.spaceshooter.rede;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameSession extends Remote {

    int joinGame(String playerName) throws RemoteException;

    void leaveGame(int playerId) throws RemoteException;

    void sendInput(int playerId, PlayerInputData input) throws RemoteException;

    GameStateSnapshot getGameState() throws RemoteException;

    boolean isGameRunning() throws RemoteException;

    int getPlayerCount() throws RemoteException;
}