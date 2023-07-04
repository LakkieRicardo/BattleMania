package net.battle.core.supplydrop;

public enum SupplyDropType {
  NORMAL(1, 50, "sd.norm"),
  RARE(2, 125, "sd.rare"),
  LEGENDARY(3, 250, "sd.lgnd");

  private SupplyDrop supplyDrop;
  private final String statName;
  private final int tokenCost;
  private final int rarity;

  SupplyDropType(int rarity, int tokenCost, String statName) {
    this.rarity = rarity;
    this.tokenCost = tokenCost;
    this.statName = statName;
  }

  public int getRarity() {
    return this.rarity;
  }

  public int getTokenCost() {
    return this.tokenCost;
  }

  public String getStatName() {
    return this.statName;
  }

  public SupplyDrop getSupplyDrop() {
    return this.supplyDrop;
  }

  public void setSupplyDrop(SupplyDrop drop) {
    this.supplyDrop = drop;
  }
}