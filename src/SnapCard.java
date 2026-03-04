import processing.core.PApplet;

public class SnapCard extends Card {

    SnapCard(String value, String suit) {
        super(value, suit);
    }

    @Override
    public void draw(PApplet sketch) {
        if (turned) {
            sketch.fill(180);
            sketch.rect(x, y, width, height);
            sketch.fill(0);
            sketch.textAlign(sketch.CENTER, sketch.CENTER);
            sketch.text("BACK", x + width / 2, y + height / 2);
            return;
        }

        sketch.fill(255);
        sketch.stroke(0);
        sketch.rect(x, y, width, height);
        sketch.fill(0);
        sketch.textAlign(sketch.LEFT, sketch.TOP);
        sketch.text(value + " " + suit, x + 6, y + 6);
    }
}