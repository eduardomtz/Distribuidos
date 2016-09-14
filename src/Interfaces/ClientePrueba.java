/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author eduardomartinez
 */
import Mensaje.Message;
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

public class ClientePrueba extends UnicastRemoteObject implements IChatClient {
    public String name;
    IChatServer server;
    String serverURL;
    
    public ClientePrueba( String name, String url ) throws RemoteException { 
        this.name = name;
        serverURL = url;
        connect();
    }
    
    private void connect() {
        try {
            server=(IChatServer) Naming.lookup("rmi://"+serverURL+"/ChatServer"); 
            server.login(name, this); // callback object
        }
        catch( Exception e ) {
                  e.printStackTrace();
        }
    }
    
    private void disconnect() {
        try {
            server.logout(name);
        }
        catch( Exception e ) {
                  e.printStackTrace();
        }
    }
    
    private void sendTextToChat(String text) { 
        try {
            server.sendBroadcast(null);
            // server.sendBroadcast(new Message(name,text), this.name);
        }
        catch( RemoteException e ) {
            e.printStackTrace();
        }
    }
    
    public void receiveEnter( String name ) {
        System.out.println("\nLog in " + name);
                //+this.name+ " -- Cadena a enviar: ");
    }
    
    public void receiveExit( String name ) { 
        System.out.println("\nLog out " + name + "\n"); 
        if ( name.equals(this.name) )
            System.exit(0);
        //else
            //System.out.println(this.name + " -- Cadena a enviar: " );
    }
    
    public void receiveMessage( Message message ) { 
        if(message.name == null || message.name.isEmpty())
            System.out.println('\n' + message.text);
        else
            System.out.println('\n' + message.name + ":" + message.text);
                // + "\n" + name + " -- Cadena a enviar: "); 
    }
    
    public static String pideCadena() { 

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
        try {
            return br.readLine();
        }
        catch(IOException ioe ) {
            System.out.println(ioe.toString()); 
        }
        return "";
    }
    
    public void getAllChannels(){
        try{
            server.getAllChannels(this.name);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /*
    public void getCurrentChannels(){
        try{
            server.getCurrentChannels(this.name);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    */
    
    public void subscribeToChannel(String channelName){
        try {
            server.loginChannel(this.name, this, channelName);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void unsubscribeToChannel(String channelName){
        try {
            server.logoutChannel(this.name, channelName);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void subscribeToTopic(String channelName, String topic){
        try {
            server.subscribeToTopic(this.name,this, channelName, topic);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void unsubscribeToTopic(String channelName, String topic){
        try {
            server.unsubscribeToTopic(this.name, topic, channelName);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void getAllTopics(String canal){
        try{
            //server.getAllTopics(this.name);
            server.getAllTopics(this.name, canal);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void getCurrentTopics(){
        try{
            //server.getCurrentTopics(this.name);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void main( String[] args ) {
        String strCad = "";
        try {
            ClientePrueba clte = new ClientePrueba(args[0], args[1]); 
            //strCad = pideCadena(args[0] + " -- Cadena a enviar: "); 
            
            System.out.println("Bienvenido " + clte.name);
            System.out.println("Escribe las siguientes opciones ó comparte un mensaje ó escribe quit para salir.");
            System.out.println("0) Mostrar menú");
            System.out.println("-------CANALES----------");
            System.out.println("1) Mostrar todos los canales");
            System.out.println("2) Mostrar los canales a los que estás suscrito");
            System.out.println("3) Suscribir o crear canal [p.ej. 3-NuevoCanal]");
            System.out.println("4) Abandonar canal [p.ej. 4-CanalAAbandonar]");
            System.out.println("-------TOPICS----------");
            System.out.println("5) Mostrar todos los topicos");
            System.out.println("6) Mostrar los topicos donde esta suscrito el cliente");
            System.out.println("7) Suscribir o crear topico en canal [p.ej. 7-Canal-Topico]");
            System.out.println("8) Abandonar topico en canal [p.ej. 8-Canal-Topico]");
            
                
            while( !strCad.equals("quit") ) {
                
                System.out.printf("[#/Mensaje/quit]: ");
                strCad = pideCadena();
                String[] items = strCad.split("-");
                
                switch(items[0]){
                    case "0":
                        System.out.println("0) Mostrar menú");
                        System.out.println("-------CANALES----------");
                        System.out.println("1) Mostrar todos los canales");
                        System.out.println("2) Mostrar los canales a los que estás suscrito");
                        System.out.println("3) Suscribir o crear canal [p.ej. 3-NuevoCanal]");
                        System.out.println("4) Abandonar canal [p.ej. 4-CanalAAbandonar]");
                        System.out.println("-------TOPICS----------");
                        System.out.println("5) Mostrar todos los topicos");
                        System.out.println("6) Mostrar los topicos donde esta suscrito el cliente");
                        System.out.println("7) Suscribir o crear topico en canal [p.ej. 7-Canal-Topico]");
                        System.out.println("8) Abandonar topico en canal [p.ej. 8-Canal-Topico]");
                        break;
                    case "1":
                        clte.getAllChannels();
                        break;
                    case "2":
                        //clte.getCurrentChannels(items[1]);
                        break;
                    case "3":
                        if(items.length > 1)
                            clte.subscribeToChannel(items[1]);
                        break;
                    case "4":
                        if(items.length > 1)
                            clte.unsubscribeToChannel(items[1]);
                        break;
                    case "5":
                        clte.getAllTopics(items[0]);
                        break;
                    case "6":
                        clte.getCurrentTopics();
                        break;
                    case "7":
                        if(items.length > 2)
                            clte.subscribeToTopic(items[1],items[2]);
                        break;
                    case "8":
                        if(items.length > 2)
                            clte.unsubscribeToTopic(items[1],items[2]);
                        break;
                    default:
                    if(strCad != null && (!strCad.isEmpty() || !strCad.equals("quit")))
                        clte.sendTextToChat(strCad);
                }
            }
            System.out.println("Local console " + clte.name + ", going down"); 
            clte.disconnect();
        }
        catch( RemoteException e ) {
                  e.printStackTrace();
        } 
    }

    @Override
    public void receiveSubscribeToTopic(String name, String canal, String topico) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveUnsubscribeToTopic(String name, String canal, String topico) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveSubscribeToChannel(String name, String canal) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveUnsubscribeToChannel(String name, String canal) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}