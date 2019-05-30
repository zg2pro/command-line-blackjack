package com.intuit.gregory.intuit.cards.core.enums;

/**
 * color like spades, hearts, diamonds or clubs
 */
public enum ColorCardsEnum {

    //1 or 11 but 11 will be counted only once in the hand
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    private final int cardValue;

    public int getCardValue() {
        return cardValue;
    }

    private ColorCardsEnum(int cardValue) {
        this.cardValue = cardValue;
    }

}
