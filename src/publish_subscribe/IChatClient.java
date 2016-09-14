/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publish_subscribe;

/**
 *
 * @author eduardomartinez
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClient extends java.rmi.Remote {
    void receiveEnter(String name) throws RemoteException;
    void receiveExit(String name)throws RemoteException;
    void receiveMessage(Mensaje message) throws RemoteException;
}