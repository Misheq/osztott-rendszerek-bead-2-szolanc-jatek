package szolanc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class TiltottSzerver extends UnicastRemoteObject implements TiltottSzerverInterface {

    public List<String> forbiddenWords;

    public TiltottSzerver(String name) throws RemoteException {
        try {
            forbiddenWords = readFile(name + ".txt");
        } catch (IOException ex) {
            System.out.println("Nincs ilyen nevu fajl " + name);
        }
    }

    private List<String> readFile(String filename) throws IOException {
        List<String> list = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            BufferedReader br = new BufferedReader(reader);
            String line;
            while (br.ready()) {
                line = br.readLine();
                list.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nincs ilyen nevu file " + filename);
        }
        return list;
    }
    
    @Override
    public boolean tiltottE(String szo) throws RemoteException {
        boolean contains = forbiddenWords.contains(szo);
        if(!contains) {
            forbiddenWords.add(szo);
        }
        return contains;    // if true -> nok, else ok
    }
}
