package com.github.zg2pro.command.line.blackjack.shell.game.e;

import com.intuit.gregory.intuit.blackjack.core.Player;
import com.intuit.gregory.intuit.blackjack.core.enums.GameResultEnum;
import com.intuit.gregory.intuit.blackjack.core.exceptions.RedCardException;
import static com.github.zg2pro.command.line.blackjack.shell.ShellBoot.ANSI_PURPLE;
import static com.github.zg2pro.command.line.blackjack.shell.ShellBoot.ANSI_RESET;
import com.github.zg2pro.command.line.blackjack.shell.ascii.CardPrinter;
import com.github.zg2pro.command.line.blackjack.shell.game.b.CashInShellComponent;
import com.github.zg2pro.command.line.blackjack.shell.game.c.RoundShellComponent;
import com.github.zg2pro.command.line.blackjack.shell.state.GameContextComponent;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Clear;

@ShellComponent
public class StandShellComponent {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StandShellComponent.class);

    @Inject
    private GameContextComponent game;

    @Inject
    private RoundShellComponent roundComponent;

    @Inject
    private Clear clear;

    @Inject
    private CardPrinter cardPrinter;

    @ShellMethod("Stand means you want to keep your hand as is")
    public String stand() throws InterruptedException {
        Player p = Player.class.cast(game.getPlayingContextHolder());
        int indexOfPlayer = game.getTheTable().getPlayers().indexOf(p);
        if (indexOfPlayer + 1 == game.getTheTable().getPlayers().size()) {
            try {
                game.getTheTable().doSoftSeventeen();
            } catch (RedCardException ex) {
                logger.trace("suspension of the game", ex);
                return roundComponent.redCardExceptionHandler();
            }
            p = game.getTheTable().getPlayers().get(0);
            game.setPlayingContextHolder(p);
            return roundResults(p);
        } else {
            p = game.getTheTable().getPlayers().get(indexOfPlayer + 1);
            //TODO: here manage robotplayer
            game.setPlayingContextHolder(p);
            return roundComponent.decisions(p);
        }
    }

    private String roundResults(Player p) throws InterruptedException {
        int playerIndex = game.getTheTable().getPlayers().indexOf(p) + 1;
        clear.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("############################ Player ")
                .append(playerIndex)
                .append("\nIn front of the croupier:\n")
                .append(cardPrinter.printHand(game.getTheTable().getCroupier()))
                .append("\n\n\n And this what you've got:\n")
                .append(cardPrinter.printHand(p.getCardsHeld()));
        if (p.getSplit() != null) {
            sb.append("\n\nAnd in your split hand:\n");
            sb.append(cardPrinter.printHand(p.getSplit()));
        }
        GameResultEnum gre = game.getTheTable().result(p);
        sb.append("\n\n#########").append(gre).append("#########\n");
        game.getTheTable().applyResult(p, gre);
        int balance = p.getChips();
        sb.append("#Your balance after this round is:#").append(balance).append("#########\n");
        Player nextPlayer = null;
        boolean passedCurrent = false;
        for (Player potentialNext : game.getTheTable().getPlayers()) {
            if (potentialNext.equals(p)) {
                passedCurrent = true;
            } else if (passedCurrent) {
                nextPlayer = potentialNext;
            }
        }
        if (balance < 2) {
            sb.append("\nout of casino chips, you're invited to leave the table.");
            game.getTheTable().getPlayers().remove(p);
        }
        if (nextPlayer != null) {
            System.out.println(sb.toString());
            Thread.sleep(3000);
            game.setPlayingContextHolder(nextPlayer);
            return roundResults(nextPlayer);
        } else {
            int remainingPlayers = game.getTheTable().getPlayers().size();
            sb.append("\nplayers still at the table:")
                    .append(remainingPlayers);
            game.getTheTable().cleanTableCards();
            if (remainingPlayers > 0) {
                game.setPlayingContextHolder(game.getTheTable().getPlayers().get(0));
                game.setOrigin(CashInShellComponent.class.getName());
                return sb.toString() + "\nWe're about to start a new round, "
                        + "\n Please type " + ANSI_PURPLE + "'bet 2' " + ANSI_RESET + " if you want to bet 2$";
            } else {
                return sb.toString() + "please 'exit'";
            }
        }
    }

    public Availability standAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && RoundShellComponent.class.getName().equals(game.getOrigin()))
                ? Availability.available()
                : Availability.unavailable("Sorry but you can't stand until you get your first cards");
    }
}
