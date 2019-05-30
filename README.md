
# command-line Blackjack Exercise

[![build status](https://gitlab.com/zg2pro/command-line-blackjack/badges/master/build.svg)](https://gitlab.com/zg2pro/command-line-blackjack/commits/master)

## Prerequisites
JDK >= 8 installed - JAVA_HOME - location of a JDK home dir

## Build
**In Windows**: Run: ./mvnw.cmd clean install
**In Linux:** Run: ./mvnw clean install
(Because I don't know on what machines the project will be built I've added maven wrapper files, also I've taken java 8 as a base)

## Run
From the project root, run: 
*java -jar command-line-blackjack-shell/target/command-line-blackjack-shell-1.0-SNAPSHOT.jar* 

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


## Test coverage Report

- [![Java coverage](https://gitlab.com/zg2pro/command-line-blackjack/badges/master/coverage.svg?job=pages)](https://zg2pro.gitlab.io/command-line-blackjack) Java

## Code quality (codeclimate defaults list):

- https://zg2pro.gitlab.io/command-line-blackjack/codeclimate.json
