/* Largest component is localSearch

Local searching methods that either skip gaps or forgo possible adds to candidate sequences.
Fixed stackoverflow problem


*/
import function.ToDoubleFunction;
import function.FunctionUtility;
import util.SequenceUtility;
import java.util.*;

public class Search{
    public ToDoubleFunction<Integer> func;
    private Verifier v;
    private static int DEFAULT_DEPTH = 10000;
    private double globalMax;
    private ArrayList<Integer> maxSequenceFound;
    private ArrayList<Skip> maxSkip;
    public Search(){
        Verifier v = new Verifier();
        this.v = v;
       // func = FunctionUtility.getDefaultFunctionMap().get("invert");
        maxSkip = new ArrayList<>();
        maxSequenceFound = new ArrayList<>();
        globalMax = 0;
    }
    public ArrayList<Integer> getMaxSequenceFound(){
        return this.maxSequenceFound;
    }
    public ArrayList<Skip> getMaxSkip(){
        return this.maxSkip;
    }
    
//Begin methods
    public ArrayList<Integer> depth(int depth, ArrayList<Skip> skip){
        ArrayList<Integer> results = new ArrayList<Integer>();
        HashMap<Integer,Integer> sums = new HashMap<Integer,Integer>();
        for(int i = 1; i < depth; i++){
            for(Skip s : skip){
                if(s.contains(i)){
                    i = s.getRight();
                }
            }
            if(v.HmVerify(results,sums,i)){
                v.addKeys(results,sums,i);
                results.add(i);
            }
        }
        return results;
    }
    public ArrayList<Integer> depthElements(int numElements, int forgoElement, ArrayList<Skip> skip,  ArrayList<Integer> results, HashMap<Integer,Integer> sums){
        int i = 1;
        boolean check = false;
        while(results.size() < numElements){
            for(Skip s : skip){
                if(s.contains(i)){
                    i = s.getRight();
                }
            }
            if(v.HmVerify(results,sums,i)){
                if(results.size() == forgoElement && !check){
                    i++;
                    check = true;
                    continue;
                }
                v.addKeys(results,sums,i);
                results.add(i);
            }
            i++;
        }
        return results;
    }

    
    /*local search that forgoes adding elements rather than skipping gaps TODO: Currently compiles with some warning message because of 
     ToDoubleFunction thing, should prob. figure out*/
    public void localSearch(ArrayList<Skip> skip, ArrayList<Integer> last, ToDoubleFunction func){
        double maximumWeight = SequenceUtility.reverseSum(last,func);
        double currWeight;
        
        int numElements = last.size(); //Using this way causes some trouble if our weight function is golomb
        //Now just initializing stuff that will be needed
        Skip carry = new Skip(0,0);
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayList<Integer> nextArray = new ArrayList<Integer>();
        HashMap<Integer, Integer> sums = new HashMap<Integer,Integer>();
        int c = 0; 
        boolean foundImprovement = false; // the solution to stack overflow prob, we check this at the end
        while(c < numElements){ //the c^th element will be forgone
            currWeight = 0;
            sums.clear();
            results = depthElements(numElements,c,skip,results,sums); //calculate whether results forgoing the element at c has > maximumWeight
            currWeight = SequenceUtility.reverseSum(results,func);
            if(currWeight > maximumWeight){
                foundImprovement = true;
                maximumWeight = currWeight;
                clearAndSetNextArray(nextArray,results);
                findAndSetNewSkip(results,last,carry,c);
                ArrayList<Skip> innerSkip = createInnerSkip(skip,carry);
                localSearch(innerSkip,nextArray,func);
            }
            c++;
            results.clear();
        }
        if(carry.getLeft() != carry.getRight()){
            skip.add(carry);
        }
        double s = SequenceUtility.reverseSum(nextArray,func);
        if(s > this.globalMax){
            this.maxSequenceFound = nextArray;
            this.maxSkip = skip;
            this.globalMax = s;
        }
        if(foundImprovement){
            localSearch(skip,nextArray,func);
        }
    }
    
    public void localSearchOne(ArrayList<Skip> skip, ArrayList<Integer> last, ToDoubleFunction func){
        double maximumWeight = SequenceUtility.reverseSum(last,func);
        int numElements = last.size();
        int count = 0;
        Skip carry = new Skip(0,0);
        double currWeight;
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayList<Integer> nextArray = new ArrayList<Integer>();
        HashMap<Integer, Integer> sums = new HashMap<Integer,Integer>();
        int c = 0;
        boolean foundImprovement = false;
        while(c < numElements){ //the c^th element will be forgone
            currWeight = 0;
            sums.clear();
            results = depthElements(numElements,c,skip,results,sums); //calculate whether results forgoing the element at c has > maximumWeight
            currWeight = SequenceUtility.reverseSum(results,func);
            if(currWeight > maximumWeight){
                foundImprovement = true;
                maximumWeight = currWeight;
                clearAndSetNextArray(nextArray,results);
                findAndSetNewSkip(results,last,carry,c);

            }
            c++;
            results.clear();
        }
        if(carry.getLeft() != carry.getRight()){
            skip.add(carry);
        }
        double s = SequenceUtility.reverseSum(nextArray,func);
        if(s > this.globalMax){
            this.maxSequenceFound = nextArray;
            this.maxSkip = skip;
            this.globalMax = s;
        }
        if(foundImprovement){
            localSearch(skip,nextArray,func);
        }
    }
    //Some helper methods for localSearch
    
    
        /* Finds where the resulting array and the previous array differ, since that element must have been forgone */
    private void findAndSetNewSkip(ArrayList<Integer> results, ArrayList<Integer> last, Skip carry, int forgoneElement){
        for(int j = 0; j < results.size(); j++){
            if(!last.get(j).equals(results.get(j))){
                int left,right;
                left = last.get(j) - 1;
                right = last.get(j) + 1;
                carry.setLeft(left);
                carry.setRight(right);
                carry.setIndex(forgoneElement);
                return;
            }
        }
        
        throw new IllegalStateException("da da da");
    }
    private ArrayList<Skip> createInnerSkip(ArrayList<Skip> prev, Skip carry){
        ArrayList<Skip> ret = new ArrayList<Skip>();
        for(Skip s: prev){
            ret.add(s);
        }
        if(carry.getLeft() != carry.getRight()){
            ret.add(carry);
        }
        return ret;
    }
    
    private void clearAndSetNextArray(ArrayList<Integer> nextArray, ArrayList<Integer> results){
        nextArray.clear();
        for(int i : results){
            nextArray.add(i);
        }
    }
}
