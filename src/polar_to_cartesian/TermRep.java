package polar_to_cartesian;

public class TermRep {

    private long constant;
    private char sign;

    void setConstant(long constant){
        this.constant = constant;
    }

    void setSign(char sign){
        this.sign = sign;
    }

    long getConstant(){
        return constant;
    }

    char getSign(){
        return sign;
    }

}
