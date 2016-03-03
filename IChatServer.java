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

public interface IChatServer extends java.rmi.Remote {
    void login(String name, IChatClient newClient) throws RemoteException; 
    void logout(String name)throws RemoteException;
    void send(Mensaje message) throws RemoteException;
    
    //Canal 
    void subscribeChannel(String nameCliente, IChatClient cliente, String canal)throws RemoteException;
    void unsubscribeChannel(String nameC, String canal) throws RemoteException;
    
    void sendBroadcast(Mensaje message)throws RemoteException;
    void getCurrentChannels(String name) throws RemoteException;
    
    void getAllChannels() throws RemoteException;
}