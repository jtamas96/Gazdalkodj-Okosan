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
public class BoardResponse<T> {
    private String errorMessage;
    private boolean actionSuccessful;
    private T value;

    public BoardResponse(String errorMessage, boolean actionSuccessful, T val) {
        this.errorMessage = errorMessage;
        this.actionSuccessful = actionSuccessful;
        this.value = val;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isActionSuccessful() {
        return actionSuccessful;
    }

    public T getValue() {
        return value;
    }

}
