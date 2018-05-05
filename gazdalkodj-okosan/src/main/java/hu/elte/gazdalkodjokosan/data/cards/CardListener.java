package hu.elte.gazdalkodjokosan.data.cards;

import hu.elte.gazdalkodjokosan.data.Player;

import java.util.function.Consumer;

public interface CardListener {
    public void stepForward(int a);

    public void stepOnBoard(int a);

    public void writeMessage(String s);

    public void immobilize(int round);

    public void playerUpdateFunction(Consumer<Player> f);
}
