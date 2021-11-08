package sample;

import javafx.scene.canvas.Canvas;

public interface Surface {


    public abstract void draw(Canvas canvas);

    public void draw(Canvas canvas, float k);
}
