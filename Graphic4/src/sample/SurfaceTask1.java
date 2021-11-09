package sample;

import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.LinkedList;

import static java.lang.Math.sin;

public class SurfaceTask1 implements Surface {
    private Canvas canvas;
    int nx = 60;
    int ny = 60;
    float p = 3; // диапазон отрисовки поверхности
    float xmax = p, xmin = -p, ymax = p, ymin = -p, zmax, zmin;
    float xv = 50f, yv = 50f, zv = 40f; // положение наблюдателя

    float exmax, exmin, eymax, eymin;
    int gmex, gmey;
    int[] phimin;
    int[] phimax;

    float cosa, sina; // меридиана точки наблюдения
    public SurfaceTask1() {
        cosa = (float) (xv / Math.sqrt(xv * xv + yv * yv));
        sina = (float) (yv / Math.sqrt(xv * xv + yv * yv));
    }

    // Генерация точек отрезка по алгоритму простого ЦДА
    void sline(int x0, int y0, int x1, int y1) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        int i, j, temp, x, y, maxacc, accx, accy, dx, dy, length = Math.abs(x1 - x0);
        if ((i = Math.abs(y1 - y0)) > length) length = i;

        maxacc = 2 * length;
        accx = accy = length;
        for (j = 0; j < 2; j++) {
            dx = 2 * (x1 - x0);
            dy = 2 * (y1 - y0);
            x = x0;
            y = y0;
            length += j;
            for (i = 0; i < length; i++) {
                if (y0 <= y1 && phimax[x] < y) {
                    pw.setColor(x, gmey - y, Color.WHITE);
                    phimax[x] = y;
                }
                if (y0 >= y1 && phimin[x] > y) {
                    pw.setColor(x, gmey - y, Color.WHITE);
                    phimin[x] = y;
                }
                accx += dx;
                accy += dy;
                if (accx >= maxacc) {
                    accx -= maxacc;
                    x++;
                } else if (accx < 0) {
                    accx += maxacc;
                    x--;
                }
                if (accy >= maxacc) {
                    accy -= maxacc;
                    y++;
                } else if (accy < 0) {
                    accy += maxacc;
                    y--;
                }
            }
            temp = x0;
            x0 = x1;
            x1 = temp;
            temp = y0;
            y0 = y1;
            y1 = temp;
        }
    }

    // Функция z=f(x,y)
    float fz(float x, float y) {
        return (float) (x * sin(y) - y * sin(x));
    }

    // Центральная проекция
    // x координата на плоскости проекциии
    float ex(Point3D point) {
        return (float) ((-(-(point.getX() - xv) * sina + (point.getY() - yv) * cosa)) /
                ((point.getX() - xv) * cosa + (point.getY() - yv) * sina));
    }
    // y координата на плоскости проекции
    float ey(Point3D point) {
        return (float) ((-(point.getZ() - zv)) /
                ((point.getX() - xv) * cosa + (point.getY() - yv) * sina));
    }

    // Вычисление экранных координат
    void vectphi(Point3D point0, Point3D point1) {
        LinkedList<Point3D> points3D = new LinkedList<Point3D>(Arrays.asList(point0, point1));
        int[] ix = new int[2];
        int[] iy = new int[2];

        int i = 0;
        for (Point3D point : points3D) {
            ix[i] = (int) ((ex(point) - exmin) * gmex / (exmax - exmin));
            iy[i] = (int) ((ey(point) - eymin) * gmey / (eymax - eymin));
            i++;
        }
        // выводим изображение с удалением невидимых линий
        sline(ix[0], iy[0], ix[1], iy[1]);
    }

    void vectfi(Point3D point0, Point3D point1, Point3D point2, Point3D point3) {
        //проекции
        double[] xx = new double[4];           //экранные координаты точек
        double[] yy = new double[4];           //экранные координаты точек

        int mx = (int) (canvas.getWidth());
        int my = (int) (canvas.getHeight());

        LinkedList<Point3D> points3D =
                new LinkedList<Point3D>(Arrays.asList(point0, point1, point2, point3));
        //проецирование точек на плоскость перевод в экранные координаты
        int i = 0;
        for (Point3D point : points3D) {
            xx[i] = ((ex(point) - exmin) * mx / (exmax - exmin));
            yy[i] = (my - (ey(point) - eymin) * my / (eymax - eymin));
            i++;
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLACK);

        gc.fillPolygon(xx, yy, 4);   //рисование закрашенного четырехугольника
        gc.strokePolygon(xx, yy, 4); //рисование закрашенного четырехугольника
    }

    float hx, hy;
    public void draw(Canvas canvas) {
        this.canvas = canvas;

        hy = (ymax - ymin) / ny;
        hx = (xmax - xmin) / nx;

        gmex = (int) canvas.getWidth();
        gmey = (int) canvas.getHeight();

        for (int i = 0; i <= nx; i++)
            for (int j = 0; j <= ny; j++) {
                float z = fz(xmin + i * hx, ymin + j * hy);
                if (z > zmax) zmax = z;
                if (z < zmin) zmin = z;
            }

        exmax = ex(new Point3D(xmin, ymax, zmax));
        exmin = ex(new Point3D(xmax, ymin, zmin));
        eymax = ey(new Point3D(xmin, ymin, zmax));
        eymin = ey(new Point3D(xmax, ymax, zmin));

        phimax = new int[gmex + 1];
        phimin = new int[gmex + 1];
        for (int i = 0; i < gmex; i++) {
            phimax[i] = 0;
            phimin[i] = gmey;
        }

        //вывод трех граней объемлющего параллелепипеда
        createParallelepipedBorders();

        createFirstRow();
        createOthersRows();

    }

    @Override
    public void draw(Canvas canvas, float k) {

    }

    float[] cvals = new float[ny + 1];
    // Заполнение первой строки
    void createFirstRow() {
        float cval = fz(xmax, ymax);
        for (int i = ny - 1; i >= 0; i--) {
            cvals[i + 1] = cval;
            cval = fz(xmax, y(i));
            vectphi(new Point3D(xmax, y(i) + hy, cvals[i + 1]),
                    new Point3D(xmax, y(i), cval));
        }
        cvals[0] = cval;

    }

    // Заполнение остальных строк
    void createOthersRows() {
        for (int j = nx - 1; j >= 0; j--) {
            float cval = fz(x(j), ymax);
            vectphi(new Point3D(x(j) + hx, ymax, cvals[ny]),
                    new Point3D(x(j), ymax, cval));

            for (int i = ny - 1; i >= 0; i--) {
                cvals[i + 1] = cval;
                cval = fz(x(j), y(i));
                vectphi(new Point3D(x(j) + hx, y(i), cvals[i]),
                        new Point3D(x(j), y(i), cval));
                vectphi(new Point3D(x(j), y(i) + hy, cvals[i + 1]),
                        new Point3D(x(j), y(i), cval));
            }
            cvals[0] = cval;
        }
    }

    float y(int i){
        return ymin + hy * i;
    }
    float x(int j){
        return xmin + hx * j;
    }

    //вывод трех граней объемлющего параллелепипеда
    void createParallelepipedBorders() {
        Point3D pointMin = new Point3D(xmin, ymin, zmin);
        Point3D point0 = new Point3D(xmin, ymax, zmin);
        Point3D point1 = new Point3D(xmin, ymin, zmax);
        Point3D point2 = new Point3D(xmax, ymin, zmin);

        vectfi(new Point3D(xmin, ymax, zmax), point0, pointMin, point1);
        vectfi(pointMin, point1, new Point3D(xmax, ymin, zmax), point2);
        vectfi(point0, pointMin, point2, new Point3D(xmax, ymax, zmin));
    }
}
