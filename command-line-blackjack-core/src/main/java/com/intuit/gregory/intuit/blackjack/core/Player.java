package com.intuit.gregory.intuit.blackjack.core;

public class Player {

    private int chips;
    private int moneyOnTable;
    private final Hand cardsHeld = new Hand();
    private boolean insuranceTaken;
    private Hand split;

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public Hand getCardsHeld() {
        return cardsHeld;
    }

    public int getMoneyOnTable() {
        return moneyOnTable;
    }

    public void setMoneyOnTable(int moneyOnTable) {
        this.moneyOnTable = moneyOnTable;
    }

    public boolean isInsuranceTaken() {
        return insuranceTaken;
    }

    public void setInsuranceTaken(boolean insuranceTaken) {
        this.insuranceTaken = insuranceTaken;
    }

    public Hand getSplit() {
        return split;
    }

    public void setSplit(Hand split) {
        this.split = split;
    }
    
    
    
}
