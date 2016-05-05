package szolanc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class InteractiveClient {

    final int PORT = 32123;
    Socket client;
    PrintWriter output;
    Scanner fromServer;
    Scanner keyInput;
    String lastGotWord;

    InteractiveClient() throws IOException {
        client = new Socket("localhost", PORT);
        output = new PrintWriter(client.getOutputStream(), true);
        fromServer = new Scanner(client.getInputStream());
        keyInput = new Scanner(System.in);
        lastGotWord = "";

        System.out.print("Please enter your name: ");
        output.println(keyInput.nextLine());

        new Thread() {
            @Override
            public void run() {
                while (handleStuff()) {
                }
            }
        }.start();
    }

    public boolean validator(String word, String serverInput) {
        boolean valid = false;
        if (word.charAt(0) == serverInput.charAt(serverInput.length() - 1)
                && word.matches("[a-z]+")) {
            valid = true;
        }
        return valid;
    }

    public boolean handleStuff() {
        boolean workStatus = true;
        String toBeSent;
        //String lastGotWord = "";

        String msg = fromServer.nextLine();
        System.out.println("from server: " + msg);

        switch (msg) {
            case "nyert":
                workStatus = false;
                break;

            case "ok":
                break;

            case "nok":
                System.out.print("Gepelj szot: ");
                toBeSent = keyInput.nextLine();
                msg = lastGotWord;
                
                //System.out.println("NOKeleje msg: " + msg);
               // System.out.println("NOKeleje lastGotWord: " + lastGotWord);

                while (!toBeSent.matches("[a-z]+")) {
                    System.out.print("Gepelj helyes szot: ");
                    toBeSent = keyInput.nextLine();
                }

                if (!("start").equals(msg) && !("exit").equals(toBeSent)) {

                    while (!validator(toBeSent, msg)) {
                        System.out.print("Gepelj helyes szot: ");
                        toBeSent = keyInput.nextLine();
                        if ("exit".equals(toBeSent)) {
                            output.println(toBeSent);
                            System.out.println("Feladom");
                            break;
                        }
                    }
                }

                if (!"nok".equals(msg)) {
                   // System.out.println("NOKvege: msg: " + msg);
                    lastGotWord = msg;
                    //System.out.println("NOKvege:lastGotWord: " + lastGotWord);
                }
                output.println(toBeSent);

                if ("exit".equals(toBeSent)) {
                    output.println(toBeSent);
                    workStatus = false;
                }

                break;

            default:
                System.out.print("Gepelj szot: ");
                toBeSent = keyInput.nextLine();
                
                //System.out.println("DEFAULTeleje msg: " + msg);
                //System.out.println("NOKeleje lastGotWord: " + lastGotWord);
                
                while (!toBeSent.matches("[a-z]+")) {
                    System.out.print("Gepelj helyes szot: ");
                    toBeSent = keyInput.nextLine();
                }

                if (!("start").equals(msg) && !("exit").equals(toBeSent)) {

                    while (!validator(toBeSent, msg)) {
                        System.out.print("Gepelj helyes szot: ");
                        toBeSent = keyInput.nextLine();
                        if ("exit".equals(toBeSent)) {
                            output.println(toBeSent);
                            System.out.println("Feladom");
                            break;
                        }
                    }
                }
                
                if (!"nok".equals(msg)) {
                  //  System.out.println("DEFAULTvege msg: " + msg);
                    lastGotWord = msg;
                   // System.out.println("DEFAULTvege lastGotWord: " + lastGotWord);
                }
                output.println(toBeSent);

                if ("exit".equals(toBeSent)) {
                    output.println(toBeSent);
                    workStatus = false;
                }

                break;
        }
        return workStatus;
    }

    public static void main(String[] args) {
        try {
            new InteractiveClient();
        } catch (IOException e) {
            System.out.println("Interaktiv kliens nemtudott elindulni");
        }
    }
}
