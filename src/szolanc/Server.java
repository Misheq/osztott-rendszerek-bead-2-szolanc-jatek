package szolanc;

import java.util.*;
import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.*;

public class Server {

    final private int port = 32123;
    ServerSocket server;
    int numb = 1;

    public Server() throws NotBoundException, IOException {
        try {
            this.server = new ServerSocket(port);
            System.out.println("SERVER>> Szerver elindult a " + port + " port szamon");
            server.setSoTimeout(30000);
            handleClients();
        } catch (IOException e) {
            System.out.println("SERVER>> Szerver szoket szunet, több játékmenetet nem lehet elindítani");
        }
    }

    public void handleClients() throws IOException, NotBoundException {

        while (true) {
            try {
                Socket s1 = server.accept();
                Socket s2 = server.accept();
                Registry reg;
                TiltottSzerverInterface tszi;

                try {
                    reg = LocateRegistry.getRegistry(12345);
                    tszi = (TiltottSzerverInterface) reg.lookup("tiltott" + numb);
                } catch (Exception e) {
                    numb = 1;
                    reg = LocateRegistry.getRegistry(12345);
                    tszi = (TiltottSzerverInterface) reg.lookup("tiltott" + numb);
                }

                new Handler(s1, s2, tszi).start();
                numb++;
            } catch (IOException e) {
                System.out.println("SERVER>> Hiba kliensekkel");
            }
        }
    }

    public class Handler extends Thread {

        Socket player1, player2;
        String name1, name2;
        Scanner input1, input2;
        PrintWriter output1, output2;
        boolean isPlayer1;
        File fileName;
        FileWriter fw;
        TiltottSzerverInterface tszi;

        public void createFileName(String player1, String player2) throws IOException {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String formatedDate = sdf.format(date);

            String filename = player1 + "_" + player2 + "_" + formatedDate + ".txt";
            this.fileName = new File(filename);
            fw = new FileWriter(fileName);
        }

        public void writeToFile(String msg) throws IOException {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.write("\n");
            bw.flush();
        }

        Handler(Socket s1, Socket s2, TiltottSzerverInterface tszi) throws IOException {
            this.player1 = s1;
            this.player2 = s2;
            this.tszi = tszi;

            input1 = new Scanner(s1.getInputStream());
            input2 = new Scanner(s2.getInputStream());

            output1 = new PrintWriter(s1.getOutputStream(), true);
            output2 = new PrintWriter(s2.getOutputStream(), true);

            name1 = input1.nextLine();
            name2 = input2.nextLine();

            System.out.println("SERVER>> " + name1 + " csatlakozott");
            System.out.println("SERVER>> " + name2 + " csatlakozott");

            isPlayer1 = true;
            createFileName(name1, name2);

        }

        @Override
        public void run() {
            String msg;
            output1.println("start");
            while (true) {
                if (isPlayer1) {
                    try {
                        msg = input1.nextLine();//

                        while (tszi.tiltottE(msg)) {
                            output1.println("nok");
                            msg = input1.nextLine();
                        }
                        output1.println("ok");

                    } catch (Exception ex) {
                        System.out.println("SERVER>> " + name2 + " nyert");
                        output2.println("nyert");
                        break;
                    }

                    if (msg.equals("exit")) {
                        System.out.println("SERVER>> " + name2 + " nyert");
                        output2.println("nyert");
                        break;
                    }

                    output2.println(msg);
                    isPlayer1 = false;

                } else {
                    try {
                        msg = input2.nextLine();

                        while (tszi.tiltottE(msg)) {
                            output2.println("nok");
                            msg = input2.nextLine();
                        }
                        output2.println("ok");

                    } catch (Exception ex) {
                        System.out.println("SERVER>> " + name1 + " nyert");
                        output1.println("nyert");
                        break;
                    }

                    if (msg.equals("exit")) {
                        System.out.println("SERVER>> " + name1 + " nyert");
                        output1.println("nyert");
                        break;
                    }

                    output1.println(msg);
                    isPlayer1 = true;
                }
                try {
                    String s = (!isPlayer1) ? name1 + " " + msg : name2 + " " + msg;
                    writeToFile(s);
                } catch (IOException ex) {
                    System.out.println("SERVER>> file irasi hiba");
                }
            }
            try {
                player1.close();
                System.out.println("SERVER>> " + name1 + " tavozott");
                player2.close();
                System.out.println("SERVER>> " + name2 + " tavozott");
            } catch (IOException e) {
                System.out.println("SERVER>> Szoket zarasi hiba");
            }
        }
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        new Server();
    }
}
