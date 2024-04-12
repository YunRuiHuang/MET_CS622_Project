import java.sql.Timestamp;
import java.util.Date;

interface BackendInterface {
    public Data[] query(String type, Timestamp  startTime, Timestamp endTime);

    public Data[] sort(int type, Boolean isReversed);

    public Summary summary();

    public Boolean delete(int id);

    public Boolean add(String type,String title,Timestamp  time,double amount,String comment);

    public Boolean edit(int id,String type,String title,Timestamp  time,double amount,String comment);

    public Summary summaryByYear(int year);

    public Summary summaryByMonth(int month);
}
