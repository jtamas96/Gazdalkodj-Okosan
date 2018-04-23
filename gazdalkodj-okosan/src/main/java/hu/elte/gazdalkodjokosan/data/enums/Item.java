package hu.elte.gazdalkodjokosan.data.enums;


public enum Item {
    LAKAS(9000000, true, true, 11000000, 2000000, 90000),
    AUTO(7000000, true, true, 7960000, 2500000, 130000),
    GYERMEK_JOVO(180000, false, false, null, null, null),
    NYUGDIJ_BISZT(180000, false, false,  null, null, null),
    CASCO_BISZT(50000, false,false, null, null, null),
    HAZORZO_BISZT(30000, false, false, null, null, null),
    BKV_BERLET(9000, false, false, null, null, null),
    KONYHA_BUTOR(300000, false, true, null, null, null),
    TV(70000, false, true, null, null, null),
    KONYVEK(8000, false, true, null, null, null),
    SPORT_FELSZERELES(25000, false, false, null, null, null), //TODO: Kell vagy nem?
    // TODO: illetve mik is kellenek a nyeréshez?

    ;

    private int cost;
    private boolean creditable;
    private boolean mandatoryForWining;
    public Integer creditableTotalCost;
    public Integer creditableStartCost;
    public Integer annualCost;

    public boolean isCreditable() {
        return creditable;
    }

    Item(int cost, boolean creditable, boolean mandatory,
         Integer creditableTotalCost, Integer creditableStartCost, Integer annualCost) {
        this.cost = cost;
        this.creditable = creditable;
        this.creditableTotalCost = creditableTotalCost;
        this.creditableStartCost = creditableStartCost;
        this.annualCost = annualCost;
        this.mandatoryForWining = mandatory;
    }

    public int getCost() {
        return cost;
    }

    public boolean getMandatory() {
        return mandatoryForWining;
    }
}
