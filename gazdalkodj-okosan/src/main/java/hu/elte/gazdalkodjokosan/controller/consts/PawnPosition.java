/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller.consts;

import hu.elte.gazdalkodjokosan.common.transfer.PlayerColor;

/**
 *
 * @author sando
 */
public class PawnPosition {
    private static final int X_FIELD_00_34 = 693;
    private static final int X_FIELD_13_21 = 44;
    private static final int X_START_01_TO_12 = 623;
    private static final int X_START_14_TO_20 = 14;
    private static final int X_START_22_TO_33 = 119;
    private static final int X_START_35_TO_41 = 658;
    
    private static final int Y_START_00_TO_13 = 490;
    private static final int Y_START_14_TO_20 = 391;
    private static final int Y_START_21_TO_34 = 70;
    private static final int Y_START_35_TO_41 = 123;
    private static final int STEP_SIZE = 45;
    private static final int DISTANCE = 16;
    
    public static int calcX(int fieldNum, int index) {
        if (fieldNum == 0 || fieldNum == 34) {
            return X_FIELD_00_34;
        } else if (fieldNum == 13 || fieldNum == 21) {
            return X_FIELD_13_21;
        } else if (fieldNum >= 1 && fieldNum <= 12) {
            return X_START_01_TO_12 - STEP_SIZE * (fieldNum - 1); 
        } else if (fieldNum >= 14 && fieldNum <= 20) {
            return X_START_14_TO_20 + DISTANCE * (index - 1);
        } else if (fieldNum >= 22 && fieldNum <= 33) {
            return X_START_22_TO_33 + STEP_SIZE * (fieldNum - 22);
        } else
            return X_START_35_TO_41 + DISTANCE * (index - 1);
    }
    
    public static int calcY(int fieldNum, int index) {
        if (fieldNum >= 0 && fieldNum <= 13) {
            return Y_START_00_TO_13 - DISTANCE * (index - 1);
        } else if (fieldNum >= 14 && fieldNum <= 20) {
            return Y_START_14_TO_20 - STEP_SIZE * (fieldNum - 14);
        } else if (fieldNum >= 21 && fieldNum <= 34) {
            return Y_START_21_TO_34 - DISTANCE * (index - 1);
        } else
            return Y_START_35_TO_41 + STEP_SIZE * (fieldNum - 35);
    }
}
