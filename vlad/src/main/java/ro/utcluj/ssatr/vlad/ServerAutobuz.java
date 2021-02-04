/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.utcluj.ssatr.vlad;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Vlad
 */
public class ServerAutobuz {
    
    ArrayList<ClientHandler> clients = new ArrayList<>();
    List<String> statiiactive = Collections.synchronizedList(new ArrayList(Arrays.asList("Statia-Marasti", "Statia-Constantin-Prezan", "Statia-Iulius-Mall","Statia-Nicaieri","Statia-Cainele-Fricos")));
    List<String> statiiocupate = Collections.synchronizedList(new ArrayList<>());
    public void startServer() {

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(9000);
        } catch (IOException ex) {
            Logger.getLogger(ServerAutobuz.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                System.out.println("S-a conectat clientul?");
                Socket s = ss.accept();
                System.out.println("Autobuzul a sosit! (Clientul s-a conectat!)");
                ClientHandler h = new ClientHandler(s, this);
                h.start();
                clients.add(h);
            } catch (IOException ex) {
                Logger.getLogger(ServerAutobuz.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//.startServer}

    public static void main(String[] args) {
        ServerAutobuz srv = new ServerAutobuz();
        srv.startServer();
    }

    public String statiiocupate() {
        String statii = statiiactive.get(0);
        statiiactive.removeIf(statiiactive -> statiiactive.equals(statii));
        statiiocupate.add(statii);
        return statii;
    }

    void statiiactive(String statii) {
        
        statiiocupate.remove(statii);
        statiiactive.add(statii);
    }
}

//class


class ClientHandler extends Thread {

    Socket s;
    BufferedReader fluxIn;
    PrintWriter fluxOut;
    ServerAutobuz server;

    public ClientHandler(Socket s, ServerAutobuz server) throws IOException {
        this.s = s;
        fluxIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        fluxOut = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
        this.server = server;
    }

    private Autobuz getAutobuz(String Autobuz, String stopTime) {
        StringTokenizer st = new StringTokenizer(Autobuz);
        int number = Integer.parseInt(st.nextToken());
        return new Autobuz( number,parseInt(stopTime));
    }

    
public void run() {
        try {
            while (true) {

                String msg = fluxIn.readLine();
                String stopTime = RandomStringUtils.randomNumeric(4);
                Autobuz a = getAutobuz(msg, stopTime);
                String statii = server.statiiocupate();
               // System.out.println("Autobuzul a sosit" + msg);
                System.out.println("Autobuzul cu nr:   " + a.getNumber() + " se afla in statia " + statii + " pentru " + Integer.parseInt(stopTime) / 1000 + " minutes");
                String response = stopTime + " " + statii;
                sendMessage(response);
                msg = fluxIn.readLine();
                server.statiiactive(statii);
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    void sendMessage(String msg) {
        fluxOut.println(msg);
    }
    
}

