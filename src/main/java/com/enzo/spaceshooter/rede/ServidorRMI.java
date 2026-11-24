package com.enzo.spaceshooter.rede;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Ponto de entrada do servidor RMI.
 * Registra a implementação de IGameSession no RMI Registry
 * e aguarda conexões de clientes.
 *
 * Como executar:
 * mvn exec:java -Dexec.mainClass="com.enzo.spaceshooter.rede.ServidorRMI"
 *
 * Ou após compilar:
 * java -cp target/classes com.enzo.spaceshooter.rede.ServidorRMI
 */
public class ServidorRMI {

    /** Porta padrão do RMI Registry */
    public static final int PORTA = 1099;

    /** Nome pelo qual o serviço é registrado no RMI Registry */
    public static final String NOME_SERVICO = "GameSession";

    public static void main(String[] args) {
        try {
            // Cria e exporta a implementação da sessão de jogo
            GameSessionImpl sessao = new GameSessionImpl();

            // Inicia (ou reutiliza) o RMI Registry na porta padrão
            Registry registry = LocateRegistry.createRegistry(PORTA);

            // Registra o objeto remoto com o nome do serviço
            registry.rebind(NOME_SERVICO, sessao);

            System.out.println("=========================================");
            System.out.println("  Space Shooter — Servidor RMI iniciado  ");
            System.out.println("  Porta  : " + PORTA);
            System.out.println("  Serviço: " + NOME_SERVICO);
            System.out.println("  Aguardando jogadores...");
            System.out.println("=========================================");

        } catch (Exception e) {
            System.err.println("[Servidor] Erro ao iniciar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
