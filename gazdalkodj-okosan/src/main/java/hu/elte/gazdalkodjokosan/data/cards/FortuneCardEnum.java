package hu.elte.gazdalkodjokosan.data.cards;

import hu.elte.gazdalkodjokosan.data.CardListener;

import java.util.List;

public enum FortuneCardEnum implements FortuneCard {
    CARD1 {
        public void notify(CardListener cl){
            cl.writeMessage("1. Lépj előre 1 mezőt!");
            cl.stepForward(1);
        }
    },

    CARD2 {
        public void notify(CardListener cl){
            cl.writeMessage("2.Takarékoskodj! A megtakarított pénzedet tartsd a Lakossági folyószámládon, melyre az OTP BANK 7% kamatot fizet. Az összeget kerekítsd!");
        }
    },

    CARD3 {
        public void notify(CardListener cl){
            cl.writeMessage("3. Visegrádi hajókiránduláson veszel részt. Lépj a 24-es mezőre! A kirándulás 12.000 Ft. Fizesd be a pénztárba.");
            cl.stepOnBoard(24);
            cl.withDrawMoney(12000);
        }
    },
    ;

    List<CardListener> listeners;

    FortuneCardEnum(){};
    FortuneCardEnum(java.util.List<CardListener> listeners){
        this.listeners = listeners;
    }
    public void addListener(CardListener cl){
        listeners.add(cl);
    }

    public void notifyListeners(){
        for(CardListener cl: listeners){
            notify(cl);
        }
    }
}
