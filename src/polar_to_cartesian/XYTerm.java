package polar_to_cartesian;

public class XYTerm extends TermRep {

    private int x = 1;
    private int y = 1;

    XYTerm(CiSTerm cisTerm){
        this.x = cisTerm.getCos();
        this.y = cisTerm.getSin();
        setConstant(cisTerm.getConstant());
        setSign(cisTerm.getSign());
    }

    @Override
    public String toString(){
        if(y == 0 && getSign() == '+'){
            return Util.constantString(getConstant()) + Util.powerString("x", x) + Util.powerString("y", y);
        }
        return getSign() + Util.constantString(getConstant()) + Util.powerString("x", x) + Util.powerString("y", y);
    }

}
