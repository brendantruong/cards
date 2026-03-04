import processing.core.PApplet;

public class Snaps extends CardGame {

    public enum Difficulty { EASY, MEDIUM, HARD }

    private CasinoButton flipButton;
    private CasinoButton snapButton;

    private Card prevFlip = null;
    private Card currFlip = null;

    private boolean matchActive = false;
    private int matchStartMs = 0;
    private int snapWindowMs = 0;
    private boolean gameOver = false;
    private String endMessage = "";

    private int flipX, flipY, flipW, flipH;
    private int snapX, snapY, snapW, snapH;

    public Snaps(Difficulty diff) {
        super();

        for (Card c : playerTwoHand.getCards()) c.setTurned(false);

        if (diff == Difficulty.EASY)   snapWindowMs = 1400;
        if (diff == Difficulty.MEDIUM) snapWindowMs = 900;
        if (diff == Difficulty.HARD)   snapWindowMs = 550;

        flipW = 160; flipH = 50;
        snapW = 160; snapH = 50;

        flipX = 70;
        flipY = 360;

        snapX = flipX;
        snapY = flipY + flipH + 18;

        flipButton = new CasinoButton(flipX, flipY, flipW, flipH, "FLIP");
        snapButton = new CasinoButton(snapX, snapY, snapW, snapH, "SNAP!");
    }

    @Override
    protected void dealCards(int numCards) {
        playerOneHand.positionCards(70, 560, 80, 120, 20);
        playerTwoHand.positionCards(70, 120, 80, 120, 20);
    }

    @Override
    public void handleDrawButtonClick(int mouseX, int mouseY) {
        if (gameOver) return;
        if (!getCurrentPlayer().equals("Player One")) return;

        if (flipButton.isClicked(mouseX, mouseY)) {
            flipForCurrentPlayer();
        }
    }

    @Override
    public void handleComputerTurn() {
        if (gameOver) return;
        if (!getCurrentPlayer().equals("Player Two")) return;

        flipForCurrentPlayer();
    }

    private void flipForCurrentPlayer() {
        if (deck.isEmpty()) {
            gameOver = true;
            endMessage = "Deck empty — no snap!";
            return;
        }

        Card flipped = deck.remove(0);
        flipped.setTurned(false);

        discardPile.add(flipped);
        lastPlayedCard = flipped;

        prevFlip = currFlip;
        currFlip = flipped;

        if (prevFlip != null && prevFlip.value.equals(currFlip.value)) {
            matchActive = true;
            matchStartMs = 0;
        } else {
            matchActive = false;
            matchStartMs = 0;
        }

        switchTurns();
    }

    public void handleSnapButtonClick(int mouseX, int mouseY) {
        if (gameOver) return;
        if (!snapButton.isClicked(mouseX, mouseY)) return;

        boolean isMatchNow = (prevFlip != null && currFlip != null && prevFlip.value.equals(currFlip.value));

        if (isMatchNow) {
            gameOver = true;
            endMessage = "YOU SNAPPED! You win 🟡";
        } else {
            gameOver = true;
            endMessage = "Bad snap! You lose 🔴";
        }
    }

    @Override
    public void drawChoices(PApplet app) {
        int now = app.millis();

        if (matchActive && matchStartMs == 0) {
            matchStartMs = now;
        }

        if (!gameOver && matchActive && matchStartMs != 0) {
            int elapsed = now - matchStartMs;
            int remaining = snapWindowMs - elapsed;

            if (remaining <= 0) {
                gameOver = true;
                endMessage = "COMPUTER SNAPPED FIRST! You lose 🟣";
            }
        }

        flipButton.setDisabled(gameOver || !getCurrentPlayer().equals("Player One"));
        flipButton.draw(app);

        snapButton.setDisabled(gameOver);
        snapButton.draw(app);

        app.textAlign(app.CENTER, app.CENTER);

        if (!gameOver && matchActive && matchStartMs != 0) {
            int elapsed = now - matchStartMs;
            float pct = 1.0f - (elapsed / (float) snapWindowMs);
            if (pct < 0) pct = 0;

            app.noStroke();
            app.fill(0, 0, 0, 120);
            app.rect(app.width / 2f - 160, app.height - 95, 320, 14, 6);

            app.fill(255, 215, 0);
            app.rect(app.width / 2f - 160, app.height - 95, 320 * pct, 14, 6);
        }

        if (gameOver) {
            app.noStroke();
            app.fill(0, 0, 0, 170);
            app.rect(0, 0, app.width, app.height);

            app.fill(255, 215, 0);
            app.textSize(34);
            app.text(endMessage, app.width / 2f, app.height / 2f - 10);

            app.fill(255);
            app.textSize(16);
            app.text("Restart by re-running the program.", app.width / 2f, app.height / 2f + 35);
        }
    }
}