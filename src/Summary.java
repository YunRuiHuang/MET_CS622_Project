public class Summary {
    private double in;
    private double out;
    private double total;

    public Summary(double in, double out, double total) {
        this.in = in;
        this.out = out;
        this.total = total;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getOut() {
        return out;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "in=" + in +
                ", out=" + out +
                ", total=" + total +
                '}';
    }
}
