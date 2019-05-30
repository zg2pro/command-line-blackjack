package com.intuit.gregory.intuit.blackjack.shell.game.b;

import com.intuit.gregory.intuit.blackjack.shell.game.a.ModesShellComponent;
import com.intuit.gregory.intuit.blackjack.core.Player;
import static com.intuit.gregory.intuit.blackjack.shell.ShellBoot.ANSI_PURPLE;
import static com.intuit.gregory.intuit.blackjack.shell.ShellBoot.ANSI_RESET;
import com.intuit.gregory.intuit.blackjack.shell.game.c.RoundShellComponent;
import com.intuit.gregory.intuit.blackjack.shell.state.GameContextComponent;
import javax.inject.Inject;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CashInShellComponent {

    @Inject
    private GameContextComponent game;

    @Inject
    private RoundShellComponent firstRound;

    @ShellMethod("The money you'll turn to chips and use at the blackjack table")
    public String cashIn(@ShellOption int money) {
        Player p = Player.class.cast(game.getPlayingContextHolder());
        p.setChips(money);
        int indexOfPlayer = game.getTheTable().getPlayers().indexOf(p);
        if (indexOfPlayer + 1 == game.getTheTable().getPlayers().size()) {
            game.setPlayingContextHolder(game.getTheTable().getPlayers().get(0));
            game.setOrigin(CashInShellComponent.class.getName());
            return "your money has been credited in casino chips, please sit at the table, bets 2-100$"
                    + firstRound.introduceNextRound();
        } else {
            game.setPlayingContextHolder(game.getTheTable().getPlayers().get(indexOfPlayer + 1));
            //TODO: here manage robotplayer
            return "And for this other player, how much money do you want to exchange against casino chips?"
                    + "\n --> Type " + ANSI_PURPLE + "'cash-in 100' " + ANSI_RESET + " for a 100$ in chips";
        }
    }

    public Availability cashInAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && ModesShellComponent.class.getName().equals(game.getOrigin()))
                ? Availability.available()
                : Availability.unavailable("we can't know who wants to cash-in until you select a game mode");
    }

}
