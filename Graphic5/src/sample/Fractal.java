package sample;

public interface Fractal {

    public abstract Complex f(Complex z);
    public abstract Complex fs(Complex z);
    public abstract Complex[] getRoot();
}
