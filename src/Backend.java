import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.sql.*;

/**
 * @author yunrui huang
 * @update 04/12/2024
 * Backend class implements BackendInterface to provide functionalities for data management
 */

public class Backend implements BackendInterface {
    private ArrayList<Data> dataList;
    private Connection connection;


    /**
     * Constructor for Backend class.
     * Initializes dataList and establishes connection to the database.
     * Retrieves data from the database upon initialization.
     */
    public Backend(){
        this.dataList = new ArrayList<>();
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        query(null,null,null);
    }

    /**
     * Sorts the dataList based on the specified type and order.
     * @param type
     * The type of sorting to be performed.
     * there are 6 choose to sort data
     * 0:id, 1:type, 2:title, 3:time, 4:amount, 5:comment
     * @param isReversed
     * Boolean indicating whether sorting should be reversed or not.
     * @return
     * An array of sorted Data objects.
     */
    @Override
    public Data[] sort(int type, Boolean isReversed){
        this.dataList.sort(new DataComparator(type,isReversed));
        return this.dataList.toArray(new Data[this.dataList.size()]);
    }

    /**
     * Retrieves data from the database based on the specified criteria.
     * If no criteria are provided, fetches the latest 20 rows.
     * @param billType
     * The type of bill to filter by. Can be null to ignore.
     * @param startTime
     * The start time to filter by. Can be null to ignore.
     * @param endTime
     * The end time to filter by. Can be null to ignore.
     * @return
     * An array of Data objects matching the specified criteria.
     */
    @Override
    public Data[] query(String billType, Timestamp startTime, Timestamp endTime) {
        boolean hasWhere = false;
        String sql = "SELECT * FROM Data ";
        if(billType != null){
            sql += "where type = '" + billType + "' ";
            hasWhere = true;
        }
        if (startTime != null){
            if (hasWhere){
                sql += "AND time >= '" + startTime + "' ";
            }else{
                sql += "where time >= '" + startTime + "' ";
                hasWhere = true;
            }
        }
        if(endTime != null){
            if (hasWhere){
                sql += "AND time <= '" + endTime + "' ";
            }else{
                sql += "where time <= '" + endTime + "' ";
                hasWhere = true;
            }
        }
        if(!hasWhere){
            sql += "Order by id desc FETCH FIRST 20 ROWS ONLY";
        }


        //send a query to database
        try{
            if (connection == null){
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
            }
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
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

    /**
     * Deletes a data entry with the specified ID from the database and updates the dataList accordingly.
     * @param id
     * The ID of the data entry to be deleted.
     * @return
     * True if the deletion was successful, false otherwise.
     */
    @Override
    public Boolean delete(int id) {
        //delete from database
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

    /**
     * Adds a new data entry to the database and updates the dataList accordingly.
     * @param type
     * The type of the data entry.
     * @param title
     * The title of the data entry. (Maximum 255 char)
     * @param time
     * The timestamp of the data entry.
     * @param amount
     * The amount of the data entry.
     * @param comment
     * Any additional comment associated with the data entry. (Maximum 1000 char)
     * @return
     * True if the addition was successful, false otherwise.
     */
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

    /**
     * Edits an existing data entry in the database and updates the dataList accordingly.
     * @param id
     * The ID of the data entry to be edited.
     * @param type
     * The new type of the data entry.
     * @param title
     * The new title of the data entry.
     * @param time
     * The new timestamp of the data entry.
     * @param amount
     * The new amount of the data entry.
     * @param comment
     * The new comment associated with the data entry.
     * @return
     * True if the editing was successful, false otherwise.
     */
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

    /**
     * Generates a summary of the total amount for each type of transaction (in/out).
     * @return
     * A Summary object containing the total amount for each type and the net total.
     */
    @Override
    public Summary summary() {
        Summary summary = new Summary();
        try{
            String sql = "SELECT type, SUM(amount) AS total_amount From Data Group By type";
            if (connection == null){
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
            }
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            this.dataList = new ArrayList<>();
            // Process the results
            while (resultSet.next()) {
                // Retrieve data from the result set for each column
                String type = resultSet.getString("Type");
                double amount = resultSet.getDouble("total_amount");
                if(type.compareToIgnoreCase("in") == 0){
                    summary.setIn(amount);
                }else if(type.compareToIgnoreCase("out") == 0){
                    summary.setOut(amount);
                }

            }
            resultSet.close();
            statement.close();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        summary.setTotal(summary.getIn()-summary.getOut());
        return summary;
    }

    /**
     * Generates a summary of the total amount for each type of transaction (in/out) within the specified year.
     * @param year
     * The year for which the summary is to be generated.
     * @return
     * A Summary object containing the total amount for each type and the net total for the specified year.
     */
    @Override
    public Summary summaryByYear(int year) {
        //send query of year amount add up
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999999999);

        Summary summary = new Summary();
        try{
            String sql = "SELECT type, SUM(amount) AS total_amount " +
                    "From Data " +
                    "where time >= '" + Timestamp.valueOf(startOfYear) + "' " +
                    "And time <= '" + Timestamp.valueOf(endOfYear) + "' " +
                    "Group By type ";
            if (connection == null){
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
            }
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            this.dataList = new ArrayList<>();
            // Process the results
            while (resultSet.next()) {
                // Retrieve data from the result set for each column
                String type = resultSet.getString("Type");
                double amount = resultSet.getDouble("total_amount");
                if(type.compareToIgnoreCase("in") == 0){
                    summary.setIn(amount);
                }else if(type.compareToIgnoreCase("out") == 0){
                    summary.setOut(amount);
                }

            }
            resultSet.close();
            statement.close();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        summary.setTotal(summary.getIn()-summary.getOut());
        return summary;
    }

    /**
     * Generates a summary of the total amount for each type of transaction (in/out) within the specified month and year.
     * @param year
     * The year of the month for which the summary is to be generated.
     * @param month
     * The month for which the summary is to be generated.
     * @return
     * A Summary object containing the total amount for each type and the net total for the specified month.
     */
    @Override
    public Summary summaryByMonth(int year, int month) {
        //send query of month amount add up

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        Summary summary = new Summary();
        try{
            String sql = "SELECT type, SUM(amount) AS total_amount " +
                    "From Data " +
                    "where time >= '" + Timestamp.valueOf(startOfMonth) + "' " +
                    "And time <= '" + Timestamp.valueOf(endOfMonth) + "' " +
                    "Group By type ";
            if (connection == null){
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                this.connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
            }
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            this.dataList = new ArrayList<>();
            // Process the results
            while (resultSet.next()) {
                // Retrieve data from the result set for each column
                String type = resultSet.getString("Type");
                double amount = resultSet.getDouble("total_amount");
                if(type.compareToIgnoreCase("in") == 0){
                    summary.setIn(amount);
                }else if(type.compareToIgnoreCase("out") == 0){
                    summary.setOut(amount);
                }

            }
            resultSet.close();
            statement.close();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        summary.setTotal(summary.getIn()-summary.getOut());
        return summary;
    }
}
