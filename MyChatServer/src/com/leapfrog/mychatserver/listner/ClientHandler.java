/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leapfrog.mychatserver.listner;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manjul Sigdel
 */
public class ClientHandler {

    List<Client> clients;

    public ClientHandler() {
        this.clients = new ArrayList<>();
    }

    public void addClient(Client c) {
        clients.add(c);
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getByUserName(String userName) {
        for (Client c : clients) {
            if (c.getUserName().equalsIgnoreCase(userName)) {
                return c;
            }
        }
        return null;
    }

    public void broadcastNewUser(Client client, String message) throws IOException {
        for (Client c : clients) {
            if (!c.equals(client)) {
                PrintStream output = new PrintStream(c.getSocket().getOutputStream());
                output.println(message);
            }
        }
    }

    public void broadcastMessage(Client client, String message) throws IOException {
        for (Client c : clients) {
            if (!c.equals(client)) {
                PrintStream output = new PrintStream(c.getSocket().getOutputStream());
                output.println(client.getUserName() + " says >" + message);
            }
        }
    }

    public void privateMessage(Client client, String userName, String message) throws IOException {
        Client friend = getByUserName(userName);
        if (friend != null) {
            PrintStream output = new PrintStream(friend.getSocket().getOutputStream());
            output.println(client.getUserName() + " says (PM) >" + message);
        }
    }

    public void privateMessage(Client client, String message) throws IOException {

        PrintStream output = new PrintStream(client.getSocket().getOutputStream());
        output.println(message);

    }

}
