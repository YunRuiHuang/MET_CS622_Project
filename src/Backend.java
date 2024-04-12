import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.sql.*;

public class Backend implements BackendInterface {
    private ArrayList<Data> dataList;
    private Connection connection;


    public Backend(){
        this.dataList = new ArrayList<>();
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        query("",null,null);
    }

    @Override
    public Data[] sort(int type, Boolean isReversed){
        this.dataList.sort(new DataComparator(type,isReversed));
        return this.dataList.toArray(new Data[this.dataList.size()]);
    }

    @Override
    public Data[] query(String billType, Timestamp startTime, Timestamp endTime) {
        //TODO send a query to database
        try{
            if (connection == null){
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
            }
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Data");
            this.dataList = new ArrayList<>();
            // Process the results
            while (resultSet.next()) {
                // Retrieve data from the result set for each column
                int id = resultSet.getInt("ID");
                String type = resultSet.getString("Type");
                String title = resultSet.getString("Title");
                Timestamp time = resultSet.getTimestamp("Time");
                double amount = resultSet.getDouble("Amount");
                String comment = resultSet.getString("Comment");

                this.dataList.add(new Data(id,type,title,time,amount,comment));
            }
            resultSet.close();
            statement.close();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this.dataList.toArray(new Data[this.dataList.size()]);
    }

    @Override
    public Boolean delete(int id) {
        //TODO delete from database
        try {

            Statement statement = connection.createStatement();

            String sql = "DELETE FROM Data WHERE id = " + id;
            int rowsAffected = statement.executeUpdate(sql);

            if(rowsAffected>0){
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getId()==id){
                        dataList.remove(i);
                        return true;
                    }
                }
            }
            statement.close();
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Boolean add(String type, String title, Timestamp time, double amount, String comment) {
        try{
            String sql = "INSERT INTO Data (Type, Title, Time, Amount, Comment) Values (";
            sql += "'"+type+"',";
            sql += "'"+title+"',";
            sql += "'"+time+"',";
            sql += ""+amount+",";
            sql += "'"+comment+"')";
            PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int rowsAffected = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(rowsAffected>0){
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    this.dataList.add(new Data(id,type,title,time,amount,comment));
                    statement.close();
                    return true;
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean edit(int id, String type, String title, Timestamp time, double amount, String comment) {
        try{
            String sql = "UPDATE Data SET ";
            sql += "type ='"+type+"',";
            sql += "title ='"+title+"',";
            sql += "time ='"+time+"',";
            sql += "amount ="+amount+",";
            sql += "comment = '"+comment+"' ";
            sql += "where id = " + id;
            Statement statement = this.connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            statement.close();
            if(rowsAffected>0){
                for (int i = 0; i < this.dataList.size(); i++) {
                    if(dataList.get(i).getId()==id){
                        dataList.set(i,new Data(id,type,title,time,amount,comment));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Summary summary() {
        double in = 0;
        double out = 0;
        for (int i = 0; i < this.dataList.size(); i++) {
            Data data = this.dataList.get(i);
            if(data.getType().compareToIgnoreCase("in") == 0){
                in += data.getAmount();
            }else{
                out += data.getAmount();
            }
        }

        return new Summary(in,out,in-out);
    }

    @Override
    public Summary summaryByYear(int year) {
        //TODO send query of year amount add up
        return summary();
    }

    @Override
    public Summary summaryByMonth(int month) {
        //TODO send query of month amount add up
        return summary();
    }
}
