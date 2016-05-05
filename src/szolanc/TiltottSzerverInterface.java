package szolanc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TiltottSzerverInterface extends Remote {

    boolean tiltottE(String szo) throws RemoteException;
}
