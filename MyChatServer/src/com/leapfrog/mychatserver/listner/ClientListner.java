/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leapfrog.mychatserver.listner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Manjul Sigdel
 */
public class ClientListner extends Thread {

    private Client client;
    private ClientHandler handler;
    private Socket socket;
    PrintStream ps;
    BufferedReader reader;

    public ClientListner(Socket socket, ClientHandler handler) throws IOException {
        this.socket = socket;
        this.handler = handler;
        ps = new PrintStream(this.socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            ps.println("Hello From Server");
            ps.println("Enter your name:");
            String name = reader.readLine();
            client = new Client(socket, name);
            handler.addClient(client);
            ps.println("Thank You " + name);
            handler.broadcastNewUser(client, name + " entered the chat");
            while (!isInterrupted()) {
                // ps.print(client.getUserName() + ":");
                String cmd = reader.readLine();
                String[] tokens = cmd.split(";;");
                if (tokens[0].equalsIgnoreCase("pm")) {
                    if (tokens.length > 2) {
                        handler.privateMessage(client, tokens[1], tokens[2]);
                    }
                    
                } else if (tokens[0].equalsIgnoreCase("list")) {
                    StringBuilder content=new StringBuilder();
                    for(Client c:handler.getClients()){
                        content.append(c.getUserName()).append("\r\n");
                    }
                    handler.privateMessage(client, content.toString());
                    
                } 
                else {
                    handler.broadcastMessage(client, cmd);
                }
            }
        } catch (IOException ioe) {

        }
    }

}
