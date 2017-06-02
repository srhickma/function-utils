package function_utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

public class Term {

    private Vector<Function> multipliers = new Vector<>();

    private boolean var = false;
    private char varName = ' ';

    private int constant = 1;

    Term(String string){
        Vector<String> mults = FUtil.depthSplit(string, '*'), multStrings = new Vector<>();
        for(String s : mults){
            if(s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')'){
                s = s.substring(1, s.length() - 1);
            }
            try{
                constant *= Integer.parseInt(s);
            }catch(Exception e){
                multStrings.add(s);
            }
        }
        if(multStrings.size() > 1){
            for(String s : multStrings){
                addMultiplyerFromString(s);
            }
        }
        else if(multStrings.size() > 0){
            if(multStrings.get(0).length() == 1){
                var = true;
                varName = multStrings.get(0).charAt(0);
            }
            else{
                addMultiplyerFromString(multStrings.get(0));
            }
        }
    }

    Term(int constant, Function... multipliers){
        this.constant = constant;
        this.multipliers = new Vector<>(Arrays.asList(multipliers));
    }

    Term(Term term, Function... multipliers){
        this.constant = term.constant;
        this.multipliers = new Vector<>(Arrays.asList(multipliers));
        this.multipliers.addAll(term.getMultipliers());
        if(term.var){
            Vector<Term> varTermVector = new Vector<>();
            Term varTerm = new Term(term.varName);
            varTermVector.add(varTerm);
            this.multipliers.add(new Function(varTermVector));
        }
    }

    private Term(char varName){
        this.var = true;
        this.varName = varName;
    }

    private void addMultiplyerFromString(String multString){
        multipliers.add(new Function(multString));
    }

    Function expand(){
        if(canExpand()){
            Function product = new Function("1");
            for(Function m : multipliers){
                m.expand();
                product = Function.multiplyExpanded(product, m);
            }
            product.multiplyByInt(constant);
            return product;
        }
        return null;
    }

    void clean(){
        for(int i = 0; i < multipliers.size(); i ++){
            Function f = multipliers.get(i);
            if(f.getTerms().size() == 1 && !f.getTerm(0).canExpand()){
                Term t = f.getTerm(0);
                constant *= t.constant;
                if(t.var){
                    t.constant = 1;
                }
                else{
                    multipliers.remove(i);
                    i --;
                }
            }
        }
    }

    private boolean canExpand(){
        return multipliers.size() != 0;
    }

    boolean sameMults(Term t){
        if(multipliers.size() != t.multipliers.size()){
            return false;
        }
        if(multipliers.size() == 0){
            return varName == t.varName;
        }
        for(Function f1 : multipliers){
            boolean found = false;
            for(Function f2 : t.getMultipliers()){
                if(f1.equals(f2)){
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

    boolean isEquivalent(Term t){
        return constant == t.getConstant() && sameMults(t);
    }

    void addMultipliers(Vector<Function> ms){
        for(Function m : ms){
            addMultiplier(m);
        }
    }

    void addMultiplier(Function m){
        for(Function f : multipliers){
            if(f.multipleOf(m)){
                f.setPower(f.getPower() + m.getPower());
                return;
            }
        }
        multipliers.add(m);
    }

    Vector<Function> getMultipliers(){
        return multipliers;
    }

    int getConstant(){
        return constant;
    }

    void setConstant(int constant){
        this.constant = constant;
    }

    char getVarName(){
        return varName;
    }

    boolean isVariable(){
        return var;
    }

    /*Term sub(Map<Character, Integer> valmap){
        if(var){
            if(valmap.containsKey(varName)){
                return new Term(getConstant() * valmap.get(varName));
            }
            else{
                return new Term(this);
            }
        }
        else if(multipliers.size() == 0){
            return new Term(constant);
        }
        else{
            Vector<Function> newMultipliers = new Vector<>();
            for(Function f : getMultipliers()){
                newMultipliers.add(f.sub(valmap, false));
            }
            return new Term(constant, newMultipliers.toArray(new Function[newMultipliers.size()]));
        }
    }*/

    public boolean multipleOf(Function f){
        if(f.getTerms().size() > 1){
            //need to check that f is equal to a multiplier of this
            for(Function fx : multipliers){
                if(fx.equals(f)){
                    return true;
                }
            }
            return false;
        }
        //Both functions only have one term
        Term fterm = f.getTerms().get(0);
        for(Function fx : fterm.multipliers){
            boolean found = false;
            for(Function fy : multipliers){
                if(fx.equals(fy)){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        return constant % fterm.constant == 0 && constant >= fterm.constant;
    }

    Term sub(Substitution... map){
        Vector<Function> newMultipliers = new Vector<>();
        for(Function f : getMultipliers()){
            newMultipliers.add(f.sub(false, map));
        }
        return new Term(constant, newMultipliers.toArray(new Function[newMultipliers.size()]));
    }

    @Override
    public String toString(){
        StringBuilder t = new StringBuilder("");
        if(constant != 1 || (!var && multipliers.size() == 0)){
            if(constant < 0){
                t.append("(").append(constant).append(")");
            }
            else{
                t.append(constant);
            }
        }
        if(constant != 1 && (multipliers.size() != 0 || var)){
            t.append("*");
        }
        if(var){
            t.append(varName);
        }
        else{
            for(int i = 0; i < multipliers.size(); i ++){
                Function m = multipliers.get(i);
                if(i > 0){
                    t.append("*");
                }
                if(m.getTerms().size() > 1){
                    t.append("(").append(m.toString()).append(")");
                }
                else{
                    t.append(m.toString());
                }
            }
        }
        return t.toString();
    }

    static Term multiply(Term t1, Term t2){
        Term product = new Term("1");
        product.constant = t1.constant * t2.constant;
        if(t1.var && t2.var){
            product.addMultiplier(new Function(Character.toString(t1.varName)));
            product.addMultiplier(new Function(Character.toString(t2.varName)));
        }
        else if(t1.var){
            if(t2.multipliers.size() == 0){
                product.var = true;
                product.varName = t1.varName;
                return product;
            }
            product.addMultiplier(new Function(Character.toString(t1.varName)));
        }
        else if(t2.var){
            if(t1.multipliers.size() == 0){
                product.var = true;
                product.varName = t2.varName;
                return product;
            }
            product.addMultiplier(new Function(Character.toString(t2.varName)));
        }
        product.addMultipliers(t1.getMultipliers());
        product.addMultipliers(t2.getMultipliers());
        return product;
    }

    void multiplyBy(Term t){
        constant = constant * t.constant;
        if(var && t.var){
            addMultiplier(new Function(Character.toString(varName)));
            addMultiplier(new Function(Character.toString(t.varName)));
            var = false;
            varName = ' ';
        }
        else if(var){
            if(t.multipliers.size() == 0){
                return;
            }
            addMultiplier(new Function(Character.toString(varName)));
            var = false;
            varName = ' ';
        }
        else if(t.var){
            if(multipliers.size() == 0){
                var = true;
                varName = t.varName;
                return;
            }
            addMultiplier(new Function(Character.toString(t.varName)));
        }
        addMultipliers(t.getMultipliers());
    }

    boolean containsAndRemove(Substitution s){
        Term from = s.getFrom();
        int maxPowerMult = -1;
        if(constant % from.constant != 0){
            return false;
        }
        int newConstant = constant / from.constant;
        Vector<Function> newMults = new Vector<>();
        for(Function f1 : from.multipliers){
            boolean found = false;
            for(Function f2 : multipliers){
                if(f1.equalsVoidPower(f2) && f2.getPower() >= f1.getPower()){
                    found = true;
                    int newPower = f2.getPower() - f1.getPower();
                    int powerMult = f2.getPower() / f1.getPower();
                    if(powerMult < maxPowerMult || maxPowerMult < 0){
                        maxPowerMult = powerMult;
                    }
                    if(newPower > 0){
                        Function newMult = new Function(f2.toString());
                        newMult.setPower(newPower);
                        newMults.add(newMult);
                    }
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        for(Function f2 : multipliers){
            boolean found = false;
            for(Function f1 : from.multipliers){
                if(f1.equalsVoidPower(f2) && f2.getPower() >= f1.getPower()){
                    found = true;
                    break;
                }
            }
            if(!found){
                newMults.add(f2);
            }
        }
        constant = newConstant;
        //for(int i = 0){
        //
        //}
        multipliers = newMults;
        System.out.println("Multiplying " + toString() + " by " + s.getTo().toString());
        for(int i = 0; i < maxPowerMult; i ++){
            multiplyBy(s.getTo());
        }
        return true;
    }

}
