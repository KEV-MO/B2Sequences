
public class Skip{
    private static int DEFAULT_INDEX = -1;
    private int left;
    private int right;
    private int index;

    public Skip(int left, int right){
        this.left = left;
        this.right = right;
        this.index = DEFAULT_INDEX;
        
    }
    public Skip(int left, int right, int index){
        this.left = left;
        this.right = right;
        this.index = index;
    }

    public int getLeft(){
        return this.left;
    }
    public int getRight(){
        return this.right;
    }
    public String toString(){
        String s = "Skip : (" + this.left + "," + this.right+")" + " at index: " + this.index;
        return s;
    }
    public void setLeft(int left){
        this.left = left;
    }
    public void setRight(int right){
        this.right = right;
    }
    public void setIndex(int index){
        this.index = index;
    }

    public boolean contains(int a){
        return (a < right && a > left);
    }
}
