package com.github.zg2pro.command.line.blackjack.shell.ascii;

import com.intuit.gregory.intuit.cards.core.Card;
import com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum;
import static com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum.JACK;
import static com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum.KING;
import static com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum.QUEEN;
import com.intuit.gregory.intuit.cards.core.enums.ColorsEnum;
import com.intuit.gregory.intuit.blackjack.core.Hand;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class CardPrinter {

    /**
     * in memory map used to draw the cards in the console
     */
    private Map<ColorsEnum, String> colorAsciiArt = new HashMap<>();
    private Map<ColorCardsEnum, String> cardToSymbol = new HashMap<>();
    private String redCard;

    @Inject
    private ResourceLoader resourceLoader;

    @Value("classpath:templates/face_down.ascii")
    private Resource faceDownCard;

    @PostConstruct
    private void init() throws IOException {
        for (ColorsEnum color : ColorsEnum.values()) {
            String filePath = "classpath:templates/" + color.name().toLowerCase() + ".ascii";
            InputStream is = resourceLoader.getResource(filePath).getInputStream();
            String fileContent = FileCopyUtils.copyToString(new InputStreamReader(is, Charset.forName("UTF-8")));
            fileContent = fileContent.replaceAll("z", " ");
            colorAsciiArt.put(color, fileContent);
        }
        Collection<ColorCardsEnum> collectionHeads = Arrays.asList(new ColorCardsEnum[]{JACK, QUEEN, KING});
        for (ColorCardsEnum cce : ColorCardsEnum.values()) {
            if (collectionHeads.contains(cce)) {
                cardToSymbol.put(cce, cce.name().charAt(0) + "");
            } else {
                cardToSymbol.put(cce, cce.getCardValue() + "");
            }
        }
        InputStream is = faceDownCard.getInputStream();
        redCard = FileCopyUtils.copyToString(new InputStreamReader(is, Charset.forName("UTF-8")));
        redCard = redCard.replaceAll("z", " ").replaceAll("\r", "");
    }

    public String print(Card c) {
        if (c.getColor() == null){
            return redCard;
        }
        String drawnCard = colorAsciiArt.get(c.getColor());
        String cardSymbol = cardToSymbol.get(c.getCard());
        if (cardSymbol.length() < 2) {
            cardSymbol += " ";
        }
        return drawnCard.replaceAll("pp", cardSymbol);
    }

    private String printHand(Hand h, boolean croupier) {
        Hand displayHand = h;
        if (croupier) {
            displayHand = new Hand();
            displayHand.add(h.get(0));
            for (int i = 1; i < h.size(); i++) {
                displayHand.add(new Card());
            }
        }

        //6 lines of each ascii art
        String[] finalLines = new String[6];
        for (int i = 0; i < 6; i++) {
            finalLines[i] = "";
            for (Card c : displayHand) {
                finalLines[i] += print(c).split("\n")[i].replaceAll("\r", "");
            }
        }
        return String.join("\n", finalLines);
    }

    public String printCroupierHand(Hand h) {
        return printHand(h, true);
    }

    public String printHand(Hand h) {
        return printHand(h, false);
    }
}
