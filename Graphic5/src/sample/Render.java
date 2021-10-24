package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

// example
    /*Complex f(Complex z) {
        return z.mul(z).mul(z).sub(new Complex(1));
    }

    Complex fs(Complex z) {
        return z.mul(z).mul(new Complex(3));
    }*/

public class Render {
    Canvas canvas;

    Render(Canvas canvas){
        this.canvas = canvas;
    }

    Complex[] root = new Complex[3];



    void draw() {
        double xmin=-1.5, ymin=-1, xmax=1.5, ymax=1;
        double maxX = canvas.getWidth(), maxY = canvas.getHeight();
        double xinc=(xmax-xmin)/maxX, yinc=(ymax-ymin)/maxY;

        Color [] colors = new Color[6];
        colors[0]= Color.rgb(127,0,0);colors[1]=Color.rgb(255,0,0);
        colors[2]=Color.rgb(0,127,0);colors[3]=Color.rgb(0,255,0);
        colors[4]=Color.rgb(0,0,127);colors[5]=Color.rgb(0,0,255);

        double re, im;
        int ncolors = 0, j, n;
        Complex z;
        ncolors = 3;
        // example root
/*        root = new Complex[] {
                new Complex(1),
                new Complex(-0.5, Math.sqrt(3.) / 2),
                new Complex(-0.5, -Math.sqrt(3.) / 2)
        };*/

        // f(z)  = z^3 + 5*z^2/2 + 3*z + 1
        // f'(z) = 3*z^2 + 5*z + 3
/*        root = new Complex[] {
                new Complex(-0.5),
                new Complex(-1, 1),
                new Complex(-1, -1)
        };*/

/*        //  f(z)  = z^3 + 7*z^2/4 + 2*z  + 15/16
        //  f'(z) = 3*z^2 + 7*z/2 + 2
        root = new Complex[] {
                new Complex(-0.75),
                new Complex(-0.5, 1),
                new Complex(-0.5, -1)
        };*/

        for (re=xmin; re<xmax; re+=xinc) {
            for (im = ymin; im < ymax; im += yinc) {
                z = new Complex(re, im);
                n = 0;
                do {
                    if (fs(z).abs() < 0.0001) n = -1;
                    else {
                        z = z.sub(f(z).div(fs(z)));
                        n++;
                    }
                } while (n >= 0 && n < 100 && f(z).abs() >= 0.0001);
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
        double xmin=-1.5, ymin=-1, xmax=1.5, ymax=1;
        double maxX = canvas.getWidth(), maxY = canvas.getHeight();
        GraphicsContext context = canvas.getGraphicsContext2D(); // и получаем GraphicContext
        PixelWriter pw = context.getPixelWriter();

        if(x<xmax&&x>xmin&&y<ymax&&y>ymin)
            pw.setColor(
                    (int) ((x-xmin)*maxX/(xmax-xmin)),
                    (int) ((ymax-y)*maxY/(ymax-ymin)),
                    color);
    }

}
