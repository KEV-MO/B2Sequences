 /*
  ex: java Driver 50 invert
  
  calculates localSearch for 50 elements.
  
 */
import java.util.*;
import function.FunctionUtility;
import function.ToDoubleFunction;
import util.SequenceUtility;
class Driver{
    public static void main(String[] args){
        Search s = new Search();
        int d = Integer.parseInt(args[0]);
        String f = args[1];
        ArrayList<Skip> a = new ArrayList<>();
        HashMap<Integer,Integer> sums = new HashMap<>();
        ArrayList<Integer> initial = new ArrayList<>();
        ToDoubleFunction func = FunctionUtility.getDefaultFunctionMap().get(f);
        s.setGlobalMax(Double.NEGATIVE_INFINITY);
        initial = s.depthElements(d,-1,a,initial,sums);
        s.localSearch(a,initial,func);
        ArrayList<Skip> o = s.getMaxSkip();
        for(Skip sk: o){
            System.out.println(sk.toString());
        }
        ArrayList<Integer> out = s.getMaxSequenceFound();
        System.out.println("Maximum found at depth: " + d);
        SequenceUtility.print(System.out,out);
        System.out.println();
        System.out.println("---------");
        System.out.println("With weight: " + (SequenceUtility.reverseSum(out,func)));
        System.out.println();
        System.out.println("---------");
        System.out.println("Max skip array: ");


        System.out.println("Weight calculated: " + SequenceUtility.reverseSum(out,func));

    }
}
