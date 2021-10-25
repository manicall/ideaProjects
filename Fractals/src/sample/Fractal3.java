package sample;

public class Fractal3 implements Fractal {
    private final Complex [] root = new Complex[] {
        new Complex(1),
                new Complex(-0.5, Math.sqrt(3.) / 2),
                new Complex(-0.5, -Math.sqrt(3.) / 2) };

    //example
    public Complex f(Complex z) {
        return z.mul(z).mul(z).sub(new Complex(1));
    }

    public Complex fs(Complex z) {
        return z.mul(z).mul(new Complex(3));
    }

    public Complex[] getRoot() {
        return root;
    }
}