/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.common.transfer;

/**
 *
 * @author sando
 */
public class BoardResponse {
    private String errorMessage;
    private boolean actionSuccessful; 

    public BoardResponse(String errorMessage, boolean actionSuccessful) {
        this.errorMessage = errorMessage;
        this.actionSuccessful = actionSuccessful;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isActionSuccessful() {
        return actionSuccessful;
    }
}
