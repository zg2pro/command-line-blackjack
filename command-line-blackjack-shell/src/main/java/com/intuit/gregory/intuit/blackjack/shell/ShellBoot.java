package com.intuit.gregory.intuit.blackjack.shell;

import org.jline.utils.AttributedStyle;
import org.jline.utils.AttributedString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
public class ShellBoot {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        SpringApplication.run(ShellBoot.class, args);
    }

    @Bean
    public PromptProvider blackjackPromptProvider() {
        System.out.println("####################################################################################################");
        System.out.println("Welcome to Gregory's Casino, would you like to come inside?");
        System.out.println("####################################################################################################");
        System.out.println("---->   Type command 'enter' or 'exit' if you want to leave the game");
        return () -> new AttributedString("my-shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

}
