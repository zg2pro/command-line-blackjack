package com.github.zg2pro.command.line.blackjack.core;

import com.github.zg2pro.command.line.blackjack.cards.core.Card;
import com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorCardsEnum;
import com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorsEnum;
import com.github.zg2pro.command.line.blackjack.core.exceptions.RedCardException;
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
