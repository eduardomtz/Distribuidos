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
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.*;

public class ChatServer extends UnicastRemoteObject implements IChatServer { 
    Hashtable<String, IChatClient> chatters;
    Hashtable<String, Canal> Canales;
    
    public ChatServer() throws RemoteException {
        chatters = new Hashtable<String, IChatClient>();
        Canales = new Hashtable<String, Canal>();
    }
    
    public synchronized void login(String name, IChatClient nc) throws RemoteException { 
        chatters.put(name,nc);
        Enumeration entChater = chatters.elements(); 
        while(entChater.hasMoreElements()){
            ((IChatClient) entChater.nextElement()).receiveEnter(name); 
        }
        System.out.println("Client " + name + " ha iniciado sesión");
    }
    
    public synchronized void logout(String name) throws RemoteException { 
        Enumeration entChater = chatters.elements();
        while( entChater.hasMoreElements()) {
            ((IChatClient) entChater.nextElement()).receiveExit(name); 
        }
        chatters.remove(name);
        System.out.println("Client " + name + " has abandonado la sesión");
    }
    
    public synchronized void send(Mensaje message, String cliente) throws RemoteException { 
        //System.out.println("Message from client "+message.name+":\n"+message.text);
        Enumeration canales = Canales.elements();
        while(canales.hasMoreElements()){
            //el cliente esta en este canal
            Canal c = (Canal)canales.nextElement();
            if(c.chatters.containsKey(cliente))
            {
                List<String> ls = c.clientesTopicos.get(cliente);
                if(ls.size()>0)
                {
                    //Cliente esta en un topico, mandar mensaje solo a compañeros de topico
                    for(int i = 0;i<ls.size();i++)
                    {
                        //ls.get(i)
                        Enumeration entChater = c.chatters.keys(); // clientes
                        while(entChater.hasMoreElements() ) {
                            // si ese cliente tiene ese topico
                            String cliente_itera = (String)entChater.nextElement();
                            if(c.clientesTopicos.get(cliente_itera).contains(ls.get(i)))
                            {
                                chatters.get(cliente_itera).receiveMessage(message); 
                            }
                        }
                    }
                }
                else
                {
                    Enumeration entChater = c.chatters.keys(); // clientes
                    while(entChater.hasMoreElements() ) {
                        // enviar mensaje a los que tampoco tengan topico
                        String cliente_itera = (String)entChater.nextElement();
                        if(c.clientesTopicos.get(cliente_itera).size()==0)
                        {
                            chatters.get(cliente_itera).receiveMessage(message); 
                        }
                    }
                }
                // enviar el mensaje a todos los clientes de este canal
                
            }
        }
    }
    
    @Override
    public void subscribeChannel(String nameCliente, IChatClient cliente, String canal) throws RemoteException {
        if(Canales.containsKey(canal)){
            //Canal ya existe
            Canal suscribir = Canales.get(canal);
            suscribir.addClient(nameCliente, cliente);
            
            // enviar un mensaje a los suscriptores de ese canal
            Enumeration suscriptores = suscribir.getClients().elements();
            while(suscriptores.hasMoreElements())
            {
                Mensaje msj = new Mensaje(nameCliente, "se unió a canal " + canal);
                ((IChatClient)suscriptores.nextElement()).receiveMessage(msj);
            }
        }
        else
        {
            //Crear canal
            Canal nuevoCanal = new Canal(canal);
            nuevoCanal.addClient(nameCliente, cliente);
            Canales.put(canal, nuevoCanal);
            // enviar un mensaje a los suscriptores de nuevo canal que solo es el usuario actual
            Mensaje msj = new Mensaje("Canal " + canal + " creado");
            cliente.receiveMessage(msj);
            msj = new Mensaje(nameCliente, " se unió a canal " + canal);
            cliente.receiveMessage(msj);
        }
    }
    
    @Override
    public void unsubscribeChannel(String nameCliente, String canal) throws RemoteException {
        if(Canales.containsKey(canal)){
            Canal suscribir = Canales.get(canal);
            suscribir.removeClient(nameCliente);
            chatters.get(nameCliente).receiveMessage(new Mensaje("Abandonaste el canal " + canal));
            
            // enviar un mensaje a los suscriptores de ese canal
            Enumeration suscriptores = suscribir.getClients().elements();
            while(suscriptores.hasMoreElements())
            {
                Mensaje msj = new Mensaje(nameCliente, " abandonó el canal " + canal);
                ((IChatClient)suscriptores.nextElement()).receiveMessage(msj);
            }
        }
        else
        {
            Mensaje msg = new Mensaje("El canal " + canal + " no existe");
            chatters.get(nameCliente).receiveMessage(msg);
        }
    }
    
    @Override
    public void getCurrentChannels(String cliente) throws RemoteException {
        
        if(Canales.size() == 0)
        {
            chatters.get(cliente).receiveMessage(new Mensaje("No hay canales disponibles"));
        }
        else
        {
            chatters.get(cliente).receiveMessage(new Mensaje("Estas suscrito a los siguientes canales: "));
            Enumeration canales = Canales.elements();
            while(canales.hasMoreElements())
            {
                Canal canal = (Canal)canales.nextElement();
                if(canal.containsClient(cliente))
                {
                    // buscar key mediante value en hashtable
                    for(Map.Entry entry: Canales.entrySet()){
                        if(entry.getValue() == canal){
                            chatters.get(cliente).receiveMessage(new Mensaje(entry.getKey().toString()));
                            break; 
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void getAllChannels(IChatClient cliente) throws RemoteException {
        if(Canales.size() == 0)
        {
            cliente.receiveMessage(new Mensaje("No hay canales disponibles"));
        }
        else
        {
            cliente.receiveMessage(new Mensaje("Canales disponibles: "));
            Enumeration canales = Canales.keys();
            while(canales.hasMoreElements())
            {
                cliente.receiveMessage(new Mensaje(canales.nextElement().toString()));
            }
        }
    }
    
    

    @Override
    public void subscribeTopic(String nameCliente, String canal, String topico) throws RemoteException {
        if(Canales.size() == 0)
        {
            chatters.get(nameCliente).receiveMessage(new Mensaje("No hay canales registrados"));
        }
        else
        {
            // si no esta suscrito al canal suscribirlo
            if(!Canales.get(canal).containsClient(nameCliente))
            {
                Canales.get(canal).addClient(nameCliente, chatters.get(nameCliente));
            }
            // suscribir al topico
            if(Canales.get(canal).subscribeTopic(topico, nameCliente))
                chatters.get(nameCliente).receiveMessage(new Mensaje("El cliente se suscribió al canal: " + canal + ", topico: " + topico));
            else
                chatters.get(nameCliente).receiveMessage(new Mensaje("No se realizo suscripción, canal: " + canal + ", topico: " + topico));
        }
    }

    @Override
    public void unsubscribeTopic(String nameCliente, String canal, String topico) throws RemoteException {
        if(Canales.size() == 0)
        {
            chatters.get(nameCliente).receiveMessage(new Mensaje("No hay canales registrados"));
        }
        else
        {
            // suscribir al topico
            if(Canales.get(canal).unsubscribeTopic(topico, nameCliente))
                chatters.get(nameCliente).receiveMessage(new Mensaje("El cliente abandonó al canal: " + canal + ", topico: " + topico));
            else
                chatters.get(nameCliente).receiveMessage(new Mensaje("No se abandonó, canal: " + canal + ", topico: " + topico));
        }
    }

    @Override
    public void getCurrentTopics(String cliente) throws RemoteException {
        if(Canales.size() == 0)
        {
            chatters.get(cliente).receiveMessage(new Mensaje("No hay canales registrados"));
        }
        else
        {
            chatters.get(cliente).receiveMessage(new Mensaje("Topicos a los que esta suscrito cliente: "));
            Enumeration canales = Canales.elements();
            while(canales.hasMoreElements())
            {
                Canal c = (Canal)canales.nextElement();
                if(c.containsClient(cliente))
                {
                    List<String> top = c.getClientTopics(cliente);
                    if(top.size() > 0)
                    {
                        for(int i =0; i<top.size(); i++)
                        {
                            chatters.get(cliente).receiveMessage(new Mensaje("Canal: " + c.name + ", Topico: " + top.get(i)));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void getAllTopics(String cliente) throws RemoteException {
        if(Canales.size() == 0)
        {
            chatters.get(cliente).receiveMessage(new Mensaje("No hay canales registrados"));
        }
        else
        {
            chatters.get(cliente).receiveMessage(new Mensaje("Todos los topicos: "));
            Enumeration canales = Canales.elements();
            while(canales.hasMoreElements())
            {
                Canal c = (Canal)canales.nextElement();
                
                    List<String> top = c.getAllTopics();
                    if(top.size() > 0)
                    {
                        for(int i =0; i<top.size(); i++)
                        {
                            chatters.get(cliente).receiveMessage(new Mensaje("Canal: " + c.name + ", Topico: " + top.get(i)));
                        }
                    }
                
            }
        }
    }
    
    public static void main( String[] args ){
        //String downloadLocation = new String("file:/Users/eduardomartinez/Documents/rmi/rmi_distribuidos"); 
        String serverURL = new String("///ChatServer");
        try {
            //System.setProperty("java.rmi.server.codebase", downloadLocation); 
            //para windows
            LocateRegistry.createRegistry(1099);
            ChatServer server = new ChatServer(); 
            Naming.rebind(serverURL, server); 
            System.out.println("Chat server ready");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
