import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;

public class ListDatabaseNames {
    public static void main(String[] args) {
        // Replace the connection string with your MongoDB cluster's connection string
        String connectionString = "mongodb+srv://PICoursework:pi-coursework-20@picluster.6cnum4w.mongodb.net/?retryWrites=true&w=majority&appName=PICluster";
        MongoClient mongoClient = MongoClients.create(connectionString);

        // List all database names
        MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
        for (String dbName : databaseNames) {
            System.out.println("Database: " + dbName);
        }

        // Close the MongoClient
        mongoClient.close();
    }
}
