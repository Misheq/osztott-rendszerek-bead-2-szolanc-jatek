package szolanc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SzolancSzimulacio {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                new Server();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos1", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Kliens 1 nemtudott elindulni");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos2", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Kliens 2 nemtudott elindulni");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos3", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Kliens 3 nemtudott elindulni");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos4", "szokincs2.txt");
                } catch (IOException ex) {
                    System.out.println("Kliens 4 nemtudott elindulni");
                }
            }
        }.start();
    }
}
