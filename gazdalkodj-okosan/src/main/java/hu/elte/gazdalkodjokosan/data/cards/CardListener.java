package hu.elte.gazdalkodjokosan.data.cards;

import hu.elte.gazdalkodjokosan.data.Player;

import java.util.function.Consumer;

public interface CardListener {
    public void stepForward(int step);

    public void stepOnBoard(int step);

    public void writeMessage(String message);

    public void immobilize(int round);

    public void playerUpdateFunction(Consumer<Player> f);
}
