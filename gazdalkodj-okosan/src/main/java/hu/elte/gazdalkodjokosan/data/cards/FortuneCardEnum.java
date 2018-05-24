package hu.elte.gazdalkodjokosan.data.cards;

import hu.elte.gazdalkodjokosan.data.SaleItem;
import hu.elte.gazdalkodjokosan.data.enums.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum FortuneCardEnum implements FortuneCard {
    CARD1 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 1]. Lépj előre 1 mezőt!");
            cl.stepForward(1);
        }
    },

    CARD2 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 2]. Takarékoskodj! A megtakarított pénzedet tartsd a Lakossági folyószámládon, melyre az OTP BANK 7% kamatot fizet. Az összeget kerekítsd!");
        }
    },

    CARD3 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 3]. Visegrádi hajókiránduláson veszel részt. Lépj a 24-es mezőre! A kirándulás 12.000 Ft. Fizesd be a pénztárba.");
            cl.stepOnBoard(24);
        }
    },

    CARD4 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 4]. Jó munkádért 300.000 Ft jutalomban részesülsz.");
            cl.playerUpdateFunction(player -> {
                int money = player.getBankBalance();
                player.setBankBalance(money + 300000);
            });
        }
    },

    CARD5 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 5]. Kivetted éves szabadságod. Kétszer kimaradsz a dobásból!");
            cl.immobilize(2);
        }
    },

    CARD6 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 6]. Jól takarékoskodtál, ezért az OTP BANK, a Lakossági folyószámlán elhelyezett pénzed után 15% kamatot fizet.");
            cl.playerUpdateFunction(player -> {
                int money = player.getBankBalance();
                player.setBankBalance(Math.round(money * 1.15F));
            });
        }
    },

    CARD7 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 7]. Gyermeked felvételt nyert az egyetemre. Ha van Generali Gyermekjövő Programod kapsz 5 millió Ft-ot, melyet a Biztosító a folyószámládra helyez.");
            cl.playerUpdateFunction(player -> {
                int money = player.getBankBalance();
                if (player.getInsurances().contains(Item.GYERMEK_JOVO)) {
                    player.setBankBalance(money + 5000000);
                }
            });
        }
    },

    CARD8 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 8]. Helyesen fejtetted meg a Füles magazin legújabb rejtvényét. 90.000 Ft-ot nyertél. Lépj a 4-es mezőre!");
            cl.playerUpdateFunction(player -> {
                int money = player.getBankBalance();
                player.setBankBalance(money + 90000);
            });
            cl.stepOnBoard(4);
        }
    },

    CARD9 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 9]. \"Kettő az egyben\" csomagra szerződtél, mely a mobiltelefon mellett a mobilinternet díját is tartalmazza, így ez csak 5.000 Ft-ba kerül. Lépj a 36-os mezőre.");
            cl.stepOnBoard(36);
        }
    },

    CARD10 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 10]. Az EURONICS boltjaiban most akciósan vásárolhatod meg televíziódat. Így 70.000 Ft helyett most csak 60.000 Ft-ot kell fizetned. Lépj a 40-es mezőre!");
            cl.playerUpdateFunction(player -> {
                Optional<SaleItem> optSi = player.getItem(Item.TV);
                optSi.ifPresent(saleItem -> saleItem.reducePriceWith(10000));
            });
            cl.stepOnBoard(40);
        }
    },

    CARD11 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 11]. Sorsjátékon szobabútort nyertél. Ha nincs lakásod, vagy már megvetted. 900.000 Ft-ot a pénztár a folyószámládra helyez.");
            cl.playerUpdateFunction(player -> {
                Optional<SaleItem> optSi = player.getItem(Item.SZOBA_BUTOR);
                optSi.ifPresent(saleItem -> {
                    if (saleItem.isPurchased() || !player.isWithHouse()) {
                        player.setBankBalance(player.getBankBalance() + Item.SZOBA_BUTOR.getCost());
                    } else {
                        saleItem.purchase();
                    }
                });
            });
        }
    },

    CARD12 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 12]. Kiégett a lakásod! Vissza kell adnod a berendezési tárgyaidat! Ha van Generali Házőrző lakásbiztosításod a biztosító kifizeti a károdat. Ha nincs akkor, ha legközelebb a 9-es mezőre lépsz, kösd meg lakásbiztosításodat.");
            cl.playerUpdateFunction(player -> {
                if (!player.getInsurances().contains(Item.HAZORZO_BISZT)) {
                    List<Item> berendezes = Arrays.asList(Item.KONYHA_BUTOR, Item.SZOBA_BUTOR, Item.MOSOGEP, Item.SUTO, Item.HUTO, Item.TV);
                    for (Item item : berendezes) {
                        Optional<SaleItem> optSi = player.getItem(item);
                        optSi.ifPresent(SaleItem::confiscate);
                    }
                }

            });
        }
    },

    CARD13 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 13]. A Budapesti Nemzetközi Vásár sorsjátékán nyertél. Ha már van mosógéped, pénztár kifizeti az árát. (90.000 Ft)");
            cl.playerUpdateFunction(player -> {
                Optional<SaleItem> optSi = player.getItem(Item.MOSOGEP);
                optSi.ifPresent(saleItem -> {
                    if (saleItem.isPurchased()) {
                        player.setBankBalance(player.getBankBalance() + Item.MOSOGEP.getCost());
                    } else {
                        saleItem.purchase();
                    }
                });
            });
        }
    },

    CARD14 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 14]. Lépj vissza 3 mezőt!");
            cl.stepForward(-3);
        }
    },
    CARD15 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 15]. Térj be cipőt vásárolni a DEICHMANN valamelyik üzletébe! Kényelmes cipőben lépj a 14-es mezőre! Fizess 15.000 Ft-ot!");
            cl.stepOnBoard(14);
        }
    },
    CARD16 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 16]. Lépj a 11-es mezőre, ahol konyhabútort vásárolhatsz, ha már van lakásod. A konyhabútor ára 300.000 Ft.");
            cl.stepOnBoard(11);
        }
    },

    CARD17 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 17]. Fizesd ki gáz-és villanyszámládat az OTP BANK-nál nyitott Lakossági folyószámlán keresztül, melynek összege 15.000 Ft. Lépj a 8-as mezőre!");
            cl.playerUpdateFunction(player -> {
                int money = player.getBankBalance();
                player.setBankBalance(money - 15000);
            });
            cl.stepOnBoard(8);
        }
    },

    CARD18 {
        public void notify(CardListener cl) {
            cl.writeMessage("[Szerencse 18]. Ebédelj barátaiddal étteremben, egy csendes mediterrán hangulatú helyen, ahová mindig szívesen térsz majd vissza. Lépj a 25-ös mezőre! Fizess 10.000 Ft-t!");
            cl.stepOnBoard(25);
        }
    };

    List<CardListener> listeners = new ArrayList<>();

    FortuneCardEnum() {
    }

    ;

    public void addListener(CardListener cl) {
        listeners.add(cl);
    }

    public void notifyListeners() {
        for (CardListener cl : listeners) {
            notify(cl);
        }
    }
}
