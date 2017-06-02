package polar_to_cartesian;

public class Util {

    static String powerString(String var, Fraction power){
        if(power.isOne()){
            return var;
        }
        if(power.isZero()){
            return "";
        }
        return var + "^" + power;
    }

    static String powerString(String var, int power){
        if(power == 1){
            return var;
        }
        if(power == 0){
            return "";
        }
        if(power < 0){
            return var + "^(" + power + ")";
        }
        return var + "^" + power;
    }


    static String funcPowerString(String func, String var, int power){
        if(power == 1){
            return func + "(" + var + ")";
        }
        if(power == 0){
            return "";
        }
        if(power < 0){
            return func + "^(" + power + ")(" + var + ")";
        }
        return func + "^" + power + "(" + var + ")";
    }

    static String constantString(long constant){
        if(constant == 1){
            return "";
        }
        return Long.toString(constant);
    }

    static long choose(long a, long b){
        if(b == 0 || b == a){
            return 1;
        }
        return scopeFactorial(a, a - b + 1) / scopeFactorial(b, 1);
    }

    static long scopeFactorial(long a, long b){
        long prod = 1;
        if(a < b){
            long temp = a;
            a = b;
            b = temp;
        }
        for(long i = b; i <= a; i ++){
            prod *= i;
        }
        if(prod < 0){
            throw new RuntimeException("Long Overflow Occurred");
        }
        return prod;
    }

    static long gcd(long x, long y){
        long a = x, b = y, r = 0;
        while(b != 0){
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    static long lcm(long x, long y){
        return x * y / gcd(x, y);
    }


}
