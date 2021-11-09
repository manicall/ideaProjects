package sample;

import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.LinkedList;

import static java.lang.Math.pow;

public class SurfaceTask3 implements Surface {
    Canvas canvas;
    float p = 1f; // диапазон отрисовки поверхности
    float k = 1f; // коэфициент учавствующий в вычислении f(x,y) = z;
    float xmax = p, xmin = -p, ymax = p, ymin = -p, zmax = 2f * p, zmin = 0f;
    Color line_color, pol_color;

    SurfaceTask3() {   }

    //функция f по заданной точке на плоскости 0xy
    //находит и возвращает координату z
    float f(float x, float y) {
        return (float) Math.exp(-pow(k, 2) * (pow(x, 2) + pow(y, 2)));
    }
    //по данной точке в пространстве находим абсциссу
    //точки на плоскости проекции
    float ex(Point3D point) {
        return (float) (-0.2 * point.getX() + 0.4 * point.getY());
    }
    //по данной точке в пространстве находим ординату
    //точки на плоскости проекции
    float ey(Point3D point) {
        return (float) (-0.1 * point.getX() + 0.2 * point.getZ());
    }
    //параметрами фунции являются координаты вершин четырехугольника
    //в пространстве
    //Функция осуществляет проекцию этого четырехугольника
    //на плоскость и вывод закрашенного четырехугольника на экран компьютера
    void vectfi(Point3D point0, Point3D point1, Point3D point2, Point3D point3) {
        //проекции
        double[] xx = new double[4];           //экранные координаты точек
        double[] yy = new double[4];           //экранные координаты точек
        LinkedList<Point3D> points3D =
                new LinkedList<Point3D>(Arrays.asList(point0, point1, point2, point3));

        float exmax = ex(new Point3D(xmin, ymax, zmax)); //вычисляем наибольшие и
        float exmin = ex(new Point3D(xmax, ymin, zmin)); //наименьшие координаты на плоскости
        float eymax = ey(new Point3D(xmin, ymin, zmax)); //проекции
        float eymin = ey(new Point3D(xmax, ymax, zmin));

        int mx = (int) (canvas.getWidth());
        int my = (int) (canvas.getHeight());

        //проецирование
        //точек на плоскость
        // перевод в экранные координаты
        int i = 0;
        for (Point3D point : points3D){
            xx[i] = ((ex(point) - exmin) * mx / (exmax - exmin));
            yy[i] = (my - (ey(point) - eymin) * my / (eymax - eymin));
            i++;
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(line_color);
        gc.setFill(pol_color);

        gc.fillPolygon(xx, yy, 4);   //рисование закрашенного четырехугольника
        gc.strokePolygon(xx, yy, 4); //рисование закрашенного четырехугольника
    }

    public void draw(Canvas canvas, float k) {
        this.k = k;
        draw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());

        createParallelepipedBorders();
        createSurface();
    }
    //вывод трех граней объемлющего параллелепипеда
    void createParallelepipedBorders() {
        line_color = Color.BLUE;
        pol_color = Color.BLACK;

        Point3D pointMin = new Point3D(xmin, ymin, zmin);
        Point3D point0 = new Point3D(xmin, ymax, zmin);
        Point3D point1 = new Point3D(xmin, ymin, zmax);
        Point3D point2 = new Point3D(xmax, ymin, zmin);

        vectfi(new Point3D(xmin, ymax, zmax), point0, pointMin, point1);
        vectfi(pointMin, point1, new Point3D(xmax, ymin, zmax), point2);
        vectfi(point0, pointMin, point2, new Point3D(xmax, ymax, zmin));
    }
    float hx, hy;
    void createSurface(){
        line_color = Color.WHITE;
        pol_color = Color.RED;
        final int nx = 100;
        final int ny = 100;

        hy = (ymax - ymin) / ny; //шаг по y
        hx = (xmax - xmin) / nx; //шаг по x

        //в цикле последовательно вычисляются
        //координаты вершин прямоугольников
        //на плоскости Oxy от дальних к ближним и
        //вызывается функция vectfi
        for (int j = 0; j <= ny - 1; j++)
            for (int i = 0; i <= nx - 1; i++) {
                int i1 = i + 1;
                int j1 = j + 1;
                vectfi(new Point3D(x(i),   y(j),  f(x(i),  y(j))),
                        new Point3D(x(i1), y(j),  f(x(i1), y(j))),
                        new Point3D(x(i1), y(j1), f(x(i1), y(j1))),
                        new Point3D(x(i),  y(j1), f(x(i),  y(j1))));
            }
    }
    float y(int j){
        return j * hy + ymin;
    }
    float x(int i){
        return i * hx + xmin;
    }
}
