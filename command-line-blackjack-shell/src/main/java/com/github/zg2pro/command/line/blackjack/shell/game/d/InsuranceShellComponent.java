package com.github.zg2pro.command.line.blackjack.shell.game.d;

import com.intuit.gregory.intuit.blackjack.core.Player;
import com.github.zg2pro.command.line.blackjack.shell.game.c.RoundShellComponent;
import com.github.zg2pro.command.line.blackjack.shell.state.GameContextComponent;
import javax.inject.Inject;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class InsuranceShellComponent {

    @Inject
    private GameContextComponent game;
    
    @Inject
    private HitShellComponent hitComponent;
    

    @ShellMethod("Insurance means you want to give 50% extra off-bet to insurance a loss against a croupier's blackjack")
    public String insurance() throws InterruptedException {
        Player p = Player.class.cast(game.getPlayingContextHolder());
        game.getTheTable().bookInsurance(p);
        return hitComponent.afterHit(p);
    }

    public Availability insuranceAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && RoundShellComponent.class.getName().equals(game.getOrigin())
                && game.getTheTable().isInsuranceAvailable(Player.class.cast(game.getPlayingContextHolder())))
                ? Availability.available()
                : Availability.unavailable("Sorry but you can't ask for insurance until you get your first cards "
                        + "and it's only available when the croupier holds an ace");
    }
}
