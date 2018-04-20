package hu.elte.gazdalkodjokosan.model.enums;


public enum Item {
    LAKAS(9000000, true, 11000000, 2000000, 90000),
    AUTO(7000000, true, 7960000, 2500000, 130000),
    GYERMEK_JOVO(180000, false, null, null, null),
    NYUGDIJ_BISZT(180000, false, null, null, null),
    CASCO_BISZT(50000, false, null, null, null),
    HAZORZO_BISZT(30000, false, null, null, null),
    BKV_BERLET(9000, false, null, null, null),
    KONYHA_BUTOR(300000, false, null, null, null),
    TV(70000, false, null, null, null),
    KONYVEK(8000, false, null, null, null),
    SPORT_FELSZERELES(25000, false, null, null, null), //TODO: Kell vagy nem?

    ;

    private int cost;
    private boolean creditable;
    public Integer creditableTotalCost;
    public Integer creditableStartCost;
    public Integer annualCost;

    public boolean isCreditable() {
        return creditable;
    }

    Item(int cost, boolean creditable,
         Integer creditableTotalCost, Integer creditableStartCost, Integer annualCost) {
        this.cost = cost;
        this.creditable = creditable;
        this.creditableTotalCost = creditableTotalCost;
        this.creditableStartCost = creditableStartCost;
        this.annualCost = annualCost;
    }

    public int getCost() {
        return cost;
    }
}
