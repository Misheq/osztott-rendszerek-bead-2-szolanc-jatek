package szolanc;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SzolancJatek {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                try {
                    new Server();
                } catch (NotBoundException ex) {
                    System.out.println("Hiba a szerver inditasnal");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Robot", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Gepi kliens nem tudott elindulni");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new InteractiveClient();
                } catch (IOException ex) {
                    System.out.println("Jatekos kliens nem tudott elindulni");
                }
            }
        }.start();
    }
}
