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
        multipliers.addAll(ms);
    }

    void addMultiplier(Function m){
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

    Term sub(Map<Character, Integer> valmap){
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

}
