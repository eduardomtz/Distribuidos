/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publish_subscribe;

import java.io.*;

public class Mensaje implements Serializable { 
    public String name;
    public String text;
    
    public Mensaje(String name, String text) { 
        this.name = name;
        this.text = text;
    }
    
    public Mensaje(String text) { 
        this.text = text;
    }
}
