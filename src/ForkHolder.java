public class ForkHolder {

    private int holder;
    public static int NO_HOLDER = -1;

    public ForkHolder() {
        this.holder = NO_HOLDER;
    }

    public int get() {
        return holder;
    }

    public void set(int holder) {
        this.holder = holder;
    }

    @Override
    public String toString() {
        return Integer.toString(holder);
    }
}
