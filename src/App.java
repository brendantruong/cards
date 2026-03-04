import processing.core.PApplet;

public class App extends PApplet {

    CardGame cardGame;

    CasinoButton easyBtn, medBtn, hardBtn;

    private int timer = 0;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    @Override
    public void settings() {
        size(1000, 800);
    }

    @Override
    public void setup() {
        textAlign(CENTER, CENTER);

        int bw = 220, bh = 60;
        int cx = width / 2 - bw / 2;

        easyBtn = new CasinoButton(cx, 310, bw, bh, "EASY");
        medBtn  = new CasinoButton(cx, 390, bw, bh, "MEDIUM");
        hardBtn = new CasinoButton(cx, 470, bw, bh, "HARD");
    }

    @Override
    public void draw() {
        background(10, 90, 45);

        noStroke();
        fill(0, 0, 0, 40);
        rect(0, 0, width, 120);
        rect(0, height - 120, width, 120);

        if (cardGame == null) {
            drawMenu();
            return;
        }

        drawCasinoFrame();

        cardGame.drawChoices(this);

        cardGame.playerOneHand.draw(this);
        cardGame.playerTwoHand.draw(this);

        fill(255, 215, 0);
        textSize(18);
        text("Current Player: " + cardGame.getCurrentPlayer(), width / 2, 35);

        textSize(16);
        text("Deck Size: " + cardGame.getDeckSize(), width / 2, height - 25);

        if (cardGame.getLastPlayedCard() != null) {
            cardGame.getLastPlayedCard().setPosition(width / 2 - 40, height / 2 - 60, 80, 120);
            cardGame.getLastPlayedCard().draw(this);
        }

        if (cardGame.getCurrentPlayer().equals("Player Two")) {
            fill(255);
            textSize(16);
            text("Computer is thinking...", width / 2, height / 2 + 95);
            timer++;
            if (timer >= 75) {
                cardGame.handleComputerTurn();
                timer = 0;
            }
        }
    }

    private void drawMenu() {
        fill(255, 215, 0);
        textSize(44);
        text("SNAPS!", width / 2, 160);

        fill(255);
        textSize(16);
        text("Choose difficulty (harder = computer snaps faster)", width / 2, 220);

        easyBtn.draw(this);
        medBtn.draw(this);
        hardBtn.draw(this);

        fill(255, 255, 255, 170);
        textSize(14);
        text("Goal: Flip cards. If the last two match, SNAP before the computer.\nSnap wrong -> you lose.",
                width / 2, 570);
    }

    private void drawCasinoFrame() {
        noFill();
        stroke(255, 215, 0);
        strokeWeight(6);
        rect(12, 12, width - 24, height - 24, 14);

        stroke(255, 215, 0, 110);
        strokeWeight(2);
        rect(22, 22, width - 44, height - 44, 12);

        noStroke();
        fill(255, 215, 0, 35);
        ellipse(width / 2f, height / 2f, 520, 320);
    }

    @Override
    public void mousePressed() {
        if (cardGame == null) {
            if (easyBtn.isClicked(mouseX, mouseY)) {
                cardGame = new Snaps(Snaps.Difficulty.EASY);
            } else if (medBtn.isClicked(mouseX, mouseY)) {
                cardGame = new Snaps(Snaps.Difficulty.MEDIUM);
            } else if (hardBtn.isClicked(mouseX, mouseY)) {
                cardGame = new Snaps(Snaps.Difficulty.HARD);
            }
            return;
        }

        cardGame.handleDrawButtonClick(mouseX, mouseY);
        cardGame.handleCardClick(mouseX, mouseY);

        if (cardGame instanceof Snaps) {
            ((Snaps) cardGame).handleSnapButtonClick(mouseX, mouseY);
        }
    }
}