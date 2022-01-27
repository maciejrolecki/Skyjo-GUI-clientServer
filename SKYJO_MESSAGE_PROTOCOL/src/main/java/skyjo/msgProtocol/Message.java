/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skyjo.msgProtocol;

import java.io.Serializable;

/**
 *
 * @author mad8
 */
public interface Message extends Serializable{
    /**
     * Return the message type.
     *
     * @return the message type.
     */
    Object getType();
    
    /**
     * Return the message content.
     *
     * @return the message content.
     */
    Object getContent();
}
