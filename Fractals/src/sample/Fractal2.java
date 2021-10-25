package sample;

public class Fractal2 implements Fractal {
    private final Complex [] root = new Complex[] {
            new Complex(-0.75),
            new Complex(-0.5, 1),
            new Complex(-0.5, -1)
    };

    //  f(z)  = z^3 + 7*z^2/4 + 2*z  + 15/16
    public Complex f(Complex z) {
        Complex z1 = z.mul(z).mul(z);
        Complex z2 = z.mul(z).mul(7).div(4);
        Complex z3 = z.mul(2);

        return z1.add(z2).add(z3).add(new Complex(15d/16d));
    }

    //  f'(z) = 3*z^2 + 7*z/2 + 2
    public Complex fs(Complex z) {
        Complex z1 = z.mul(z).mul(3);
        Complex z2 = z.mul(7).div(2);
        return z1.add(z2).add(new Complex(2));
    }

    public Complex[] getRoot() {
        return root;
    }
}
