package polar_to_cartesian;

public class PolarFunction {

    private Fraction i, j;

    int k;

    public PolarFunction(String i, String j, int k){
        this.i = new Fraction(i);
        this.j = new Fraction(j);
        this.k = k;
    }

    @Override
    public String toString(){
        return String.format("%s%scos(%sϕ)", Util.powerString("z", i), Util.powerString("p", j), Util.constantString(k));
    }

    Fraction getI(){
        return i;
    }

    Fraction getJ(){
        return j;
    }

    int getK(){
        return k;
    }

}
