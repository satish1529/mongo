package com.mongo.tutorial.mongo.connection;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;

/**
 * Hello world!
 *
 */
public class DBConnection 
{
    static volatile MongoClient mc = null;
    
    public MongoClient getMongoConnection()
    {
    	MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
    	MongoClientOptions options = builder.connectionsPerHost(50).build();
    	mc = new MongoClient("localhost",options);
    	return mc;
    }
    
    public MongoDatabase getDB(String dbName)
    {
    	if(mc==null)
    		mc = getMongoConnection();
    	return mc.getDatabase(dbName);
    }
    
    public static void main(String[] args) {
		System.out.println(new DBConnection().getMongoConnection());
	}
    
    
}
