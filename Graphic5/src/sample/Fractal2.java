package sample;

public class Fractal2 extends Fractal {

    // f(z)  = z^3 + 5*z^2/2 + 3*z + 1
    Complex f(Complex z) {
        Complex z1 = z.mul(z).mul(z);
        Complex z2 = z.mul(z).mul(5).div(2);
        Complex z3 = z.mul(3);

        return z1.add(z2).add(z3).add(new Complex(1));
    }
    // f'(z) = 3*z^2 + 5*z + 3
    Complex fs(Complex z) {
        Complex z1 = z.mul(z).mul(3);
        Complex z2 = z.mul(5);
        return z1.add(z2).add(new Complex(3));
    }
}
