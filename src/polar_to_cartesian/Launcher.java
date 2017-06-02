package polar_to_cartesian;

public class Launcher {

    public static void main(String[] args){
        if(args.length != 4){
            System.out.println("Usage: java f-convert {FRACTION:I} {FRACTION:J} {INTEGER:K}");
        }
        else{
            try{
                PolarFunction pf = new PolarFunction(args[2], args[3], Integer.parseInt(args[4]));
                CartesianFunction cf = PCMapper.map(pf);
                System.out.println("\n" + cf + "\n");
            }catch(RuntimeException re){
                System.out.println("ERROR: " + re.getMessage());
            }
        }
    }

}
