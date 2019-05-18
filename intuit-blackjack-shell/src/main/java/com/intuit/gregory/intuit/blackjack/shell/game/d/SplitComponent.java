package com.intuit.gregory.intuit.blackjack.shell.game.d;

import com.intuit.gregory.intuit.blackjack.core.Player;
import com.intuit.gregory.intuit.blackjack.core.exceptions.RedCardException;
import com.intuit.gregory.intuit.blackjack.shell.game.c.RoundShellComponent;
import com.intuit.gregory.intuit.blackjack.shell.state.GameContextComponent;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class SplitComponent {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HitShellComponent.class);

    @Inject
    private GameContextComponent game;
    @Inject
    private RoundShellComponent roundComponent;

    @Inject
    private HitShellComponent hitComponent;
    
    @ShellMethod("Split means you have a pair and make two hands with one card in each")
    public String split() throws InterruptedException {
        Player p = Player.class.cast(game.getPlayingContextHolder());
        try {
            game.getTheTable().split(p);
        } catch (RedCardException ex) {
            logger.trace("suspension of the game", ex);
            return roundComponent.redCardExceptionHandler();
        }
        return hitComponent.afterHit(p);
    }

    public Availability splitAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && RoundShellComponent.class.getName().equals(game.getOrigin())
                && game.getTheTable().isSplitAvailable(Player.class.cast(game.getPlayingContextHolder())))
                ? Availability.available()
                : Availability.unavailable("Sorry but you can't split until you get your first cards "
                        + "and it's only available when you have a pair");
    }
}
