package polar_to_cartesian;

import java.util.ArrayList;
import java.util.List;

public class PCMapper {

    public static CartesianFunction map(PolarFunction pf){
        System.out.println("\n>>STARTING_CONVERSION");
        System.out.println(String.format(">>Converting Polar Function To Cartesian Coordinates--->> %s", pf));
        int n = pf.getK();
        CiSTerm[] cisTerms = getComplexExpansion(n);
        System.out.println(String.format(">>Expanded And Simplified By DMT----------------------->> %s", printExpanded(pf.getI(), pf.getJ(), cisTerms)));
        XYTerm[] xyTerms = getCartesianExpansion(cisTerms);
        Fraction newJ = pf.getJ();
        newJ.subtract(new Fraction(pf.getK(), 1));
        System.out.println(String.format(">>Applied Map {z->z, pcos(ϕ)->x, psin(ϕ)->y}----------->> %s", printExpanded(pf.getI(), newJ, xyTerms)));
        newJ.multiplyBy(new Fraction(1, 2));
        CartesianFunction cf = new CartesianFunction(pf.getI(), newJ, xyTerms);
        System.out.println(String.format(">>Applied Map {p->(x^2+y^2)^(1/2)}--------------------->> %s", cf));
        System.out.println(">>CONVERSION_FINISHED");
        return cf;
    }

    static CiSTerm[] getComplexExpansion(int n){
        List<CiSTerm> terms = new ArrayList<>();
        for(int i = 0; i < n + 1; i += 2){
            long constant = Util.choose(n, i);
            char sign = i % 4 == 0 ? '+' : '-';
            terms.add(new CiSTerm(n - i, i, constant, sign));
        }
        return terms.toArray(new CiSTerm[terms.size()]);
    }

    static XYTerm[] getCartesianExpansion(CiSTerm[] cisTerms){
        XYTerm[] terms = new XYTerm[cisTerms.length];
        for(int i = 0; i < terms.length; i ++){
            terms[i] = new XYTerm(cisTerms[i]);
        }
        return terms;
    }

    private static String printExpanded(Fraction i, Fraction j, TermRep[] terms){
        StringBuilder expansion = new StringBuilder("");
        for(TermRep t : terms){
            expansion.append(t);
        }
        return String.format("%s%s(%s)", Util.powerString("z", i), Util.powerString("p", j), expansion);
    }

}
