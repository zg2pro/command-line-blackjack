package com.github.zg2pro.command.line.blackjack.shell.game.d;

import com.intuit.gregory.intuit.blackjack.core.Player;
import com.intuit.gregory.intuit.blackjack.core.exceptions.RedCardException;
import com.github.zg2pro.command.line.blackjack.shell.ascii.CardPrinter;
import com.github.zg2pro.command.line.blackjack.shell.game.c.RoundShellComponent;
import com.github.zg2pro.command.line.blackjack.shell.game.e.StandShellComponent;
import com.github.zg2pro.command.line.blackjack.shell.state.GameContextComponent;
import java.util.Arrays;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.commands.Clear;

@ShellComponent
public class HitShellComponent {

    private static final Logger logger = LoggerFactory.getLogger(HitShellComponent.class);

    @Inject
    private GameContextComponent game;

    @Inject
    private RoundShellComponent roundComponent;

    @Inject
    private Clear clearComponent;

    @Inject
    private CardPrinter cardPrinter;

    @Inject
    private StandShellComponent standComponent;

    @ShellMethod("Hit means you want another card")
    public String hit(@ShellOption(defaultValue = "1") String splitHandIndex) throws InterruptedException {
        if (!Arrays.asList(new String[]{"1", "2"}).contains(splitHandIndex)) {
            throw new IllegalArgumentException("What are you trying to hit?");
        }
        Player p = Player.class.cast(game.getPlayingContextHolder());
        if ("1".equals(splitHandIndex) && p.getCardsHeld().isBlown()){
            throw new IllegalArgumentException("This hand has already blown, you can hit only the second hand");
        }
        if ("2".equals(splitHandIndex) && p.getSplit().isBlown()){
            throw new IllegalArgumentException("This hand has already blown, you can hit only the first hand");
        }
        try {
            game.getTheTable().hit(p, "2".equals(splitHandIndex));
        } catch (RedCardException ex) {
            logger.trace("suspension of the game", ex);
            return roundComponent.redCardExceptionHandler();
        }
        return afterHit(p);
    }

    public String afterHit(Player p) throws InterruptedException {
        if (p.getSplit() == null && p.getCardsHeld().isBlown()
                || p.getSplit() != null && p.getSplit().isBlown() && p.getCardsHeld().isBlown()) {
            System.out.println("You're blown, sorry sir.");
            int playerIndex = game.getTheTable().getPlayers().indexOf(p);
            clearComponent.clear();
            StringBuilder sb = new StringBuilder();
            sb.append("######  Turn of player ")
                    .append(playerIndex)
                    .append("\n In front of the croupier: \n")
                    .append(cardPrinter.printCroupierHand(game.getTheTable().getCroupier()))
                    .append("\n\n\n And this what you've got:\n")
                    .append(cardPrinter.printHand(p.getCardsHeld()));
            if (p.getSplit() != null) {
                sb.append("\n\nAnd in your split hand:\n");
                sb.append(cardPrinter.printHand(p.getSplit()));
            }
            sb.append("\n");
            System.out.println(sb.toString());
            Thread.sleep(3000);
            if (playerIndex + 1 < game.getTheTable().getPlayers().size()) {
                p = game.getTheTable().getPlayers().get(playerIndex + 1);
                //TODO: here manage robotplayer
                game.setPlayingContextHolder(p);
                return roundComponent.decisions(p);
            } else {
                return standComponent.stand();
            }
        }
        return roundComponent.decisions(p);
    }

    public Availability hitAvailability() {
        return (game.getPlayingContextHolder() instanceof Player
                && RoundShellComponent.class.getName().equals(game.getOrigin()))
                ? Availability.available()
                : Availability.unavailable("Sorry but you can't hit until you get your first cards");
    }
}
