package com.github.zg2pro.command.line.blackjack.core;

import com.github.zg2pro.command.line.blackjack.core.enums.GameResultEnum;
import com.github.zg2pro.command.line.blackjack.core.exceptions.RedCardException;
import static com.github.zg2pro.command.line.blackjack.cards.core.enums.ColorCardsEnum.ACE;
import static com.github.zg2pro.command.line.blackjack.core.enums.GameResultEnum.BLACKJACK;
import static com.github.zg2pro.command.line.blackjack.core.enums.GameResultEnum.DRAW;
import static com.github.zg2pro.command.line.blackjack.core.enums.GameResultEnum.INSURANCE_LOST;
import static com.github.zg2pro.command.line.blackjack.core.enums.GameResultEnum.LOST;
import static com.github.zg2pro.command.line.blackjack.core.enums.GameResultEnum.WIN;
import java.util.ArrayList;
import java.util.List;

public class BlackjackTable {

    private final List<Player> players = new ArrayList<>();
    private final Hand croupier = new Hand();
    private final Deck theDeck = new Deck();
    private boolean cardsDistributed = false;

    public List<Player> getPlayers() {
        return players;
    }

    public Hand getCroupier() {
        return croupier;
    }

    public Deck getTheDeck() {
        return theDeck;
    }

    public void distribute() throws RedCardException {
        for (Player p : players) {
            if (!p.getCardsHeld().isEmpty()) {
                cardsDistributed = true;
            }
        }
        if (!croupier.isEmpty()) {
            cardsDistributed = true;
        }
        if (cardsDistributed) {
            throw new IllegalStateException("players already have cards in their hands");
        }
        for (int i = 0; i < 2; i++) {
            for (Player p : players) {
                p.getCardsHeld().add(theDeck.unstackCard());
            }
            croupier.add(theDeck.unstackCard());
        }
        cardsDistributed = true;
    }

    public void cleanTableCards() {
        for (Player p : players) {
            p.getCardsHeld().clear();
        }
        croupier.clear();
        cardsDistributed = false;
    }

    public boolean isCardsDistributed() {
        return cardsDistributed;
    }

    public boolean isInsuranceAvailable(Player p) {
        boolean basicConditions = cardsDistributed
                && !p.isInsuranceTaken()
                && croupier.size() == 2
                && p.getCardsHeld().size() == 2
                && ACE.equals(croupier.get(0).getCard());
        int halfOfBet = p.getMoneyOnTable() / 2;
        return basicConditions && halfOfBet <= p.getChips();
    }

    public void bookInsurance(Player p) {
        if (!isInsuranceAvailable(p)) {
            throw new IllegalArgumentException("this player can't get an insurance");
        }
        int halfOfBet = p.getMoneyOnTable() / 2;
        p.setMoneyOnTable(p.getMoneyOnTable() + halfOfBet);
        p.setChips(p.getChips() - halfOfBet);
        p.setInsuranceTaken(true);
    }

    public boolean isSplitAvailable(Player p) {
        return cardsDistributed
                && p.getSplit() == null
                && croupier.size() == 2
                && p.getCardsHeld().size() == 2
                && p.getCardsHeld().get(0).getCard().equals(p.getCardsHeld().get(1).getCard());
    }

    public void split(Player p) throws RedCardException {
        if (!isSplitAvailable(p)) {
            throw new IllegalArgumentException("this player can't split");
        }
        p.setSplit(new Hand());
        p.getSplit().add(p.getCardsHeld().get(1));
        p.getCardsHeld().remove(1);
        p.getCardsHeld().add(theDeck.unstackCard());
        p.getSplit().add(theDeck.unstackCard());
    }

    public boolean isDoubleDownAvailable(Player p) {
        boolean basicConditions = cardsDistributed
                && croupier.size() == 2
                && p.getCardsHeld().size() == 2
                && p.getSplit() == null;
        if (!basicConditions) {
            return false;
        }
        for (int point : p.getCardsHeld().points()) {
            if (point > 8 && point < 12) {
                return true;
            }
        }
        return false;
    }

    public void doubleDown(Player p) throws RedCardException {
        if (!isDoubleDownAvailable(p)) {
            throw new IllegalArgumentException("this player can't double-down");
        }
        int doubleValue = p.getMoneyOnTable();
        p.setMoneyOnTable(2 * doubleValue);
        p.setChips(p.getChips() - doubleValue);
        p.getCardsHeld().add(theDeck.unstackCard());
    }

    public void hit(Player p, boolean onSplit) throws RedCardException {
        if (onSplit) {
            if (p.getSplit() == null) {
                throw new IllegalArgumentException("you can't hit the split cause there is no split");
            } else {
                p.getSplit().add(theDeck.unstackCard());
            }
        } else {
            p.getCardsHeld().add(theDeck.unstackCard());
        }
    }

    public void doSoftSeventeen() throws RedCardException {
        while (croupier.bestPoints() < 17) {
            croupier.add(theDeck.unstackCard());
        }
    }

    public GameResultEnum result(Player p) {
        if (p.getSplit() != null
                && p.getSplit().bestPoints() > p.getCardsHeld().bestPoints()
                && p.getSplit().bestPoints() < 22) {
            p.getCardsHeld().clear();
            p.getCardsHeld().addAll(p.getSplit());
            p.setSplit(null);
        }
        if (croupier.isBlackjack() && p.isInsuranceTaken() && !p.getCardsHeld().isBlackjack()) {
            return INSURANCE_LOST;
        }
        if (croupier.isBlackjack() && p.getCardsHeld().isBlackjack()) {
            return DRAW;
        }
        if (croupier.isBlackjack() && !p.getCardsHeld().isBlackjack()) {
            return LOST;
        }
        if (!croupier.isBlackjack() && p.getCardsHeld().isBlackjack()) {
            return BLACKJACK;
        }
        // blackjack is better than 21
        if (croupier.isBlown() && p.getCardsHeld().isBlown()) {
            return DRAW;
        }
        if (croupier.isBlown() && !p.getCardsHeld().isBlown()) {
            return WIN;
        }
        if (!croupier.isBlown() && p.getCardsHeld().isBlown()) {
            return LOST;
        }
        if (croupier.bestPoints() > p.getCardsHeld().bestPoints()) {
            return LOST;
        }
        if (croupier.bestPoints() < p.getCardsHeld().bestPoints()) {
            return WIN;
        }
        if (croupier.bestPoints() == p.getCardsHeld().bestPoints()) {
            return DRAW;
        }
        throw new IllegalStateException("something I didn't think of?");
    }

    public void applyResult(Player p, GameResultEnum result) {
        int insurance = p.isInsuranceTaken() ? p.getMoneyOnTable() / 3 : 0;
        int initialBet = p.getMoneyOnTable() - insurance;
        if (WIN.equals(result)) {
            p.setChips(p.getChips() + 2 * initialBet);
        } else if (BLACKJACK.equals(result)) {
            p.setChips(p.getChips() + 2 * initialBet + initialBet / 2);
        } else if (DRAW.equals(result) || INSURANCE_LOST.equals(result)) {
            p.setChips(p.getChips() + initialBet);
        }
        //in case LOST, nothing particular to do
        p.setMoneyOnTable(0);
    }
}
