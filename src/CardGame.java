import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import processing.core.PApplet;

public class CardGame {
    ArrayList<Card> deck = new ArrayList<>();
    Hand playerOneHand;
    Hand playerTwoHand;
    ArrayList<Card> discardPile = new ArrayList<>();
    Card selectedCard;
    int selectedCardRaiseAmount = 15;

    boolean playerOneTurn = true;
    Card lastPlayedCard;
    boolean gameActive;

    ClickableRectangle drawButton;
    int drawButtonX = 250;
    int drawButtonY = 400;
    int drawButtonWidth = 100;
    int drawButtonHeight = 35;

    public CardGame() {
        initializeGame();
        dealCards(6);
    }

    protected void initializeGame() {
        drawButton = new ClickableRectangle();
        drawButton.x = drawButtonX;
        drawButton.y = drawButtonY;
        drawButton.width = drawButtonWidth;
        drawButton.height = drawButtonHeight;
        drawButton.text = "Draw";

        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        playerOneHand = new Hand();
        playerTwoHand = new Hand();
        gameActive = true;

        createDeck();
    }

    protected void createDeck() {
        String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
        String[] values = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(value, suit));
            }
        }
        Collections.shuffle(deck, new Random());
    }

    protected void dealCards(int numCards) {
        Collections.shuffle(deck);

        for (int i = 0; i < numCards; i++) {
            playerOneHand.addCard(deck.remove(0));
            Card card = deck.remove(0);
            card.setTurned(true);
            playerTwoHand.addCard(card);
        }

        playerOneHand.positionCards(50, 450, 80, 120, 20);
        playerTwoHand.positionCards(50, 50, 80, 120, 20);
    }

    protected boolean isValidPlay(Card card) {
        return true;
    }

    public void drawCard(Hand hand) {
        if (deck != null && !deck.isEmpty()) {
            hand.addCard(deck.remove(0));
        } else if (discardPile != null && discardPile.size() > 1) {
            lastPlayedCard = discardPile.remove(discardPile.size() - 1);
            deck.addAll(discardPile);
            discardPile.clear();
            discardPile.add(lastPlayedCard);
            Collections.shuffle(deck);

            if (!deck.isEmpty()) {
                hand.addCard(deck.remove(0));
            }
        }
    }

    public void handleDrawButtonClick(int mouseX, int mouseY) {
        if (drawButton.isClicked(mouseX, mouseY) && playerOneTurn) {
            drawCard(playerOneHand);
            switchTurns();
        }
    }

    public boolean playCard(Card card, Hand hand) {
        if (!isValidPlay(card)) {
            System.out.println("Invalid play: " + card.value + " of " + card.suit);
            return false;
        }

        hand.removeCard(card);
        card.setTurned(false);

        discardPile.add(card);
        lastPlayedCard = card;

        switchTurns();
        return true;
    }

    public void switchTurns() {
        playerOneTurn = !playerOneTurn;
        playerOneHand.positionCards(50, 560, 80, 120, 20);
        playerTwoHand.positionCards(50, 120, 80, 120, 20);
    }

    public String getCurrentPlayer() {
        return playerOneTurn ? "Player One" : "Player Two";
    }

    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

    public int getDeckSize() {
        return deck != null ? deck.size() : 0;
    }

    public Hand getPlayerOneHand() {
        return playerOneHand;
    }

    public Hand getPlayerTwoHand() {
        return playerTwoHand;
    }

    public void handleComputerTurn() {
        drawCard(playerTwoHand);
        switchTurns();
    }

    public void handleCardClick(int mouseX, int mouseY) {
        if (!playerOneTurn) return;

        Card clickedCard = getClickedCard(mouseX, mouseY);
        if (clickedCard == null) return;

        if (selectedCard == null) {
            selectedCard = clickedCard;
            selectedCard.setSelected(true, selectedCardRaiseAmount);
            return;
        }

        if (selectedCard == clickedCard) {
            if (playCard(selectedCard, playerOneHand)) {
                selectedCard.setSelected(false, selectedCardRaiseAmount);
                selectedCard = null;
            }
            return;
        }

        selectedCard.setSelected(false, selectedCardRaiseAmount);
        selectedCard = clickedCard;
        selectedCard.setSelected(true, selectedCardRaiseAmount);
    }

    public Card getClickedCard(int mouseX, int mouseY) {
        for (int i = playerOneHand.getSize() - 1; i >= 0; i--) {
            Card card = playerOneHand.getCard(i);
            if (card != null && card.isClicked(mouseX, mouseY)) return card;
        }
        return null;
    }

    public void drawChoices(PApplet app) {
    }
}
