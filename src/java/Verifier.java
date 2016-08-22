/* Verifier holds methods to verify b2 sequences. So far verifying methods include
treeMapVerify and optimalSpaceVerify
*/

import java.util.*;

public class Verifier{

    /* Pretty simple and straightforward, just searching to see if our sum is already in the TreeMap */
    public boolean setVerify(ArrayList<Integer> a, Set<Integer> distances, int n){
        int search = 0;

        for(int i = 0; i < a.size(); i++){
            search = n-a.get(i);
            if(distances.contains(search)){ //we only need to see if it has this key
                return false;
            }
        }
        return true;
    }
    
    public boolean HmVerify(ArrayList<Integer> a, HashMap<Integer, Integer> sums, int n){
        int search = 0;
            
        for(int i = 0; i < a.size(); i++){
            search = n+a.get(i);
            if(sums.containsKey(search)){ //we only need to see if it has this key
                return false;
            }
        }
        return true;
    }
    /*ATTN: The value in the treemap's key value pair is currently meaningless.
     We might think of replacing the value in KV pair with some other useful thing.
     */

    public void addKeys(ArrayList<Integer> a, HashMap<Integer, Integer> sums, int n){
        for(int i = 0; i < a.size(); i++){
            sums.put(a.get(i)+n, n);
                
        }
        //Adding final key
        sums.put(n+n, n);
        return;
    }
    public void addDistances(ArrayList<Integer> a, Set<Integer> distances, int n){
        for(int i = 0; i < a.size(); i++){
            distances.add(n-a.get(i));

        }
        return;
    }



    public boolean binarySearch(ArrayList<Integer> a, int search, int j){
        int mid;
        int left = 0;
        int right = j;
        while(left <= right){
            mid = (left + (right-left)/2);
            if(search < a.get(mid)){
                right = mid-1;
            }
            else if(search > a.get(mid)){
                left = mid+1;
            }
            else{ // found mid, trichotomy
                return true;
            }
        }
        return false;
    }
        /* Binary search method, may not be asymptotically optimal, but may run faster for smaller sets due to constants.
         Also, no space use*/
    public boolean optimalSpaceVerify(ArrayList<Integer> a, int n){
        int sum;
        int search;
        int len = a.size();
        for(int i = len-1; i >= 0; i--){
            if(2*a.get(i) < n){
                return true;
            }
            for(int j = i; j >=0; j--){
                sum = a.get(i) + a.get(j);
                search = sum-n;
                if(search < 0){
                    break;
                }
                if(binarySearch(a,search,j)){
                    return false;
                }
            }
        }
        return true;

    }
}
