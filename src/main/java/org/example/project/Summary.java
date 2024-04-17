package org.example.project;

/**
 * @author yunrui huang
 * @update 04/12/2024
 * Represents a summary of transaction amounts
 */
public class Summary {
    private double in;
    private double out;
    private double total;

    /**
     * Constructs a Summary object with initial values of 0.
     */
    public Summary(){
        this(0,0,0);
    }

    /**
     * Constructs a Summary object with the specified amounts.
     * @param in
     * Total amount for 'in' transactions.
     * @param out
     * Total amount for 'out' transactions.
     * @param total
     * Net total (in - out).
     */
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
