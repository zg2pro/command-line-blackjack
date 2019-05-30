package com.github.zg2pro.command.line.blackjack.core;

import com.github.zg2pro.command.line.blackjack.cards.core.Card;
import com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorCardsEnum;
import com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorsEnum;
import com.github.zg2pro.command.line.blackjack.core.exceptions.RedCardException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * In Casinos, the deck contains between 5 and 8 sets of 52 cards + a red card
 * that gets placed usually between 40 and 60 cards before the end of the deck.
 * When the croupier sees the red card in the deck, the game is suspended, and
 * cards have to be reshuffled
 */
public class Deck extends ArrayList<Card> {

    public void init() {
        this.clear();
        for (int i = 0; i < 8; i++) {
            for (ColorsEnum color : ColorsEnum.values()) {
                for (ColorCardsEnum ce : ColorCardsEnum.values()) {
                    Card c = new Card();
                    c.setCard(ce);
                    c.setColor(color);
                    this.add(c);
                }
            }
        }
        Collections.shuffle(this);
        int positionOfTheRedCard = this.size() - this.size() / 8;
        add(positionOfTheRedCard, new Card());
    }

    public Card unstackCard() throws RedCardException {
        Card c = remove(0);
        if (c.getCard() == null) {
            throw new RedCardException();
        }
        return c;
    }

}
