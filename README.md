
# Intuit Blackjack Exercise

**hint**: Paste this readme.md file in https://stackedit.io/app# if you want to enjoy the markup.

## Prerequisites
JDK >= 8 installed - JAVA_HOME - location of a JDK home dir

## Build
**In Windows**: Run: ./mvnw.cmd clean install
**In Linux:** Run: ./mvnw clean install
(Because I don't know on what machines the project will be built I've added maven wrapper files, also I've taken java 8 as a base)

## Run
From the project root, run: 
*java -jar intuit-blackjack-shell/target/intuit-blackjack-shell-1.0-SNAPSHOT.jar* 

and follow the instructions.

For every command, you can type "help command-name" to see the **manual**, example: "help enter".

There is no 1-2-3 options or Y-N options to select at each step, please remember you have to type your commands in 
full words as requested by standard outputs.

The application supports **TAB completion** almost everywhere possible. So if there is an echo command and the user 
presses e, c, TAB then echo will appear. Should there be several commands that start with ec, then the user will 
be prompted to choose (using TAB or Shift+TAB to navigate, and ENTER for selection.)
Notable shortcuts include Ctrl+r to perform a reverse search, Ctrl+a and Ctrl+e to move to beginning and end of line respectively 
or Esc f and Esc b to move forward (resp. backward) one word at a time.

I recommend you have your **terminal in full screen mode** for the cards are displayed in ascii-art. Also, 
colors work in **Linux and Mac terminals** but not in Windows.

## Things not finished
- multiplayer interface could be improved: at the end of the game player 1 only has 3 seconds to see his score before 
player 2's scores appear
- robotplayer is not implemented: it could be done with several easy/hard/expert modes using blackjack strategies, but
it would have taken a couple more hours. I described how to do it in ModesShellComponent.java

## User/Technical constraints
- **Implement a card game**
-- done
- **Maintain a scoring system that persists over multiple game sessions**
-- depending on what we mean by "score" and "session" : the balance of the player is maintained between rounds of blackjack, 
but it's not saved when the game is stopped and restarted. For this I'd suggest saving the state of the GameContextComponent.java 
with ObjectOutputStream/Gzip and save the binary to a file in defined path such as /etc/intuit-blackjack/data/context.tmp. 
If the file has to be shared across several instances of the game and if user accounts managed, maybe save this file with something 
like google drive api. If we are very ambitious with the game, then to make the data easier to maintain, maybe serialize the context 
object in json and save it in a mongoDb...
- **Ability to play against the computer, or against other players**
-- The croupier is managed by the computer everytime. I also wanted to implement a robotplayer mode managing another player at 
the same table, and apply blackjack strategies like 
[https://wizardofodds.com/games/blackjack/strategy/4-decks/](https://wizardofodds.com/games/blackjack/strategy/4-decks/) 
which are quite easy to implement. We could also have easy/hard/expert modes depending on how much the strategy is used 
by the robot player.
- **Be extensible so that other card games could be implemented in the future, potentially dozens of different games**
-- the librairies intuit-cards-core and intuit-blackjack-core are full POJO libraires, so they can be used in any kind of framework 
or application.
- **Track each player/computer action within the system**
-- the framework does it by itself and creates a spring-shell.log file in the folder from which you'll run the jar file. 
This log file contains all the commands you'll execute inside of the application, with timestamps.
- **Capable of supporting interaction patterns other than a command-line interface**
-- pojo libraries contain methods callable from any kind of framework. We can imagine this library being embedded in a 
jhipster-react for instance to enable easy development of a dynamic monopage web application. Once this application is 
available on web page, it can be easily transformed to mobile app for instance with apache cordova.
- **When playing against the computer a difficulty setting of easy/medium/hard can be selected**
-- easily do-able once the robotplayer mode is implemented

## Approach, Choices made

> Blackjack’s what I know best across the other game examples but I
> don’t know it in details,  so I’ve installed a game app on my mobile
> to study the rules better.  Of course all the gaming won’t be counted
> in the 4 hours time! Obviously, the collection of features is very
> ambitious for a 4 hours work,  so the way I see it is this: I’m going
> to use my preferred language java to waste as least time as possible, 
> and create a base core module with a model of cards, deck, hand,
> points, the match with the croupier,  I’m not sure yet I can have
> absolutely all the rules including all money stuff like the insurance,
> the double-down,  the split etc. But I’ll try to have as much possible
> in the first version to make it a consistent set of game rules.  And
> then, I think I’ll reserve my last hour to make a module we can play
> in console,  I believe I’ll adapt something depending on how far I’ve
> been able to go during the 3 first hours.  I’ll create a README.md
> overtime, as well as I’ll take notes on my ideas to finish this thing
> up some other time. I’ll abide by the deadline, beginning of next
> week, if it’s okay for you.

**Post finem:** It took rather a 16-ish hours to do all, and ui took actually longer than implementing the rules of the game.
I would usually implement more test basing on jacoco reports, have continuous inspection and continuous integration, 
even for a small project like this one, I often use bettercode and travis. But, as I'm not pushing this code online, 
and as I'm in hurry to meet the deadlines, I tested only what I needed to debug during my development. At least, I have one 
spring-boot-test - powered integration test booting the whole context, and unit testing the ascii-art display of the cards.

## Test coverage Report

- [![Java coverage](https://gitlab.com/zg2pro/intuit-blackjack/badges/master/coverage.svg?job=pages)](https://zg2pro.gitlab.io/intuit-blackjack) Java

## Code quality (codeclimate defaults list):

- https://zg2pro.gitlab.io/intuit-blackjack/codeclimate.json
