package com.example;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import com.example.morphia.entities.*;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;

import java.util.ArrayList;
import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.eq;


public class DBConnection {

    Datastore datastore;

    public DBConnection(){
        String uri = "mongodb+srv://PICoursework:pi-coursework-20@picluster.6cnum4w.mongodb.net/?retryWrites=true&w=majority&appName=PICluster";

        // Define a datastore that will connect to the database
        datastore = Morphia.createDatastore(MongoClients.create(uri), "Main");

        datastore.getMapper().mapPackage("com.mongodb.morphia.entities");
        datastore.ensureIndexes();
    }

    //Saves or updates an entity
    public <T> void save(T item){
        datastore.save(item);
    }

    //Deletes an entity
    public <T> void delete(T item){
        datastore.delete(item);
    }

    //Returns all users
    public List<User> getAllUsers(){
        return datastore.find(User.class)
                .iterator(new FindOptions().sort(Sort.descending("userID")))
                .toList();
    }

    public User getUser(String userID){
        return datastore.find(User.class).filter(eq("userID",userID)).first();
    }


    public static void main(String[] args) {
        new DBConnection();
    }

}
