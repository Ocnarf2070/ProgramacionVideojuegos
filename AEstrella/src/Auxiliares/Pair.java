package Auxiliares;

public class Pair<First extends Comparable<? super First>, Second extends Comparable<? super Second>> implements Comparable<Pair<First, Second>>{
    private First first;
    private Second second;

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public void setSecond(Second second) {
        this.second = second;
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }

    public void set(First first, Second second) {
        setFirst(first);
        setSecond(second);
    }

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Pair)){
            return false;
        }

        Pair<First,Second> other_ = (Pair<First,Second>) other;

        // this may cause NPE if nulls are valid values for x or y. The logic may be improved to handle nulls properly, if needed.
        return other_.first.equals(this.first) && other_.second.equals(this.second);
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "("+first+","+second+")";
    }
    
    @Override
    public int compareTo(Pair<First, Second> o) {
        int res = this.first.compareTo(o.first);
        if (res==0) res = this.second.compareTo(o.second);
        // TODO Auto-generated method stub
        return res;
    }
}