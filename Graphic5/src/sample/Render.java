package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Render {
    Canvas canvas;
    Fractal fractal;

    double xmin, ymin, xmax, ymax;
    double maxX, maxY;

    Render(Canvas canvas, Fractal fractal){
        this.canvas = canvas;
        xmin=-1.5; ymin=-1; xmax=1.5; ymax=1;
        maxX = canvas.getWidth(); maxY = canvas.getHeight();
        this.fractal = fractal;
    }

    void draw() {
        Complex[] root = fractal.getRoot();
        double xinc=(xmax-xmin)/maxX, yinc=(ymax-ymin)/maxY;

        Color [] colors = new Color[6];
        colors[0]= Color.rgb(127,0,0);colors[1]=Color.rgb(255,0,0);
        colors[2]=Color.rgb(0,127,0);colors[3]=Color.rgb(0,255,0);
        colors[4]=Color.rgb(0,0,127);colors[5]=Color.rgb(0,0,255);

        double re, im;
        int ncolors = 0, j, n;
        Complex z;
        ncolors = 3;

        for (re=xmin; re<xmax; re+=xinc) {
            for (im = ymin; im < ymax; im += yinc) {
                z = new Complex(re, im);
                n = 0;
                do {
                    if (fractal.fs(z).abs() < 0.0001) n = -1;
                    else {
                        z = z.sub(fractal.f(z).div(fractal.fs(z)));
                        n++;
                    }
                } while (n >= 0 && n < 100 && fractal.f(z).abs() >= 0.0001);
                if (n < 0) continue;
                for (j = 0; j < ncolors; j++) {
                    if ((z.sub(root[j])).abs() < 0.0001)
                        putpoint(re, im, colors[2 * j + n % 2]);
                }
            }
        }
    }

    void putpoint (double x, double y, Color color)
    {
        GraphicsContext context = canvas.getGraphicsContext2D(); // и получаем GraphicContext
        PixelWriter pw = context.getPixelWriter();

        if(x<xmax&&x>xmin&&y<ymax&&y>ymin)
            pw.setColor(
                    (int) ((x-xmin)*maxX/(xmax-xmin)),
                    (int) ((ymax-y)*maxY/(ymax-ymin)),
                    color);
    }

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
    }
}
