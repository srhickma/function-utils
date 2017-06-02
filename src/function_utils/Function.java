package function_utils;

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
        expand();
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

    void setPower(int power){
        this.power = power;
    }

    int getPower(){
        return power;
    }

    private boolean distributive(){
        return advancedFunction == AdvancedFunction.NONE && power == 1;
    }

    public boolean multipleOf(Function f){
        if(terms.size() > 1){ //TODO only works if f is only one term, need to implement factoring otherwise
            //there is no coefficient so they must be equal
            if(equals(f)){
                return true;
            }
            for(Term t : terms){
                if(!t.multipleOf(f)){
                    return false;
                }
            }
            return true;
        }
        Term term = terms.get(0);
        if(f.terms.size() > 1){
            //need to check that f is equal to a multiplier of this
            for(Function fx : term.getMultipliers()){
                if(fx.equals(f)){
                    return true;
                }
            }
            return false;
        }
        //Both functions only have one term
        Term fterm = f.terms.get(0);
        for(Function fx : fterm.getMultipliers()){
            boolean found = false;
            for(Function fy : term.getMultipliers()){
                if(fx.equals(fy)){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        if(fterm.isVariable() && term.isVariable() && fterm.getVarName() != term.getVarName()){
            return false;
        }
        return term.getConstant() % fterm.getConstant() == 0 && term.getConstant() >= fterm.getConstant();
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

    public boolean equalsVoidPower(Function f){
        if(terms.size() != f.terms.size() || advancedFunction != f.advancedFunction){
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

    public Function sub(Substitution... map){
        return sub(true, map);
    }

    Function sub(boolean topCall, Substitution... map){
        Function newFunction = new Function(toString());
        for(Term t : newFunction.terms){
            for(Substitution s : map){

                if(t.containsAndRemove(s)){
                    System.out.println(String.format("Replacing %s with %s", t.toString(), s.getTo().toString()));
                }

            /*if(multipleOf(s.getFrom())){
                System.out.println(String.format("Replacing %s with %s", toString(), s.getTo().toString()));
                return s.getTo();
            }*/
            }
        }
        /*if(terms.size() == 1 && terms.get(0).getMultipliers().size() == 0){
            return this;
        }
        Function newFunc = new Function();
        for(Term t : terms){
            newFunc.addTerm(t.sub(map));
            System.out.println("");
        }
        System.out.println();
        newFunc.power = power;
        newFunc.advancedFunction = advancedFunction;
        if(topCall){
            newFunc.expand();
        }
        return newFunc;*/
        //return new Function("");
        return newFunction;
    }

    /*public Function subVariable(Map<Character, Integer> valmap){
        return subVariable(valmap, true);
    }

    public Function subSimple(Map<Function, Function> fmap){
        return subSimple(fmap, true);
    }

    Function subVariable(Map<Character, Integer> valmap, boolean topCall){
        Function newFunc = new Function();
        for(Term t : terms){
            newFunc.addTerm(t.sub(valmap));
        }
        if(topCall){
            newFunc.expand();
        }
        return newFunc;
    }

    Function subSimple(Map<Function, Function> fmap, boolean topCall){
        for(Function f : fmap.keySet()){
            if(equals(f)){
                return fmap.get(f);
            }
        }
        Function newFunc = new Function();
        for (Term t : terms) {
            newFunc.addTerm(t.subSimple(fmap));
        }
        if (topCall) {
            newFunc.expand();
        }
        return newFunc;
    }*/

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
