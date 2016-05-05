package szolanc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotClient {

    final int PORT = 32123;
    Socket client;
    PrintWriter output;
    Scanner fromServer;
    List<String> wordList;
    String name;

    RobotClient(String name, String file) throws IOException {
        this.name = name;

        client = new Socket("localhost", PORT);
        output = new PrintWriter(client.getOutputStream(), true);
        fromServer = new Scanner(client.getInputStream());
        wordList = readFile(file);

        output.println(name);

        new Thread() {
            @Override
            public void run() {
                while (handleStuff()) {
                }
            }
        }.start();
    }

    public List<String> readFile(String filename) {
        List<String> ls = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            BufferedReader br = new BufferedReader(reader);
            String line;
            while (br.ready()) {
                line = br.readLine();
                ls.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nincs ilyen fajl hogy: " + filename);
        } catch (IOException ex) {
            System.out.println("Fajl kezeleskor hiba keletkezzet");
        }
        return ls;
    }

    public boolean handleStuff() {
        boolean workStatus = true;

        String msg = fromServer.nextLine();
        String lastGotWord = "";
        String toBeSent = "";
        String lastLetter = "";

        switch (msg) {
            case "start":
                output.println(wordList.get(0));
                System.out.println(name + ": " + wordList.get(0));
                wordList.remove(0);
                break;

            case "nyert":
                System.out.println(name + " " + msg);
                workStatus = false;
                break;

            case "ok":
                break;
                
            case "nok":
                toBeSent = "exit";
                workStatus = false;
                msg = lastGotWord; // ez itt a lenyeg, hogy a legutobbi szohoz viszonyitva keres megint masikat
                lastLetter = msg.substring(msg.length() - 1);

                for (int i = 0; i < wordList.size(); ++i) {
                    String firstLetter = wordList.get(i).substring(0, 1);
                    if (lastLetter.equals(firstLetter)) {
                        toBeSent = wordList.get(i);
                        wordList.remove(i);
                        workStatus = true;
                        break;
                    }
                }
                
                if (!"nok".equals(msg)) {
                    lastGotWord = msg;
                }
                
                output.println(toBeSent);
                System.out.println(name + ": " + toBeSent);
                break;
                
            default:
                toBeSent = "exit";
                workStatus = false;
                lastLetter = msg.substring(msg.length() - 1);

                for (int i = 0; i < wordList.size(); ++i) {
                    String firstLetter = wordList.get(i).substring(0, 1);
                    if (lastLetter.equals(firstLetter)) {
                        toBeSent = wordList.get(i);
                        wordList.remove(i);
                        workStatus = true;
                        break;
                    }
                }
                
                if (!"nok".equals(msg)) {
                    lastGotWord = msg;
                }
                
                output.println(toBeSent);
                System.out.println(name + ": " + toBeSent);
                break;
        }

        return workStatus;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Helytelen darab parameter");
            return;
        }
        try {
            new RobotClient(args[0], args[1]);
        } catch (IOException e) {
            System.out.println("A gepi kliens nemtudodd elindulni");
        }
    }
}
