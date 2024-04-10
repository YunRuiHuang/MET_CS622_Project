import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class FakeData {
    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

    private ArrayList<Data> dataList = new ArrayList<>();

    public FakeData() {
        for (int i = 0; i < 10; i++) {
            int id = i;
            String type = ((i%2)==1) ? "in" : "out";
            String title = "test title " + i;
            Date time = new GregorianCalendar(2022, 2, 11+i).getTime();
            double amount = 1234.5 * (1+i);
            String comment = "comment " + i;
            this.dataList.add(new Data(id,type,title,time,amount,comment));
        }
    }
}
