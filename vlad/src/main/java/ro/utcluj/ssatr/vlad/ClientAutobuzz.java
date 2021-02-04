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
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
/**
 *
 * @author lenovo
 */
public class ClientAutobuzz extends Thread {
    BufferedReader fluxIn;
    PrintWriter fluxOut;
     public ClientAutobuzz() throws IOException {
        Socket s = new Socket("127.0.0.1", 9000);
        fluxIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        fluxOut = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
    }
      private Autobuz initAutobuz() {
        int AutobuzNum = parseInt(RandomStringUtils.randomNumeric(3));
       
        return new Autobuz(AutobuzNum);
    }

    private int getAutobuzString(Autobuz a) {

        return a.getNumber();
    }
    
    public void run() {
        while (true) {
            String statie;
            try {
                Autobuz a = initAutobuz();
                int Autobuz = getAutobuzString(a);
                fluxOut.println(Autobuz);
                statie = fluxIn.readLine();
                StringTokenizer st = new StringTokenizer(statie);
                String stopTime = st.nextToken();
                String statia = st.nextToken();
                if (parseInt(stopTime) / 1000 != 0) {
                       System.out.println("Autobuzul cu nr:  " + a.getNumber() + " a ajuns in  "+statia+"! Acesta va stationa " + parseInt(stopTime) / 1000 + " minute!");
                    } else {
                        System.out.println("Autobuzul cu nr:  "  + a.getNumber() + " nu opreste in" +statia);
                    }
                try {
                    Thread.sleep(parseInt(stopTime));
                    if (parseInt(stopTime) / 1000 != 0) {
                        System.out.println("Autobuzul cu nr: "  + a.getNumber() + " a parasit statia!");
                    } else {
                        System.out.println("Autobuzul  cu nr:"  + a.getNumber() + " a trecut prin statie!");
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientAutobuzz.class.getName()).log(Level.SEVERE, null, ex);
                }
                fluxOut.println("Parasire " + statia);
            } catch (IOException ex) {
                Logger.getLogger(ClientAutobuzz.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }

        }
    }

    public void sendMessage(String msg) {
        fluxOut.println(msg);
    }

    public static void main(String[] args) throws IOException {
        ClientAutobuzz auto = new ClientAutobuzz();
        auto.start();
        
    }
    
}
