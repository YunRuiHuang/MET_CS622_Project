import java.sql.Timestamp;
import java.util.Date;

public class Data {
    private int id;
    private String type;
    private String title;
    private Timestamp time;
    private double amount;
    private String comment;

    /**
     *
     * @param id
     * @param type
     * @param title
     * @param time
     * @param amount
     * @param comment
     */
    public Data(int id, String type, String title, Timestamp time, double amount, String comment) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.time = time;
        this.amount = amount;
        this.comment = comment;
    }

    public Data(){
        this(0,"","",null,0,"");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp  time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                '}';
    }
}
