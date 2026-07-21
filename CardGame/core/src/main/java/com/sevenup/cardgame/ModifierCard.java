package com.sevenup.cardgame;

// Represents score modifier cards
public class ModifierCard extends Card {
    private final int bonusPoints;
    private final boolean isMultiplier;

    public ModifierCard(String id, String cardName,String cardImageName, int bonusPoints, boolean isMultiplier) {
        super(id, cardName, cardImageName);
        this.bonusPoints = bonusPoints;
        this.isMultiplier = isMultiplier;
    }

    public int getBonusPoints() { return bonusPoints; }
    public boolean isMultiplier() { return isMultiplier; }
}
