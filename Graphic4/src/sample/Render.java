package sample;

import com.sun.javafx.geom.Matrix3f;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Render {
    Canvas canvas;
    int nx = 40;
    int ny = 40;
    float p = 3.7f;

    float xmax = p, xmin = -p, ymax = p, ymin = -p, zmax = 2 * p, zmin = 0;
    float exmax, exmin, eymax, eymin;
    int mx, my; Color line_color, pol_color;

    //-------------------------------------------------------------------------
    Render(Canvas canvas) {
        this.canvas = canvas;
        int i, j;
        float x, y, z;
        // Подготовка окна вывода
        // Инициализация графического режима
        /*hy = (ymax - ymin) / ny;
        hx = (xmax - xmin) / nx;
        gmex = (int) canvas.getWidth() - 1;
        gmey = (int) canvas.getHeight() - 1;*/
    }

//функция f по заданной точке на плоскости 0xy

//находит и возвращает координату z



    float f(float x,float y) {
        return (float) (1.2*Math.sin(Math.cos(x+y)-Math.cos(y-x)));
    }


//по данной точке в пространстве находим абсциссу

//точки на плоскости проекции

    float ex(float x,float y,float z) {return (float) (-0.2*x+0.4*y);}

//по данной точке в пространстве находим ординату

//точки на плоскости проекции

    float ey(float x,float y,float z) {
        return (float) (-0.1*x+0.2*z);
    }

//параметрами ф-ции являются координаты вершин четырехугольника

//в пространстве

//Ф-ция осуществляет проекцию этого четырехугольника

//на плоскость и вывод закрашенного четырехугольника на экран компьютера

    void vectfi(float x0, float y0, float z0,

                float x1, float y1, float z1,

                float x2, float y2, float z2,

                float x3, float y3, float z3) {

        float ex0, ey0, ex1, ey1, ex2, ey2, ex3, ey3;//координаты точек на плоскости

        //проекции

        double [] xx = new double[4];           //экранные координаты точек
        double [] yy = new double[4];           //экранные координаты точек

//проецирование

//точек на плоскость       //перевод в экранные координаты

        ex0 = ex(x0, y0, z0);         xx[0] = ((ex0 - exmin) * mx / (exmax - exmin));

        ey0 = ey(x0, y0, z0);         yy[0] = (my - (ey0 - eymin) * my / (eymax - eymin));

        ex1 = ex(x1, y1, z1);         xx[1] = ((ex1 - exmin) * mx / (exmax - exmin));

        ey1 = ey(x1, y1, z1);         yy[1] = (my - (ey1 - eymin) * my / (eymax - eymin));

        ex2 = ex(x2, y2, z2);         xx[2] = ((ex2 - exmin) * mx / (exmax - exmin));

        ey2 = ey(x2, y2, z2);         yy[2] = (my - (ey2 - eymin) * my / (eymax - eymin));

        ex3 = ex(x3, y3, z3);         xx[3] = ((ex3 - exmin) * mx / (exmax - exmin));

        ey3 = ey(x3, y3, z3);         yy[3] = (my - (ey3 - eymin) * my / (eymax - eymin));


        //setcolor(line_color);               //установка цвета линии
        //setfillstyle(SOLID_FILL, pol_color); //установка типа и цвета заливки

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(line_color);
        gc.setFill(pol_color);

        gc.fillPolygon(xx, yy,4);                 //рисование закрашенного четырехугольника
        gc.strokePolygon(xx, yy,4);                 //рисование закрашенного четырехугольника

    }

    void draw() {

        //int gd = DETECT, gm, i, j;

        float x1, y1, x2, y2, x3, y3, x4, y4, hx, hy;

        //initgraph( & gd,&gm, "..\BGI")//установка графического режима

        mx = (int) (canvas.getWidth() - 10);
        my = (int) (canvas.getHeight() - 80);

        //setcolor(RED);
        //setfillstyle(SOLID_FILL, WHITE);

        hy = (ymax - ymin) / ny;     //шаг по y

        hx = (xmax - xmin) / nx;     //шаг по x

        exmax = ex(xmin, ymax, zmax); //вычисляем наибольшие и

        exmin = ex(xmax, ymin, zmin); //наименьшие координаты на плоскости

        eymax = ey(xmin, ymin, zmax); //проекции

        eymin = ey(xmax, ymax, zmin);

        line_color = Color.BLUE;
        pol_color = Color.BLACK;

        //вывод трех граней объемлющего параллелепипеда

        vectfi(xmin, ymax, zmax, xmin, ymax, zmin, xmin, ymin, zmin, xmin, ymin, zmax);

        vectfi(xmin, ymin, zmin, xmin, ymin, zmax, xmax, ymin, zmax, xmax, ymin, zmin);

        vectfi(xmin, ymax, zmin, xmin, ymin, zmin, xmax, ymin, zmin, xmax, ymax, zmin);

        line_color = Color.WHITE;
        pol_color = Color.RED;

        for (int j = 0; j <= ny - 1; j++)        //в цикле последовательно вычисляются

        {                          //координаты вершин прямоугольников

            y1 = j * hy + ymin;          //на плоскости Oxy от дальних к ближним и

            y2 = j * hy + ymin;          //вызывается функция vectfi

            y3 = (j + 1) * hy + ymin;

            y4 = (j + 1) * hy + ymin;

            for (int i = 0; i <= nx - 1; i++) {

                x1 = i * hx + xmin;

                x2 = (i + 1) * hx + xmin;

                x3 = (i + 1) * hx + xmin;

                x4 = i * hx + xmin;

                //delay(1);            //задержка на 1 милисекунду

                vectfi(x1, y1, f(x1, y1), x2, y2, f(x2, y2), x3, y3, f(x3, y3), x4, y4, f(x4, y4));

            }

        }

        //getch();
        //closegraph();

    }


}
