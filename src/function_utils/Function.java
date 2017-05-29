package function_utils;

import java.util.Map;
import java.util.Vector;

public class Function {

    private Vector<Term> terms = new Vector<>();

    private int power = 1;

    private AdvancedFunction advancedFunction = AdvancedFunction.NONE;

    public Function(String string){
        string = FUtil.sanitize(string);
        Vector<String> termStrings = FUtil.depthSplit(string, '+');
        if(termStrings.size() == 1 && FUtil.depthSplitSize(string, '*') == 1){
            advancedFunction = FUtil.getAdvancedFunction(string);
            if(advancedFunction != AdvancedFunction.NONE){
                termStrings.set(0, string.substring(string.indexOf('(')));
            }
            else{
                Vector<String> powerStrings = FUtil.depthSplit(string, '^');
                if(powerStrings.size() == 2){
                    power = Integer.parseInt(powerStrings.get(1));
                    termStrings.set(0, powerStrings.get(0));
                }
            }
        }
        for(String s : termStrings){
            terms.add(new Term(s));
        }
    }

    Function(Vector<Term> terms){
        this.terms = terms;
    }

    private Function(){}

    public void expand(){
        Function result = new Function();
        for(Term t : terms){
            Function f = t.expand();
            if(f != null){
                result.addFunction(t.expand());
            }
            else{
                result.addTerm(t);
            }
        }
        result.groupExpanded();
        terms = result.terms;
    }

    Vector<Term> getTerms(){
        return terms;
    }

    Term getTerm(int i){
        return terms.get(i);
    }

    private void groupExpanded(){
        for(Term t: terms){
            t.clean();
        }
        for(int i = 0; i < terms.size(); i ++){
            for(int j = i + 1; j < terms.size(); j ++) {
                Term t1 = getTerm(i), t2 = getTerm(j);
                if(t1.sameMults(t2)){
                    t1.setConstant(t1.getConstant() + t2.getConstant());
                    if(t1.getConstant() == 0){
                        terms.remove(i);
                        i --;
                        j --;
                    }
                    terms.remove(j);
                    j --;
                }
            }
        }
    }

    private void addFunction(Function f){ terms.addAll(f.terms); }

    private void addTerm(Term t){ terms.add(t); }

    void multiplyByInt(int constant){
        for(Term t : terms){
            t.setConstant(t.getConstant() * constant);
        }
    }

    private boolean distributive(){
        return advancedFunction == AdvancedFunction.NONE && power == 1;
    }

    public boolean equals(Function f){
        if(power != f.power || terms.size() != f.terms.size() || advancedFunction != f.advancedFunction){
            return false;
        }
        if(terms.size() == 1 && f.terms.size() == 1){
            Term t1 = terms.get(0);
            Term t2 = f.terms.get(0);
            if(t1.getMultipliers().size() == 0 && t2.getMultipliers().size() == 0){
                return t1.getVarName() == t2.getVarName() && t1.getConstant() == t2.getConstant();
            }
        }
        expand();
        f.expand();
        for(Term t1 : terms){
            boolean found = false;
            for(Term t2 : f.terms){
                if(t1.isEquivalent(t2)){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        return true;
    }

    public Function sub(Map<Character, Integer> valmap){
        return sub(valmap, true);
    }

    Function sub(Map<Character, Integer> valmap, boolean topCall){
        Function newFunc = new Function();
        for(Term t : terms){
            newFunc.addTerm(t.sub(valmap));
        }
        if(topCall){
            newFunc.expand();
        }
        return newFunc;
    }

    @Override
    public String toString(){
        StringBuilder f = new StringBuilder("");
        for(int i = 0; i < terms.size() - 1; i ++){
            f.append(terms.get(i).toString()).append("+");
        }
        f.append(terms.get(terms.size() - 1).toString());
        String fString = f.toString();
        if(advancedFunction != AdvancedFunction.NONE){
            fString = advancedFunction.name().toLowerCase() + "(" + fString + ")";
        }
        else if(power != 1 && (terms.size() != 1 || terms.get(0).getMultipliers().size() > 0 || terms.get(0).getConstant() != 1)){
            fString = "(" + fString + ")";
        }
        fString += FUtil.powerString(power);
        if(terms.size() == 1 && fString.matches("\\((.*)\\)")){
            fString = fString.substring(1, fString.length() - 1);
        }
        return fString;
    }

    public static Function multiply(Function f1, Function f2){
        f1.expand();
        f2.expand();
        return multiplyExpanded(f1, f2);
    }

    static Function multiplyExpanded(Function f1, Function f2){
        Vector<Term> productTerms = new Vector<>();
        if(!f1.distributive() && !f2.distributive()){
            productTerms.add(new Term(1, f1, f2));
        }
        else if(!f1.distributive()){
            for(Term t : f2.terms){
                productTerms.add(new Term(t, f1));
            }
        }
        else if(!f2.distributive()){
            for(Term t : f1.terms){
                productTerms.add(new Term(t, f2));
            }
        }
        else{
            for(Term t1 : f1.terms){
                for(Term t2 : f2.terms){
                    productTerms.add(Term.multiply(t1, t2));
                }
            }
        }
        Function product = new Function(productTerms);
        product.groupExpanded();
        return product;
    }

}
