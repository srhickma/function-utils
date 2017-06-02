package polar_to_cartesian;

public class CartesianFunction {

    private Fraction z, xy;

    private XYTerm[] terms;

    CartesianFunction(Fraction z, Fraction xy, XYTerm[] terms){
        this.z = z;
        this.xy = xy;
        this.terms = terms;
    }

    @Override
    public String toString(){
        StringBuilder expansion = new StringBuilder("");
        for(TermRep t : terms){
            expansion.append(t);
        }
        return String.format("%s%s(%s)", Util.powerString("z", z), Util.powerString("(x^2+y^2)", xy), expansion);
    }

}
