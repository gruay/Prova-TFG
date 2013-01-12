package com.david;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	MongoClient m;
		try {
			m = new MongoClient();
	    	DB db = m.getDB("mydb");
	    	DBCollection coll = db.getCollection("testCollection");
	    	BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database").append("count", 1).append("info",  new BasicDBObject("x", 203).append("y", 102));
	    	coll.insert(doc);
	    	DBObject myDoc = coll.findOne();
	    	System.out.println(myDoc);
	    } catch (UnknownHostException e) {
			System.out.println("Peta tot1");
			e.printStackTrace();
			System.out.println("Peta tot2");
		}

    }
}
