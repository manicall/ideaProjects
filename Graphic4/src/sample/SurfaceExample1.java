package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.awt.*;

public class SurfaceExample1 implements Surface{
    int nx =50;
    int ny =50;

    float xmax = 3, xmin = -3, ymax = 3, ymin = -3,  zmax, zmin,hx,hy;

    float xv = 5f, yv = 3.5f, zv = 3f ; // положение наблюдателя

    float d = 10 ;            // расстояние до плоскости проекции
    float cosa, sina ;         // меридиана точки наблюдения
    float exmax, exmin, eymax, eymin ;
    int gmex, gmey ;
    int [] phimin;
    int [] phimax;
    float [] cvals = new float [ny+1];
    float cval;

    private Canvas canvas;


    public SurfaceExample1(){
        hy=(ymax-ymin)/ny;
        hx=(xmax-xmin)/nx;

        cosa= (float) (xv/Math.sqrt(xv*xv+yv*yv));
        sina= (float) (yv/Math.sqrt(xv*xv+yv*yv));
    }

    void line(int x0, int y0, int x1, int y1)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.moveTo(x0,y0);
        gc.lineTo(x1,y1);
    }
    // Генерация точек отрезка по алгоритму простого ЦДА
    void sline( int x0, int y0, int x1, int y1 )
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        int i,j, temp, x, y, maxacc, accx, accy, dx, dy,
                length=Math.abs(x1-x0);
        if ((i=Math.abs(y1-y0))>length) length=i;
//     if (length==0) return;
        maxacc=2*length; accx=accy=length;
        for (j=0; j<2; j++)
        {
            dx=2*(x1-x0); dy=2*(y1-y0); x=x0; y=y0;
            length+=j;
            for (i=0; i<length; i++)
            {
                if (y0<=y1&&phimax[x]<y)
                {
                    pw.setColor(x, gmey-y, Color.BLACK);
                    phimax[x]=y;
                }
                if (y0>=y1&&phimin[x]>y)
                {
                    pw.setColor(x, gmey-y, Color.BLACK);
                    phimin[x]=y;
                }
                accx+=dx; accy+=dy;
                if (accx>=maxacc)
                {
                    accx-=maxacc; x++;
                } else if (accx<0)
                {
                    accx+=maxacc; x--;
                }
                if (accy>=maxacc)
                {
                    accy-=maxacc; y++;
                } else if (accy<0)
                {
                    accy+=maxacc; y--;
                }
            }
            temp=x0; x0=x1; x1=temp; temp=y0; y0=y1; y1=temp;
        }
    }
    // Функция z=f(x,y)
    float fz(float x, float y) {return (float) Math.sin(x*x+y*y);}
    //float fz(float x, float y) {return (x*x*y-x*x*x)*0.1;}
    // Центральная проекция
    // x координата на плоскости проекциии
    float ex(float x, float y, float z)
    {
        return (-d*(-(x-xv)*sina+(y-yv)*cosa))/((x-xv)*cosa+(y-yv)*sina);
    }
    // y координата на плоскости проекции
    float ey ( float x, float y, float z )
    {
        return (-d*(z-zv))/((x-xv)*cosa+(y-yv)*sina);
    }
    // Вычисление экранных координат
    void vectphi(float x0, float y0, float z0,
                 float x1, float y1, float z1,int mm)
    {
        float ex0, ex1, ey0, ey1;
        int ix0, ix1, iy0, iy1;
        ex0=ex(x0,y0,z0);
        ix0= (int) ((ex0-exmin)*gmex/(exmax-exmin));
        ey0=ey(x0,y0,z0);
        iy0= (int) ((ey0-eymin)*gmey/(eymax-eymin));
        ex1=ex(x1,y1,z1);
        ix1= (int) ((ex1-exmin)*gmex/(exmax-exmin));
        ey1=ey(x1,y1,z1);
        iy1= (int) ((ey1-eymin)*gmey/(eymax-eymin));

        // Если mm не равно нулю, то выводим
        // изображение с удалением невидимых линий,
        // иначе – без удаления невидимых линий
        if (mm != 0) sline(ix0,iy0,ix1,iy1);
        else line(ix0,gmey-iy0,ix1,gmey-iy1);
    }

    //-------------------------------------------------------------------------
    public void draw(Canvas canvas)
    {
        this.canvas = canvas;
        gmex= (int) canvas.getWidth();
        gmey= (int) canvas.getHeight();




        float x, y, z;

        zmin=zmax=0.f;
        for (int i=0; i<=nx; i++)
            for (int j=0; j<=ny; j++)
            {
                z=fz(xmin+i*hx,ymin+j*hy);
                if (z>zmax) zmax=z;
                if (z<zmin) zmin=z;
            }

        exmax=ex(xmin, ymax, zmax);
        exmin=ex(xmax, ymin, zmin);
        eymax=ey(xmin, ymin, zmax);
        eymin=ey(xmax, ymax, zmin);
        for(x=xmin; x<=xmax; x+=xmax-xmin)
            for(y=ymin; y<=ymax; y+=ymax-ymin)
                for(z=zmin; z<=zmax; z+=zmax-zmin)
                {
                    if (exmax<ex(x,y,z)) exmax=ex(x,y,z);
                    if (exmin>ex(x,y,z)) exmin=ex(x,y,z);
                    if (eymax<ey(x,y,z)) eymax=ey(x,y,z);
                    if (eymin>ex(x,y,z)) eymin=ey(x,y,z);
                }

        phimax=new int[gmex+1]; phimin=new int[gmex+1];



        for(int i=0;i<gmex;i++) {phimax[i]=0; phimin[i]=gmey;}
        // Заполнение первой строки
        cval=fz(xmax, ymax);
        for(int i=ny-1;i>=0;i--)
        {
            cvals[i+1]=cval; y=ymin+hy*i;
            cval=fz(xmax, y);
            vectphi(xmax, y+hy, cvals[i+1], xmax, y, cval, 1);
        }
        cvals[0] = cval;



        // Заполнение остальных строк
        for (int j=nx-1;j>=0;j--)
        {
            x=xmin+hx*j;
            cval=fz(x, ymax);
            vectphi(x+hx, ymax, cvals[ny], x,ymax,cval, 1);
            for(int i=ny-1;i>=0;i--)
            {
                cvals[i+1]=cval; y=ymin+hy*i;
                cval=fz(x, y);
                vectphi(x+hx, y, cvals[i], x, y, cval, 1);
                vectphi(x, y+hy, cvals[i+1], x, y, cval, 1);
            }
            cvals[0] = cval ;
        }


    }


}