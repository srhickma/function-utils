package polar_to_cartesian;

public class CiSTerm extends TermRep {

    private int cos = 1;
    private int sin = 1;

    CiSTerm(int cos, int sin, long constant, char sign){
        this.cos = cos;
        this.sin = sin;
        setConstant(constant);
        setSign(sign);
    }

    @Override
    public String toString(){
        if(sin == 0 && getSign() == '+'){
            return Util.constantString(getConstant()) + Util.funcPowerString("cos", "ϕ", cos) + Util.funcPowerString("sin", "ϕ", sin);
        }
        return getSign() + Util.constantString(getConstant()) + Util.funcPowerString("cos", "ϕ", cos) + Util.funcPowerString("sin", "ϕ", sin);
    }

    void setCos(int cos){
        this.cos = cos;
    }

    void setSin(int sin){
        this.sin = sin;
    }

    int getCos(){
        return cos;
    }

    int getSin(){
        return sin;
    }

}
