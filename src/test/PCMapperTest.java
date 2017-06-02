package test;

import polar_to_cartesian.CartesianFunction;
import polar_to_cartesian.PCMapper;
import polar_to_cartesian.PolarFunction;

public class PCMapperTest {

    public static void main(String[] args){
        PolarFunction pf = new PolarFunction("3/4", "57/3", 50);
        CartesianFunction cf = PCMapper.map(pf);
        System.out.println(cf);
    }

}
