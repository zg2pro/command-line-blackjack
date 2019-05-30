package com.github.zg2pro.command.line.blackjack.shell.game.d;

import com.intuit.gregory.intuit.blackjack.core.Player;
import com.intuit.gregory.intuit.blackjack.core.exceptions.RedCardException;
import com.github.zg2pro.command.line.blackjack.shell.exceptions.UnderConstructionException;
import com.github.zg2pro.command.line.blackjack.shell.game.c.RoundShellComponent;
import com.github.zg2pro.command.line.blackjack.shell.state.GameContextComponent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DoubleDownComponent {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HitShellComponent.class);
    
    @Inject
    private GameContextComponent game;

    @Inject
    private RoundShellComponent roundComponent;
    
    @Inject
    private HitShellComponent hitComponent;
    
    @ShellMethod("Doubledown means you have double your bet before you get a new card")
    public String doubleDown() throws InterruptedException {
        Player p = Player.class.cast(game.getPlayingContextHolder());
        try {
            game.getTheTable().doubleDown(p);
        } catch (RedCardException ex) {
            logger.trace("suspension of the game", ex);
            return roundComponent.redCardExceptionHandler();
        }
        return hitComponent.afterHit(p);
    }

    public Availability doubleDownAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && RoundShellComponent.class.getName().equals(game.getOrigin())
                && game.getTheTable().isDoubleDownAvailable(Player.class.cast(game.getPlayingContextHolder())))
                ? Availability.available()
                : Availability.unavailable("Sorry but you can't ask for insurance until you get your first cards "
                        + "and it's only available when the current score is between 9 and 11");
    }
}
