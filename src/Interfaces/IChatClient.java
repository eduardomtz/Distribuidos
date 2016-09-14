//===============================================================
// Interfaz Cliente Chat - paracallback
//===============================================================


package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Mensaje.Message;

/*
  Este script maneja la lógica del cliente:
  - Recepción de mensajes.
  - Subscripción y abandono de canales.
  - Subscripción y abandono de tópicos.
 */

public interface IChatClient extends java.rmi.Remote {

    // Entrada y salida.
    void receiveEnter(String name) throws RemoteException;
    void receiveExit(String name) throws RemoteException;

    // Recepción de mensajes.
    void receiveMessage(Message message) throws RemoteException;

    // Subscripción y abandono de tópicos.
    void receiveSubscribeToTopic(String name, String canal, String topico) throws RemoteException;
    void receiveUnsubscribeToTopic(String name, String canal, String topico)throws RemoteException;

    // Subscripciónn y abandono de canales.
    void receiveSubscribeToChannel(String name, String canal) throws RemoteException;
    void receiveUnsubscribeToChannel(String name, String canal) throws RemoteException;

}