package test;

import function_utils.*;

import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args){
        Long start = System.nanoTime();

        Function f = new Function("9*x+4*x*(3*x-5*(x-3))*3*y");
        //Function f = new Function("3+4*x^2");
        //Function f = new Function("(3+4*x^2+5*4)^4");
        //Function f = new Function("3*(3+4*x)^2");
        //Function f = new Function("(3+5*x)*(2*6*y+1)^5");
        //f.expand();
        //System.out.println(f.toString());
        /*Function f1 = new Function("4+3 *(x* 5+2)^2-53*(x*5+2)^2 - 4*(5)(5 *x)");
        Function f2 = new Function("4+(-50)*(5*x+2)^2-100*x");
        f1.expand();
        f2.expand();
        System.out.println(f1 + " vs " + f2);
        System.out.println(f1.equals(f2));*/
        //Function f = new Function("4*x");
        //Function f = new Function("1 + x");
        Map<Character, Integer> valmap = new HashMap<>();
        valmap.put('x', 2);
        System.out.println(f);
        Function result = f.sub(valmap);
        System.out.println(result);
        valmap.put('y', -1);
        System.out.println(Integer.parseInt(result.sub(valmap).toString()));

        System.out.println((System.nanoTime() - start) / 1000000);
    }

}
