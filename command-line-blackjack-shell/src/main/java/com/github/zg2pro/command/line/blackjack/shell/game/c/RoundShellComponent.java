package com.github.zg2pro.command.line.blackjack.shell.game.c;

import com.github.zg2pro.command.line.blackjack.core.Player;
import com.github.zg2pro.command.line.blackjack.core.exceptions.RedCardException;
import com.github.zg2pro.command.line.blackjack.shell.game.b.CashInShellComponent;
import static com.github.zg2pro.command.line.blackjack.shell.ShellBoot.ANSI_PURPLE;
import static com.github.zg2pro.command.line.blackjack.shell.ShellBoot.ANSI_RESET;
import com.github.zg2pro.command.line.blackjack.shell.ascii.CardPrinter;
import com.github.zg2pro.command.line.blackjack.shell.state.GameContextComponent;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.commands.Clear;

@ShellComponent
public class RoundShellComponent {

    private static final Logger logger = LoggerFactory.getLogger(RoundShellComponent.class);

    @Inject
    private GameContextComponent game;

    @Inject
    private Clear clearComponent;

    @Inject
    private CardPrinter cardPrinter;

    public String introduceNextRound() {
        return "\n --> How much would you like to bet for the next round?"
                + "\n --> Type " + ANSI_PURPLE + "'bet 2' " + ANSI_RESET + " for a 2$ bet";
    }

    /**
     * creating a real exception handler in spring shell framework is not
     * available natively in the framework
     *
     * @return the message displayed in std output
     */
    public String redCardExceptionHandler() {
        for (Player p : game.getTheTable().getPlayers()) {
            p.setChips(p.getMoneyOnTable());
            p.setMoneyOnTable(0);
        }
        game.setPlayingContextHolder(game.getTheTable().getPlayers().get(0));
        game.setOrigin(CashInShellComponent.class.getName());
        return "Sorry, not enough cards remaining in the deck, the game is suspended"
                + "\n you can take back your bets, we need a few seconds to prepare a new deck..."
                + "\n when ready, please type " + ANSI_PURPLE + "'bet 2' " + ANSI_RESET + " if you want to start the next round with 2$";
    }

    @ShellMethod("The money you initially bet")
    public String bet(@ShellOption int chips) throws InterruptedException {
        if (chips < 2 || chips > 100 || chips % 2 == 1) {
            return "This isn't a valid bet, you must be between 2 and 100$ and your bet "
                    + "should be a multiple of the downer limit";
        }
        Player p = Player.class.cast(game.getPlayingContextHolder());
        if (p.getChips() < chips) {
            return "You don't have this much, try again please";
        }
        p.setMoneyOnTable(chips);
        p.setChips(p.getChips() - chips);
        int indexOfPlayer = game.getTheTable().getPlayers().indexOf(p);
        if (indexOfPlayer + 1 == game.getTheTable().getPlayers().size()) {
            System.out.println("Your bet is placed on the table.  Distributing cards...");
            if (!game.getTheTable().isCardsDistributed()) {
                try {
                    game.getTheTable().distribute();
                } catch (RedCardException rce) {
                    logger.trace("suspension of the game", rce);
                    return redCardExceptionHandler();
                }
            }
            Thread.sleep(2000);
            return allBetsDownStartPlaying();
        } else {
            game.setPlayingContextHolder(game.getTheTable().getPlayers().get(indexOfPlayer + 1));
            //TODO: here manage robotplayer
            return "Next player, how much money do you want to bet on this round?"
                    + "\n --> Type " + ANSI_PURPLE + "'bet 2' " + ANSI_RESET + " for a 2$ bet";
        }
    }

    private String allBetsDownStartPlaying() {
        Player p = game.getTheTable().getPlayers().get(0);
        game.setPlayingContextHolder(p);
        game.setOrigin(RoundShellComponent.class.getName());
        return decisions(p);
    }

    public String decisions(Player p) {
        int playerIndex = game.getTheTable().getPlayers().indexOf(p) + 1;
        clearComponent.clear();
        StringBuilder sb = new StringBuilder();
        sb.append("######  Turn of player ")
                .append(playerIndex)
                .append("\n In front of the croupier:\n")
                .append(cardPrinter.printCroupierHand(game.getTheTable().getCroupier()))
                .append("\n\n\n And this what you've got:\n")
                .append(cardPrinter.printHand(p.getCardsHeld()));
        if (p.getSplit() != null) {
            sb.append("\n\nAnd in your split hand:\n");
            sb.append(cardPrinter.printHand(p.getSplit()));
        }
        sb.append("\n\n\n Player ")
                .append(playerIndex)
                .append(", Would you like another card?\n Your options:")
                .append("\n Get one more card, type " + ANSI_PURPLE + "'hit' " + ANSI_RESET + " ('hit 2' to hit on the second hand after a split)")
                .append("\n Tell the croupier you're served, type " + ANSI_PURPLE + "'stand' " + ANSI_RESET);
        if (game.getTheTable().isInsuranceAvailable(p)) {
            sb.append("\n The croupier has an ace, get an insurance, type " + ANSI_PURPLE + "'insurance' " + ANSI_RESET);
        }
        if (game.getTheTable().isSplitAvailable(p)) {
            sb.append("\n You have a pair, type " + ANSI_PURPLE + "'split' " + ANSI_RESET);
        }
        if (game.getTheTable().isDoubleDownAvailable(p)) {
            sb.append("\n You have between 9 and 11 points, type " + ANSI_PURPLE + "'double-down' " + ANSI_RESET + " to double-down your bets before you get an extra card");
        }
        return sb.toString();
    }

    public Availability betAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && CashInShellComponent.class.getName().equals(game.getOrigin()))
                ? Availability.available()
                : Availability.unavailable("Sorry but you can't bet until we know how many casino chips you've got");
    }
}
