import java.util.Date;

interface Backend {
    public Data[] query(String type, Date startTime, Data endTime);

    public Summary summary();

    public Boolean delete(int id);

    public Boolean add(String type,String title,Date time,float amount,String comment);

    public Boolean edit(int id,String type,String title,Date time,float amount,String comment);

    public Summary summaryByYear(int year);

    public Summary summaryByMonth(int month);
}
