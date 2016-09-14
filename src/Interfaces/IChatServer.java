//===============================================================
// Interfaz Servidor Chat
//===============================================================

package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Mensaje.Message;

/*
  Este script maneja la lógica del servidor:
  - Manejo de sesiones.
  - Subscripción y abandono de canales.
  - Subscripción y abandono de tópicos.
 */

public interface IChatServer extends java.rmi.Remote {

    // Registro y abandono de sesion.
    void login(String name, IChatClient newClient) throws RemoteException;
    void logout(String name) throws RemoteException;

    // Registro y abandono de canales.
    void loginChannel(String nameCliente, IChatClient nc, String canal)throws RemoteException;    
    void logoutChannel(String nameC, String canal) throws RemoteException;

    // Registro y abandono de topicos.
    void subscribeToTopic(String name, IChatClient newClient, String topico, String canal) throws RemoteException;    
    void unsubscribeToTopic(String name,String topico, String canal) throws RemoteException;

    /*
      Envio de mensajes bajo esquemas unicast y broadcast.
      ------------------------------------------------------------
      | TYPE      | ASSOCIATIONS     | SCOPE           | EXAMPLE |
      ------------------------------------------------------------
      | Unicast   | 1 to 1           | Whole network   | HTTP    | 
      ------------------------------------------------------------
      | Broadcast | 1 to Many        | Subnet          | ARP     |
      ------------------------------------------------------------
    */
    void sendUnicast(Message message, String toWhom)throws RemoteException;    
    void sendBroadcast(Message message)throws RemoteException;

    // Obtener logs, topicos y canales.
    void getAllTopics(String name, String canal)throws RemoteException;    
    void getAllChannels(String name) throws RemoteException;
    void getLog(String name, String canal, String topico)throws RemoteException;
}