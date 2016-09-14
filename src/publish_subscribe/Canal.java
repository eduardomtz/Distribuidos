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
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Canal {
    Hashtable<String, IChatClient> chatters; 
    List<String> topicos;
    Hashtable<String, List<String>> clientesTopicos;
    String name;
    
    public Canal(String name){
        this.name = name;
        chatters = new Hashtable<String, IChatClient>();
        //cliente, topico
        clientesTopicos = new Hashtable<String, List<String>>();
        topicos = new ArrayList<String>();
    }
    
    public boolean addTopic(String topic){
        if(topicos.contains(topic))
        {
            return false;
        }
        topicos.add(topic);
        return true;
    }
    
    public boolean removeTopic(String topic){
        if(topicos.contains(topic))
        {
            Enumeration listTopics = clientesTopicos.elements();
            while(listTopics.hasMoreElements()) {
                ((List<String>)listTopics.nextElement()).remove(topic);
            }
            
            topicos.remove(topic);
            return true;
        }
        return false;
    }
    
    public boolean subscribeTopic(String topic, String client){
        // existe el topico
        if(!topicos.contains(topic))
        {
            addTopic(topic);
        }
        
        if(clientesTopicos.get(client) != null)
        {
            if(!clientesTopicos.get(client).contains(topic))
            {
                clientesTopicos.get(client).add(topic);
                return true;
            }
            return false; // ya estaba suscrito
        }
        else
        {
            // agregar
            List<String> lista = new ArrayList<String>();
            lista.add(topic);
            clientesTopicos.put(client, lista); // no estaba suscrito
        }
        return true;
    }
    
    public boolean unsubscribeTopic(String topic, String client){
        if(topicos.contains(topic))
        {
            if(clientesTopicos.containsKey(client))
            {
                if(clientesTopicos.get(client).contains(topic))
                {
                    clientesTopicos.get(client).remove(topic); // quitar el topico para ese cliente
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<String> getClientTopics(String client) {
        if(clientesTopicos.size()>0)
        {
            if(clientesTopicos.containsKey(client))
            {
                return clientesTopicos.get(client);
            }
        }
        return null;
    }
    
    public List<String> getAllTopics() {
        return topicos;
    }
    
    public boolean addClient(String NombreCliente, IChatClient Cliente){
        boolean res = false;
        if(!chatters.containsKey(NombreCliente)){
            chatters.put(NombreCliente, Cliente);
            
            List<String> lista = new ArrayList<String>();
            clientesTopicos.put(NombreCliente, lista);
            
            res = true;
        } 
        return res;
    }
    
    public boolean removeClient(String usuario){
        boolean res = false;
        if(chatters.containsKey(usuario)){
            chatters.remove(usuario);
            clientesTopicos.remove(usuario);
            res = true;
        } 
        return res;
    
    }   
    
    public boolean containsClient(String usuario){
        boolean res = false;
        if(chatters.containsKey(usuario)){
            res = true;
        }
        return res;
    }
    
    public Hashtable<String, IChatClient> getClients(){
        return chatters;
    }
    
    public IChatClient getIChatClient(String nombreC){
        IChatClient res = null;
        if(containsClient(nombreC)){
            res = chatters.get(nombreC);
        }
        return res;
    }
}
