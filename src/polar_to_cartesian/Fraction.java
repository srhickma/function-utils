package polar_to_cartesian;

public class Fraction {

    private long num;
    private long den;

    public Fraction(long num, long den){
        this.num = num;
        this.den = den;
        clean();
    }

    public Fraction(String fraction){
        String[] parts = fraction.split("/");
        try{
            num = Long.parseLong(parts[0]);
            if(parts.length == 1){
                den = 1;
            }
            else{
                den = Long.parseLong(parts[1]);
            }
        }catch(Exception e){
            throw new RuntimeException("Invalid Function");
        }
        clean();
    }

    void multiplyBy(Fraction f){
        f.clean();
        num *= f.num;
        den *= f.den;
        clean();
    }

    void add(Fraction f){
        f.clean();
        long lcm = Util.lcm(den, f.den);
        den = Util.lcm(den, f.den);
        num = num * lcm / den + f.num * lcm / f.den;
        clean();
    }

    void subtract(Fraction f){
        f.clean();
        long lcm = Util.lcm(den, f.den);
        den = Util.lcm(den, f.den);
        num = num * lcm / den - f.num * lcm / f.den;
        clean();
    }

    private void clean(){
        if(den == 0){
            throw new RuntimeException("Invalid Fraction");
        }
        int sign = num * den < 0 ? -1 : 1;
        num = Math.abs(num);
        den = Math.abs(den);
        long gcd = Util.gcd(num, den);
        num /= gcd;
        den /= gcd;
        num *= sign;
    }

    boolean isZero(){
        return num == 0;
    }

    boolean isOne(){
        return num == den;
    }

    boolean isNegative(){
        return num < 0;
    }

    public boolean equals(Fraction f){
        return f.num == num && f.den == den;
    }

    @Override
    public String toString(){
        clean();
        StringBuilder result = new StringBuilder(Long.toString(num));
        if(den != 1){
            return "(" + result.append("/").append(Long.toString(den)).append(")").toString();
        }
        if(num < 0){
            return "(" + result.append(")").toString();
        }
        return result.toString();
    }

}
