package polar_to_cartesian;

public class Launcher {

    public static void main(String[] args){
        if(args.length != 3){
            System.out.println("Usage: java -jar fconverter.jar {FRACTION:I} {FRACTION:J} {INTEGER:K}");
        }
        else{
            try{
                PolarFunction pf = new PolarFunction(args[0], args[1], Integer.parseInt(args[2]));
                CartesianFunction cf = PCMapper.map(pf);
                System.out.println("\n" + cf + "\n");
            } catch (RuntimeException re) {
                System.out.println("ERROR: " + re.getMessage());
            }
        }
    }

}
