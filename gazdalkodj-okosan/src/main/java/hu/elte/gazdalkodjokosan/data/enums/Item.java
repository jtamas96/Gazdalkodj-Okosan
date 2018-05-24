package hu.elte.gazdalkodjokosan.data.enums;


public enum Item {
    LAKAS(9500000, true, true, 11000000, 2000000, 90000, false),
    AUTO(7000000, true, true, 7960000, 2500000, 130000, false),
    GYERMEK_JOVO(180000, false, false, null, null, null, true),
    NYUGDIJ_BISZT(180000, false, false, null, null, null, true),
    CASCO_BISZT(50000, false, false, null, null, null, true),
    HAZORZO_BISZT(30000, false, false, null, null, null, true),
    BKV_BERLET(9000, false, false, null, null, null, false),
    KONYHA_BUTOR(300000, false, true, null, null, null, false),
    SZOBA_BUTOR(900000, false, true, null, null, null, false),
    TV(70000, false, true, null, null, null, false),
    MOSOGEP(90000, false, true, null, null, null, false),
    HUTO(80000, false, true, null, null, null, false),
    SUTO(70000, false, true, null, null, null, false);

    private int cost;
    private boolean creditable;
    private boolean mandatoryForWining;
    public Integer creditableTotalCost;
    public Integer creditableStartCost;
    public Integer annualCost;
    public boolean insurrance;

    public boolean isCreditable() {
        return creditable;
    }

    Item(int cost, boolean creditable, boolean mandatory,
         Integer creditableTotalCost, Integer creditableStartCost, Integer annualCost, Boolean insurrance) {
        this.cost = cost;
        this.creditable = creditable;
        this.creditableTotalCost = creditableTotalCost;
        this.creditableStartCost = creditableStartCost;
        this.annualCost = annualCost;
        this.mandatoryForWining = mandatory;
        this.insurrance = insurrance;
    }

    public int getCost() {
        return cost;
    }

    public boolean getMandatory() {
        return mandatoryForWining;
    }
}
