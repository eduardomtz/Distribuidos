//===============================================================
// Mensaje
//===============================================================

package Mensaje;

import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Message implements Serializable {

    public String name;
    public String text;
    public String canal;
    public String topic;
    public Date   fecha;
    public String date;

    /*
      Clase mensaje:
      - Nombre del cliente.
      - Texto del mensaje.
      - Canal por el que envió.
      - Tópico.
      - Se incluye fecha de mensaje.
     */
    public Message(String name, String text, String canal, String topic) {
        this.name = name;
        this.text = text;
        this.canal = canal;
        this.topic = topic;
        fecha = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = dateFormat.format(fecha);
    }

    @Override
    public String toString(){
        return "Fecha: "   + date  + "\n" +
               "Cliente: " + name  + "\n" +
               "Channel: " + canal + "\n" +
               "Topico: "  + topic + "\n" +
               "Mensaje: " + text;
    }
}