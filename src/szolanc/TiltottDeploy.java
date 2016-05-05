package szolanc;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TiltottDeploy {

    private static Registry reg;
    private static final int PORT = 12345; 

    public TiltottDeploy(int n) throws RemoteException {
        for (int i = 1; i <= n; i++) {
            String forb_name = "tiltott" + i;
            try {
                reg.rebind(forb_name, new TiltottSzerver(forb_name));
            } catch(RemoteException ex) {
                System.out.println("Hiba keletkezett " + forb_name + "-nel");
            }
            System.out.println(forb_name + " letrehozva");
        }
    }
    
    public static void main(String[] args) throws RemoteException {
        if(args.length == 1 && Integer.parseInt(args[0]) >= 1) {
            try {
                reg = LocateRegistry.createRegistry(PORT);
            } catch (RemoteException ex) {
                System.out.println("Regisztri letrehozaskor hiba lepett fel");
        }
        int n = Integer.parseInt(args[0]);
        new TiltottDeploy(n);
        } else {
            System.out.println("Parameter hiba kell 1 db min 1-es erteku parameter");
            return;
        }
    }
    
}
