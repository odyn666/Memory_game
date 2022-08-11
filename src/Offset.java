public class Offset
    {
    private int largest;
    private int smallest;

    public Offset(int largest, int smallest) {
        assert smallest <= largest;

        this.largest = largest;
        this.smallest = smallest;
    }

    public int getLargest() {
        return largest;
    }

    public int getSmallest() {
        return smallest;
    }

    public int getDiff() {
        return largest - smallest;
    }
}
