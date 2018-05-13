package hu.elte.gazdalkodjokosan.service.model;

import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.SaleItem;
import hu.elte.gazdalkodjokosan.data.cards.CardListener;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.events.BuyEvent;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNotFoundException;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

@Component
public class GameModel implements CardListener {

    private List<Player> players;
    private List<Field> table;
    private Player currentPlayer;
    private final ApplicationEventPublisher publisher;
    private Player lastStepped;

    @Autowired
    public GameModel(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void newGame(int playerNumber) throws PlayerNumberException {
        if (playerNumber >= 2 && playerNumber <= 6) {
            players = new ArrayList<>();

            for (int i = 0; i < playerNumber; i++) {
                Player p = new Player(3000000, 0, 0, i, SaleItem.getInitialListForUser());
                players.add(p);
            }
            currentPlayer = players.get(0);
            table = new ArrayList<>();
            table.add(new Field(0, new ArrayList<>(players)));
            for (int i = 1; i < 42; i++) {
                table.add(new Field(i, new ArrayList<>()));
            }
        } else {
            throw new PlayerNumberException("Invalid number of players.");
        }
    }

    public void stepGame() {
        if(currentPlayer.equals(lastStepped)) return;

        lastStepped = currentPlayer;
        int immobilized = currentPlayer.getImmobilized();
        if (immobilized > 0) {
            currentPlayer.setImmobilized(immobilized - 1);
//            switchPlayer(); Ezeket külön Actionok!
//            stepGame();
        } else {
            Random random = new Random();
            int step = random.nextInt(6) + 1;
            stepForward(step);
            System.out.println("Game stepped. Current player at field: " + currentPlayer.getPosition());
        }
    }

    private void playerSteppedOnStart(boolean exact) {
        int currentBalance = currentPlayer.getBankBalance();
        if (exact) {
            currentPlayer.setBankBalance(currentBalance + 1000000);
        } else {
            currentPlayer.setBankBalance(currentBalance + 500000);
        }
    }

    public List<Field> getTable() {
        return table;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOverForPlayer(int playerIndex) throws PlayerNotFoundException {
        Player player = getPlayer(playerIndex);

        List<SaleItem> items = getItemsOfUser(playerIndex);

        boolean hasAllMandatory = items.stream()
                .filter(userItem -> Item.valueOf(userItem.name).getMandatory())
                .allMatch(SaleItem::isPurchased);
        return hasAllMandatory && player.getBankBalance() >= 600000;
    }

    public List<SaleItem> getItemsOfUser(int playerIndex) throws PlayerNotFoundException {
        Player player = getPlayer(playerIndex);
        return player.getItems();
    }

    private Player getPlayer(int playerIndex) throws PlayerNotFoundException {
        Optional<Player> optPlayer = players.stream()
                .filter(p -> p.getIndex() == playerIndex)
                .findFirst();

        if (optPlayer.isPresent()) {
            return optPlayer.get();
        } else {
            throw new PlayerNotFoundException("Player not found with color: " + playerIndex);
        }
    }

    public void switchPlayer() {
        if(! currentPlayer.equals(lastStepped)){
            publisher.publishEvent(new MessageEvent(this, "Még nem dobtál!"));
            return;
        }
        int newPlayersIndex = (currentPlayer.getIndex() + 1) % players.size();
        currentPlayer = players.get(newPlayersIndex);
        System.out.println("Switched player. Current player: " + currentPlayer.getIndex());
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void runFieldEffect(int position) {
        switch (position) {
            case 1:
                writeMessage("Kézi csomózású perzsaszőnyegekkel díszíted lakásod. Fizess 30.000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance(player.getBankBalance() - 30000));
                break;
            case 2:
                writeMessage("Megvásárolhatod éves bérletedet 9.000 Ft-ért a BKV-nál. Ha már van bérleted, a BKV mezőin (2-es, 15-ös, 27-es) nem kell többet fizetned.");
                publisher.publishEvent(new BuyEvent(this, currentPlayer));
                break;
            case 3:
            case 10:
            case 16:
            case 23:
            case 28:
            case 32:
            case 37:
                writeMessage("Húzz egy Szerencsekerék kártyát, és kövesd annak utasításait!");
                //Todo szerencsekártya húzás
                break;
            case 4:
                writeMessage("Szereted az izgalmas rejtvényeket, ezért Füles magazin előfizetést vásároltál. Fizess 6000 Ft-t!");
                playerUpdateFunction(player -> player.setBankBalance(player.getBankBalance() - 6000));
                break;
            case 5:
                writeMessage("Megveheted az Ivanicstól a legújabb Volvo modellt egy összegben vagy részletre.");
                publisher.publishEvent(new BuyEvent(this, currentPlayer));
                break;
            case 6:
                writeMessage("Jól kihasználtad a REGIO játékkereskedés akcióit. Vásárlásodért most csak 5.000 Ft- kell fizetned!");
                playerUpdateFunction(player -> player.setBankBalance(player.getBankBalance() - 5000));
                break;
            case 7:
                writeMessage("A sport szórakozás, egészség. Sportolj!");
                break;
            case 8:
                writeMessage("7 % kamatjóváírásban, részesülsz, amikor ezen a mezőn áthaladsz, vagy rálépsz.");
                playerUpdateFunction(player -> player.setBankBalance((int) (player.getBankBalance() * 0.7)));
                break;
            case 9:
                writeMessage("A GENERALI-nál megkötheted gyermekjövő élet- 180.000 Ft / év, Nyugdíj – 180.000 Ft / év, Házőrző lakás lakás- 300.000 Ft / év és Casco biztosításodat 50 000 Ft / év összegért.");
                //Todo biztosítás event
                break;
            case 11:
                if(currentPlayer.isWithBKVPass() && currentPlayer.getBankBalance() >= 300000){
                    writeMessage("Ha van pénzed és lakásod, vásárolj modern konyhabútort. Fizess 300.000 Ft-ot!");
                    publisher.publishEvent(new BuyEvent(this, currentPlayer));
                }
                break;
            case 12:
                writeMessage("Meglátogattad fővárosunk kedvenc állatait az Állat- és Növénykertben.");
                break;
            case 13:
                writeMessage("Túl sokat idegeskedsz, vonulj szanatóriumba! Kimaradsz két dobásból.");
                immobilize(2);
                break;
            case 14:
                writeMessage("Tönkrement a cipőd, vásárolnod kell egy újat a DElCHMANN valamelyik üzletében. A legjobb minőséget kapod a legjobb áron. Fizess 15.000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 15000)));
                break;
            case 15:
                writeMessage("Jegy nélkül utaztál az autóbuszon, 6 000 Ft büntetést fizetsz! Ha van bérleted, a büntetést most megúsztad.");
                if (!currentPlayer.isWithBKVPass()) {
                    playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 6000)));
                }
                break;
            case 17:
                writeMessage("A film tanít, szórakoztat. Nézd meg a legújabb sikerfilmet barátaiddal! Fizess 15.000 Ft-t!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 15000)));
                break;
            case 18:
                writeMessage("Minden könyvújdonságot megtalálsz, és kedvedre böngészhetsz a LIBRI könyvesboltokban. A vásárolt könyvekért fizess 8.000 Ft-t!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 8000)));
                break;
            case 19:
                writeMessage("Takarékoskodj, mert így szép lakáshoz juthatsz. Ha van pénzed, fizess be 9.500.000 Ft-ot az OTP BANK pénztárába és megkapod a lakásod. Amennyiben részletfizetésre van csak lehetőséged, fizess 2.000.000 Ft-ot A fennmaradó 9.000.000 Ft-ot pedig 90.000 Ft-os részletekben törlesztheted.");
                publisher.publishEvent(new BuyEvent(this, currentPlayer));
                break;
            case 20:
                writeMessage("Sportszereket vásároltál a DECATHLON Sportáruházban az akciós termékekből, 25000 Ft-ot kell fizetned. Vásárlás után sportoltál is, ezért jutalmul húzz egy Szerencsekerék kártyát!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 25000)));
                //Todo kártya húzás event
                break;
            case 21:
                writeMessage("Pihenj Siófok Balaton parti mediterrán szállodájában, a Hotel Azúrban! A háromnapos pihenésedért fizess 90 000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 95000)));
                break;
            case 22:
                writeMessage("Megtekintheted a NEMZETI GALÉRIA gyönyörű kiállítását.");
                break;
            case 24:
                writeMessage("Visegrádi hajókiránduláson voltál. Fizess 12.000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 12000)));
                break;
            case 25:
                writeMessage("Ma étteremben ebédelsz barátaiddal. Ez Neked 10.000 Ft-ba kerül.");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 10000)));
                break;
            case 26:
                writeMessage("Kellemes séta közben tekintsd meg a Margitsziget nevezetességeit!");
                break;
            case 27:
                writeMessage("Tömegközlekedést vettél igénybe, lemondtál a kényelmedről. Húzz egy Szerencsekerék kártyát, es kövesd annak utasításait!");
                //Todo szerencsekártya event
                break;
            case 29:
                writeMessage("Sétálj a Halászbástyán! Innen gyönyörű kilátás nyílik Budapestre.");
                break;
            case 30:
                writeMessage("Az utazási irodákban elintézhetsz mindent az utazásoddal kapcsolatosan kényelmesen és gyorsan. Fizess 150.000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 150000)));
                break;
            case 31:
                writeMessage("Bevásároltál az élelmiszer boltban. Fizess 7000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 7000)));
                break;
            case 33:
                writeMessage("Ha van pénzed. Vásárolj elektromos gépeket; hűtőszekrény, mosógépet, sütőt!");
                publisher.publishEvent(new BuyEvent(this, currentPlayer));
                break;
            case 34:
                writeMessage("Beléptél a KIKA áruházba! Konyhafelszerelésért fizess 40.000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 40000)));
                break;
            case 35:
                writeMessage("Vásároltál a regiojatek.hu web áruházban. Fizess 15.000 Ft-ot!");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 15000)));
                break;
            case 36:
                writeMessage("„Kettő az egyben\" csomagra szerződtél, mely a mobiltelefon mellett a mobilinternet díját is tartalmazza, így ez Neked csak 5.000 Ft-ba kerül.");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 5000)));
                break;
            case 38:
                if(currentPlayer.isWithHouse()){
                    writeMessage("Ha már van lakásod most vásárolhatsz bele szobabútort. A szobabútor ára 900000 Ft.");
                    publisher.publishEvent(new BuyEvent(this, currentPlayer));
                }
                break;
            case 39:
                writeMessage("Takarékoskodj, mert így szép lakáshoz juthatsz. Ha van pénzed, fizess be 9.500.000 Ft-ot az OTP BANK pénztárába, és megkapod a lakásod. Amennyiben részletfizetésre van csak lehetőséged, fizess 2.000.000 Ft-ot! A fennmaradó 9.000000 Ft-ot pedig 90.000 Ft-os részletekben törlesztheted.");
                publisher.publishEvent(new BuyEvent(this, currentPlayer));
                break;
            case 40:
                writeMessage("Az EURONICS Műszaki Áruházában minőséget vásárolhatsz olcsón. Most vedd meg a televíziód! Fizess 70.000 Ft-ot!");
                publisher.publishEvent(new BuyEvent(this, currentPlayer));
                break;
            case 41:
                writeMessage("A Lufthansa kényelmes és gyors utazást biztosít Európa nagyvárosaiba. A Repülőjegyed ára 60.000 Ft.");
                playerUpdateFunction(player -> player.setBankBalance((player.getBankBalance() - 60000)));
                break;
        }
    }

    @Override
    public void stepForward(int step) {
        int currentPosition = currentPlayer.getPosition();
        table.get(currentPosition).removePlayer(currentPlayer);
        if (currentPosition + step < table.size()) {
            table.get(currentPosition + step).addPlayer(currentPlayer);
            currentPlayer.setPosition(currentPosition + step);
        } else {
            int nextPosition = step + currentPosition - table.size() - 1;
            if (nextPosition == 0) {
                playerSteppedOnStart(true);
            } else {
                playerSteppedOnStart(false);
            }
            table.get(nextPosition).addPlayer(currentPlayer);
            currentPlayer.setPosition(nextPosition);
        }
        publisher.publishEvent(new GameSteppedEvent(this, currentPlayer, table));
        runFieldEffect(currentPlayer.getPosition());
    }

    // TODO: A Stepforward az alabbit kellene felhasznania.
    // Es ha az alabbiban lenne a start atlepes check akkor mindekett esetben mukodne a check!
    @Override
    public void stepOnBoard(int position) {
        table.get(currentPlayer.getPosition()).removePlayer(currentPlayer);
        if (position == 0) {
            playerSteppedOnStart(true);
        } else if (position < currentPlayer.getPosition()) {
            playerSteppedOnStart(false);
        }
        currentPlayer.setPosition(position);
        table.get(position).addPlayer(currentPlayer);
        publisher.publishEvent(new GameSteppedEvent(this, currentPlayer, table));
        runFieldEffect(position);
    }

    @Override
    public void writeMessage(String message) {
        publisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public void immobilize(int round) {
        currentPlayer.setImmobilized(currentPlayer.getImmobilized() + round);
    }

    @Override
    public void playerUpdateFunction(Consumer<Player> f) {
        f.accept(currentPlayer);
        publisher.publishEvent(new UpdatePlayerEvent(this, currentPlayer));
    }
}