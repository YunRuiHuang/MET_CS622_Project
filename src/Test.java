import java.sql.*;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        test.todo();
    }

    public void todo(){
        Backend testBackend = new Backend();
//        testAdd(testBackend);
        testQuery(testBackend);
        testSummary(testBackend);
//        testEdit(testBackend);
//        testSort(testBackend);

    }

    public void testQuery(Backend testBackend){
        LocalDateTime dateTime = LocalDateTime.of(2024, 4, 11, 20, 0);
        Timestamp time = Timestamp.valueOf(dateTime);
        Data[] array = testBackend.query(null,time,null);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    public void testEdit(Backend testBackend){
        String type = "out";
        String title = "test edit";
        Timestamp time = new Timestamp(System.currentTimeMillis());
        double amount = 1145.14;
        String comment = "comment";
        testBackend.edit(311,type,title,time,amount,comment);
    }

    public void testSort(Backend testBackend){
        Data[] array = testBackend.sort(3,true);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    public void testAdd(Backend testBackend){
        for (int i = 0; i < 10; i++) {
            String type = ((i%2)==1) ? "in" : "out";
            String title = "test title " + i;
            LocalDateTime startOfMonth = LocalDateTime.of(2024, 1+i, 1, 0, 0);
            Timestamp time = Timestamp.valueOf(startOfMonth);
            double amount = 1234.5 * (1+i);
            String comment = "comment " + i;
            testBackend.add(type,title,time,amount,comment);
        }
        Data[] array = testBackend.query(null,null,null);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

    public void testDelete(Backend testBackend){
        testBackend.delete(401);
    }

    public void testSummary(Backend testBackend){
        System.out.println(testBackend.summary());
        System.out.println(testBackend.summaryByYear(2021));
        System.out.println(testBackend.summaryByYear(2024));
        for (int i = 1; i < 13; i++) {
            System.out.println(testBackend.summaryByMonth(2024,i));
        }
    }


    public void todo2(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby:project;user=user;password=password");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Data");

            // Process the results
            while (resultSet.next()) {
                // Retrieve data from the result set for each column
                int id = resultSet.getInt("ID");
                String type = resultSet.getString("Type");
                String title = resultSet.getString("Title");
                Timestamp time = resultSet.getTimestamp("Time");
                double amount = resultSet.getDouble("Amount");
                String comment = resultSet.getString("Comment");

                // Print out the values
                System.out.println("ID: " + id);
                System.out.println("Type: " + type);
                System.out.println("Title: " + title);
                System.out.println("Time: " + time);
                System.out.println("Amount: " + amount);
                System.out.println("Comment: " + comment);
                System.out.println(); // Add a blank line for readability
            }


            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
