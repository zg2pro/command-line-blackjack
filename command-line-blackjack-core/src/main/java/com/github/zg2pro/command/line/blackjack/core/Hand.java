package com.github.zg2pro.command.line.blackjack.core;

import com.intuit.gregory.intuit.cards.core.Card;
import com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum;
import static com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum.TEN;
import java.util.ArrayList;

public class Hand extends ArrayList<Card> {

    /**
     * when the hand contains aces, one of them can be counted as 11 eleven
     * points instead of 1 so we need to maintain always 2 point sums for each
     * hand
     * @return the points given by the cards
     */
    public int[] points() {
        //no need to count the aces as only one of them should be counted with 11 points,
        //if 4 aces: 1 + 1 + 1 + 1 or 11 + 1 + 1 + 1
        int sum = 0;
        boolean aces = false;
        for (Card ce : this) {
            sum += ce.getCard().getCardValue();
            if (ColorCardsEnum.ACE.equals(ce.getCard())) {
                aces = true;
            }
        }
        if (aces) {
            return new int[]{sum, sum + 10};
        } else {
            return new int[]{sum};
        }
    }

    public int bestPoints() {
        int points[] = points();
        if (points.length > 1 && points[1] < 22) {
            return points[1];
        }
        return points[0];
    }

    /**
     * remember nonetheless that a split hand can't be a blackjack
     *
     * @return true if the cards are blackjack
     */
    public boolean isBlackjack() {
        if (this.size() != 2) {
            return false;
        }
        return (this.get(0).getCard().getCardValue() == 1 
                && this.get(1).getCard().getCardValue() == 10 
                && !TEN.equals(this.get(1).getCard())
                || this.get(0).getCard().getCardValue() == 10 
                && !TEN.equals(this.get(0).getCard())
                && this.get(1).getCard().getCardValue() == 1);
    }

    public boolean isBlown() {
        boolean blown = true;
        for (int i : points()) {
            if (i < 22) {
                blown = false;
            }
        }
        return blown;
    }

}
