import processing.core.PApplet;

public class CasinoButton extends Button {

    public CasinoButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    @Override
    public void draw(PApplet app) {
        app.pushStyle();

      
        app.noStroke();
        app.fill(0, 0, 0, 80);
        app.rect(x + 4, y + 4, width, height, 12);

  
        if (isDisabled) {
            app.fill(120);
        } else {
            app.fill(20, 20, 20); 
        }
        app.rect(x, y, width, height, 12);

        
        if (!isDisabled) app.stroke(255, 215, 0);
        else app.stroke(200);
        app.strokeWeight(3);
        app.noFill();
        app.rect(x, y, width, height, 12);

        
        app.fill(255, 215, 0);
        if (isDisabled) app.fill(220);

        app.textAlign(app.CENTER, app.CENTER);
        app.textSize(18);
        app.text(text, x + width / 2f, y + height / 2f);

        app.popStyle();
    }
}
