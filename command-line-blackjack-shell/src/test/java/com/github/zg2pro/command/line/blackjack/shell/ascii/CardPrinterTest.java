package com.github.zg2pro.command.line.blackjack.shell.ascii;

import com.github.zg2pro.command.line.blackjack.shell.ascii.CardPrinter;
import com.intuit.gregory.intuit.cards.core.Card;
import com.intuit.gregory.intuit.cards.core.enums.ColorCardsEnum;
import com.intuit.gregory.intuit.cards.core.enums.ColorsEnum;
import com.intuit.gregory.intuit.blackjack.core.Hand;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.shell.standard.commands.Clear;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

@Configuration
@ComponentScan(basePackages = "com.intuit.gregory.intuit.blackjack.shell.*")
class TestBoot implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }

    @Bean
    public Clear clear() {
        return Mockito.mock(Clear.class);
    }

}

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TestBoot.class)
public class CardPrinterTest {

    @Autowired
    private CardPrinter cardPrinter;

    @Value("classpath:templates/handTest.ascii")
    private Resource handTestExpectedResult;

    @Value("classpath:templates/croupierTest.ascii")
    private Resource croupierTestExpectedResult;
    
    private Hand handFactory() {
        Card firstCard = new Card();
        firstCard.setColor(ColorsEnum.CLUBS);
        firstCard.setCard(ColorCardsEnum.ACE);
        Card secondCard = new Card();
        secondCard.setColor(ColorsEnum.DIAMONDS);
        secondCard.setCard(ColorCardsEnum.JACK);
        Card thirdCard = new Card();
        thirdCard.setColor(ColorsEnum.HEARTS);
        thirdCard.setCard(ColorCardsEnum.TEN);
        Hand h = new Hand();
        h.add(firstCard);
        h.add(secondCard);
        h.add(thirdCard);
        return h;
    }

    @Test
    public void handPrint() throws IOException {
        Hand h = handFactory();
        String result = cardPrinter.printHand(h);
        InputStream is = handTestExpectedResult.getInputStream();
        String fileContent = FileCopyUtils.copyToString(new InputStreamReader(is, Charset.forName("UTF-8")));
        fileContent = fileContent.replaceAll("z", " ").replaceAll("\r", "");
        Assert.assertEquals(fileContent, result);
    }

    @Test
    public void croupierPrint() throws IOException {
        Hand h = handFactory();
        String result = cardPrinter.printCroupierHand(h);
        InputStream is = croupierTestExpectedResult.getInputStream();
        String fileContent = FileCopyUtils.copyToString(new InputStreamReader(is, Charset.forName("UTF-8")));
        fileContent = fileContent.replaceAll("z", " ").replaceAll("\r", "");
        Assert.assertEquals(fileContent, result);
    }

}
