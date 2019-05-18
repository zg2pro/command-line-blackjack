package com.intuit.gregory.intuit.blackjack.core;

import com.intuit.gregory.intuit.cards.core.Card;
import com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum;
import com.intuit.gregory.intuit.cards.core.enums.ColorsEnum;
import com.intuit.gregory.intuit.blackjack.core.exceptions.RedCardException;
import org.junit.Assert;
import org.junit.Test;

public class BlackjackTableTest {

    @Test
    public void softSeventeenTest() throws RedCardException{
        BlackjackTable bjT = new BlackjackTable();
        bjT.getCroupier().add(new Card());
        bjT.getCroupier().get(0).setColor(ColorsEnum.CLUBS);
        bjT.getCroupier().get(0).setCard(ColorCardsEnum.ACE);
        bjT.getCroupier().add(new Card());
        bjT.getCroupier().get(1).setColor(ColorsEnum.DIAMONDS);
        bjT.getCroupier().get(1).setCard(ColorCardsEnum.ACE);
        bjT.getTheDeck().init();
        bjT.doSoftSeventeen();
        Assert.assertTrue(bjT.getCroupier().bestPoints() > 16);
    }
    
}
