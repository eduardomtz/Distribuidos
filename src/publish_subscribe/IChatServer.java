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
    // Chat 
    void login(String name, IChatClient newClient) throws RemoteException; 
    void logout(String name)throws RemoteException;
    void send(Mensaje message, String cliente) throws RemoteException;
    
    // Canal 
    void subscribeChannel(String nameCliente, IChatClient cliente, String canal)throws RemoteException;
    void unsubscribeChannel(String nameCliente, String canal) throws RemoteException;
    void getCurrentChannels(String cliente) throws RemoteException;
    void getAllChannels(IChatClient cliente) throws RemoteException;
    
    // Topics
    void subscribeTopic(String nameCliente, String canal, String topico)throws RemoteException;
    void unsubscribeTopic(String nameCliente, String canal, String topico) throws RemoteException;
    void getCurrentTopics(String cliente) throws RemoteException;
    void getAllTopics(String cliente) throws RemoteException;
    
}