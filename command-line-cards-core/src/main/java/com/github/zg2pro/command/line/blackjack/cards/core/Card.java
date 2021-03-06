package com.github.zg2pro.command.line.blackjack.cards.core;

import com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorsEnum;
import com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorCardsEnum;

/**
 * only jokers are usually not described by card type and colors
 * so this class can be used only for 32 or 52 cards games
 */
public class Card {

    private ColorCardsEnum card;
    private ColorsEnum color;

    public ColorCardsEnum getCard() {
        return card;
    }

    public void setCard(ColorCardsEnum card) {
        this.card = card;
    }

    public ColorsEnum getColor() {
        return color;
    }

    public void setColor(ColorsEnum color) {
        this.color = color;
    }

}
