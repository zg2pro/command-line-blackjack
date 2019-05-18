package com.intuit.gregory.intuit.blackjack.shell.state;

import com.intuit.gregory.intuit.blackjack.core.BlackjackTable;
import org.springframework.stereotype.Component;

/**
 * Stateful object that's gonna keep the game status
 */
@Component
public class GameContextComponent {

    private BlackjackTable theTable;

    /**
     * In today's version only Player objects are stored in this field
     * but depending on what evolutions we wanna have on the cinematics of the game
     * we may need to access another kind of object
     */
    private Object playingContextHolder;
    
    private String origin;

    public BlackjackTable getTheTable() {
        return theTable;
    }

    public Object getPlayingContextHolder() {
        return playingContextHolder;
    }

    public void setPlayingContextHolder(Object contextObjectHolder) {
        this.playingContextHolder = contextObjectHolder;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void init() {
        theTable = new BlackjackTable();
        theTable.getTheDeck().init();
    }

}
