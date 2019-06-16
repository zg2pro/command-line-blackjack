package com.github.zg2pro.command.line.blackjack.shell.game.a;

import com.github.zg2pro.command.line.blackjack.core.Player;
import static com.github.zg2pro.command.line.blackjack.shell.ShellBoot.ANSI_PURPLE;
import static com.github.zg2pro.command.line.blackjack.shell.ShellBoot.ANSI_RESET;
import com.github.zg2pro.command.line.blackjack.shell.state.GameContextComponent;
import com.github.zg2pro.command.line.blackjack.shell.exceptions.UnderConstructionException;
import javax.inject.Inject;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.commands.Quit;

@ShellComponent
public class ModesShellComponent {

    @Inject
    private GameContextComponent game;

    @Inject
    private Quit quit;

    @ShellMethod("Continue the game")
    public String continueTheGame(@ShellOption(defaultValue = "____NULL____") String noPrompt) {
        if ("____NULL____".equals(noPrompt)) {
            return "would you like to continue?";
        } else if ("yes".equals(noPrompt)) {
            return enter();
        } else if ("no".equals(noPrompt)) {
            quit.quit();
            return "quitting...";
        } else {
            throw new IllegalStateException("not a good answer");
        }
    }

    @ShellMethod("Enter the casino")
    public String enter() {
        return "Please select game mode"
                + "\n If you want to duel the croupier, type  " + ANSI_PURPLE + "'single' " + ANSI_RESET
                + "\n If you want to compete with a human neighbor at the same table, type " + ANSI_PURPLE + "'multiplayer' " + ANSI_RESET
                + "\n If you want to compete with a machine neighbor at the same table, type " + ANSI_PURPLE + "'robotplayer' " + ANSI_RESET;
    }

    /**
     * the player knowing the command can go directly to the mode selection
     * without typing enter
     *
     * @return the single mode first operations
     */
    @ShellMethod("Duel the croupier")
    public String single() {
        game.init();
        Player p = new Player();
        game.setPlayingContextHolder(p);
        game.setOrigin(ModesShellComponent.class.getName());
        game.getTheTable().getPlayers().add(p);
        return "How much money do you want to exchange against casino chips?"
                + "\n --> Type " + ANSI_PURPLE + "'cash-in 100' " + ANSI_RESET + " for a 100$ in chips";
    }

    @ShellMethod("Take turns in front of the computer and compete with a friend at the same table, against the croupier")
    public String multiplayer() {
        game.init();
        Player p1 = new Player();
        Player p2 = new Player();
        game.setPlayingContextHolder(p1);
        game.setOrigin(ModesShellComponent.class.getName());
        game.getTheTable().getPlayers().add(p1);
        game.getTheTable().getPlayers().add(p2);
        return "First player: How much money do you want to exchange against casino chips?"
                + "\n --> Type " + ANSI_PURPLE + "'cash-in 100' " + ANSI_RESET + " for a 100$ in chips";
    }

    /**
     * TODO necessary:
     *
     * 1- create an operations enum in blackjack-core
     *
     * 2- method adapted from roundComponent.decisions listing available
     * decisions
     *
     * 3- pick decision from there instead of return to standard input (this
     * happens in 3 occurences of "TODO: here manage robotplayer")
     *
     * 4- add a boolean flag in player to know this player is robot
     *
     * 5- add a strategy algorithm for the robot player in player class or
     * blackjacktable class
     *
     * @return the robotplayer first operations
     */
    @ShellMethod("Compete with a machine neighbor at the same table")
    public String robotplayer() {
        throw new UnderConstructionException("multiplayer mode isn't yet available");
    }

}
