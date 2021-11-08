package sample;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static java.lang.Math.pow;

public class SurfaceExample2 implements Surface {
    Canvas canvas;
    int nx = 60;
    int ny = 60;

    float xmax = 10, xmin = -10, ymax = 10, ymin = -10,  zmax, zmin,hx,hy;
    float exmax, exmin, eymax, eymin ;
    int gmex, gmey ;
    //-------------------------------------------------------------------------
    SurfaceExample2()
    {
        int i, j;
        float x,y,z;
        // Подготовка окна вывода
        // Инициализация графического режима
        hy=(ymax-ymin)/ny;
        hx=(xmax-xmin)/nx;
    }
    // Функция z=f(x,y)
    float fz(float x, float y)
    {
        return  (float) (7*Math.sin(x)*Math.sin(y));
    }
    // Параллельная проекция
    // x координата на плоскости проекциии
    float ex(float x, float y, float z)
    {
        return (float) (y-0.7*x);
    }
    // y координата на плоскости проекции
    float ey ( float x, float y, float z )
    {
        return (float) (z-0.7*x);
    }
    // Вычисление координат полигона
    void fpoly(float [] x, float [] y, float [] z, int n)
    {
        float px,py;
        int []ix= new int[n];
        int []iy= new int[n];

        ArrayList<Point2D> p = new ArrayList<Point2D>();

        for (int i=0; i<n; i++)
        {
            px=ex(x[i],y[i],z[i]);
            ix[i]= (int) ((px-exmin)*gmex/(exmax-exmin));
            py=ey(x[i],y[i],z[i]);
            iy[i]= (int) ((py-eymin)*gmey/(eymax-eymin));
            p.add(new Point2D(ix[i], gmey-iy[i]));
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();


        double [] xx = new double[n];
        double [] yy = new double[n];

        for (int i = 0; i < n; i++) {
            xx[i] = p.get(i).getX();
        }
        for (int i = 0; i < n; i++) {
            yy[i] = p.get(i).getY();
        }

        gc.setStroke(Color.WHITE);
        gc.fillPolygon(xx, yy, n);
        gc.strokePolygon(xx, yy, n);
    }
    //-------------------------------------------------------------------------
    @Override
    public void draw(Canvas canvas)
    {
        this.canvas = canvas;

        gmex= (int) canvas.getWidth();
        gmey= (int) canvas.getHeight();

        int i,j; float x, y, z;
        float [] xx = new float[4];
        float [] yy = new float[4];
        float [] zz = new float[4];

        zmin=zmax=0f;
        for (i=0; i<=nx; i++)
            for (j=0; j<=ny; j++)
            {
                z=fz(xmin+i*hx,ymin+j*hy);
                if (z>zmax) zmax=z;
                if (z<zmin) zmin=z;
            }
        exmax=ex(xmin, ymax, zmax);
        exmin=ex(xmax, ymin, zmin);
        eymax=ey(xmin, ymax, zmax);
        eymin=ey(xmax, ymin, zmin);
        for(x=xmin; x<=xmax; x+=xmax-xmin)
            for(y=ymin; y<=ymax; y+=ymax-ymin)
                for(z=zmin; z<=zmax; z+=zmax-zmin)
                {
                    if (exmax<ex(x,y,z)) exmax=ex(x,y,z);
                    if (exmin>ex(x,y,z)) exmin=ex(x,y,z);
                    if (eymax<ey(x,y,z)) eymax=ey(x,y,z);
                    if (eymin>ey(x,y,z)) eymin=ey(x,y,z);
                }
        for (i=0; i<nx; i++)
        {
            xx[0]=xmin+i*hx; xx[1]=xmin+(i+1)*hx;
            xx[2]=xmin+(i+1)*hx; xx[3]=xmin+i*hx;
            for (j=0; j<ny; j++)
            {
                yy[0]=ymin+j*hy; zz[0]=fz(xx[0],yy[0]);
                yy[1]=ymin+j*hy; zz[1]=fz(xx[1],yy[1]);
                yy[2]=ymin+(j+1)*hy; zz[2]=fz(xx[2],yy[2]);
                yy[3]=ymin+(j+1)*hy; zz[3]=fz(xx[3],yy[3]);
                fpoly(xx,yy,zz,4);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, float k) {

    }
}
