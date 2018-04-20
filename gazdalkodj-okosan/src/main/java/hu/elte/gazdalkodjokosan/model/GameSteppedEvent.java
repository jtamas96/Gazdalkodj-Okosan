/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.model;

import org.springframework.context.ApplicationEvent;

public class GameSteppedEvent extends ApplicationEvent {

    public GameSteppedEvent(Object source) {
        super(source);
    }

}
