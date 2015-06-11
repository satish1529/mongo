package com.mongo.tutorial.mongo.connection;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class DBOperations {

	
	public static void main(String[] args) {
		//insert();
		//update();
		//find();
		//findAndRemove(new DBConnection().getDB("students"));;
		findAndRemovew3(new DBConnection().getDB("school"));;
	}
	
	 public static MongoDatabase getDB(String dbName)
	 {
		 return new DBConnection().getDB("testdb");
	 }
	 
	 private static void update()
	 {
		 MongoDatabase md = getDB("testdb");
		// ToAddNewObjectToNestedObject(md, null);
		// AddLikesField(md,null);
		// UpdateEmbeddedField(md,null);
		// UpdateEmbeddedFieldBasedOnCondition(md,null);
		 incrementValue(md,null);
	 }
	
	 
	 private static void find()
	 {
		 MongoDatabase md = getDB("testdb");
		find(md);
	 }
	 
	private static void insert()
	{
		int count=5;
		MongoDatabase md = getDB("testdb");
		
		Post p = createPost("India won in semifinal ",count++);
		insert(md,p);
		
		p = createPost("starting office",count++);
		insert(md,p);
		
		p = createPost("going USA",count++);
		insert(md,p);
	}
	
	private static Post createPost(String name,int id)
	{
		Post p = new Post(name,id);
		List<Comments> listOFComments = new ArrayList<>();
		Comments c = new Comments();
		c.setComment("nice"+id);
		c.setUser("anonymous"+id);
		c.setCommentID(1+id);
		listOFComments.add(c);
		
		id++;
		
		c = new Comments();
		c.setComment("nice"+id);
		c.setUser("anonymous"+id);
		c.setCommentID(1+id);
		
		listOFComments.add(c);
		
		
		p.setComments(listOFComments);
		return p;
	}
	
	private static void insert(MongoDatabase md,Post p)
	{
		BasicDBObject bdo = new BasicDBObject("_id",p.getId()).append("post", p.getName());

		//List<BasicDBObject> listOFComments = new ArrayList<>();
		
		BasicDBList listOFComments = new BasicDBList();
		
		for(Comments c : p.getComments())
		{
			BasicDBObject c1 = new BasicDBObject("commentID",c.getCommentID()).append("comment", c.getComment()).append("user", c.getUser());
			listOFComments.add(c1);
		}
		bdo.append("comments", listOFComments);
		
		Document d = new Document(bdo);
		MongoCollection<Document> mc = md.getCollection("posts");
		
		mc.insertOne(d);
		
	}
	
	private static void insert(MongoDatabase md)
	{
		
		Post s = new Post("satish",25);
		BasicDBObject bdo = new BasicDBObject("name","satish").append("age", 12);
		
		Document d = new Document("s",bdo);
		MongoCollection<Document> mc = md.getCollection("tutorial");
		
		mc.insertOne(d);
	}
	
	private static void ToAddNewObjectToNestedObject(MongoDatabase md,Post p)
	{
		BasicDBObject bdo = new BasicDBObject("_id",2);
		
		BasicDBObject c1 = new BasicDBObject("commentID",7).append("comment","no").append("user", "testing");
		
		BasicDBObject base = new BasicDBObject();
		//$push is used to add addiontal object
		base.append("$push", new BasicDBObject().append("comments",c1) );
			
		MongoCollection<Document> mc = md.getCollection("posts");
		
		
		mc.updateOne(bdo, base);
		
	}
	
	private static void AddLikesNewField(MongoDatabase md,Post p)
	{
		BasicDBObject bdo = new BasicDBObject("_id",2);
		
		
		BasicDBObject base = new BasicDBObject();
		//$set is used to update value , if key is not present it adds new key
		base.append("$set", new BasicDBObject().append("likes",3) ).append("$currentDate", new BasicDBObject().append("lastModified",true));
			
		MongoCollection<Document> mc = md.getCollection("posts");
		
		
		mc.updateOne(bdo, base);
		
	}
	
	private static void UpdateEmbeddedField(MongoDatabase md,Post p)
	{
		BasicDBObject bdo = new BasicDBObject("_id",2).append("comments.commentID",3);
	
		BasicDBObject base = new BasicDBObject();
		//$set is used to update value , if key is not present it adds new key
		base.append("$set", new BasicDBObject().append("comments.$.likes",3) );
			
		MongoCollection<Document> mc = md.getCollection("posts");
		
		
		mc.updateOne(bdo, base);
		
	}
	
	private static void UpdateEmbeddedFieldBasedOnCondition(MongoDatabase md,Post p)
	{
		//BasicDBObject bdo = new BasicDBObject("_id",7).append("comments.likes", "$elemMatch:{ $gt: 5, $lt: 9 }");
		BasicDBObject bdo = new BasicDBObject().append("comments",new BasicDBObject("$elemMatch",new BasicDBObject("likes",new BasicDBObject("$gt" ,5).append("$lt", 8))));
			
		BasicDBObject base = new BasicDBObject();
		//$set is used to update value , if key is not present it adds new key
		base.append("$set", new BasicDBObject().append("comments.$.shares",1) );
			
		MongoCollection<Document> mc = md.getCollection("posts");
		
			
		mc.updateOne(bdo, base);
		
	}
	
	
	private static void incrementValue(MongoDatabase md,Post p)
	{
		//BasicDBObject bdo = new BasicDBObject("_id",7).append("comments.likes", "$elemMatch:{ $gt: 5, $lt: 9 }");
		BasicDBObject bdo = new BasicDBObject().append("comments",new BasicDBObject("$elemMatch",new BasicDBObject("likes",new BasicDBObject("$gt" ,5).append("$lt", 8))));
			
		BasicDBObject base = new BasicDBObject();
		base.append("$inc", new BasicDBObject().append("comments.$.shares",1) );
			
		MongoCollection<Document> mc = md.getCollection("posts");
		
			
		mc.updateOne(bdo, base);
		
	}
	
	private static void find(MongoDatabase md)
	{
		//greater operator
		/*BasicDBObject elemMatch = new BasicDBObject();
		elemMatch.append("$gt", 5);
		
		BasicDBObject query = new BasicDBObject();
		query.put("_id",7);	
		query.append("comments.likes",elemMatch);
		
		MongoCollection<Document> mc = md.getCollection("posts");
		FindIterable<Document> fi= mc.find(query);
		
		MongoCursor<Document> mcursor = fi.iterator();
		System.out.println(mcursor.hasNext());
		while(mcursor.hasNext())
		{
			Document d = mcursor.next();
		 System.out.println(d);
		}*/
		
		/**
		 * elemMatch 
		 */
		BasicDBObject condition = new BasicDBObject();
		condition.append("likes",new BasicDBObject("$lt", 7).append("$gt", 1));
		//condition.append("comment","nice4");
		
		BasicDBObject elemMatch = new BasicDBObject();
		elemMatch.append("$elemMatch", condition);
		
		BasicDBObject query = new BasicDBObject();
	//	query.put("_id",7);	
		query.append("comments",elemMatch);
		
		MongoCollection<Document> mc = md.getCollection("posts");
		FindIterable<Document> fi= mc.find(query);
		
		MongoCursor<Document> mcursor = fi.iterator();
	//	System.out.println(mcursor.hasNext());
		while(mcursor.hasNext())
		{
			Document d = mcursor.next();
		 System.out.println(d);
		}
		
		
	}
	
	private static void findAndRemove(MongoDatabase md)
	{
		BasicDBObject condition = new BasicDBObject();
		condition.append("type","homework");
		MongoCollection<Document> mc = md.getCollection("grades");
		
		BasicDBObject studentSort = new BasicDBObject();
		studentSort.append("student_id",1);
		studentSort.append("score",1);
		
		
		
		
		FindIterable<Document> fi= mc.find(condition).sort(studentSort);//.sort(scoreSort);
	int count =0;
	int dcount = 0;
		MongoCursor<Document> mcursor = fi.iterator();
	//	System.out.println(mcursor.hasNext());
		double id = -1;
		
		while(mcursor.hasNext())
		{
			Document d = mcursor.next();
			
		// System.out.println(d);
			double tempStudentId = d.getDouble("student_id");
			double tempScore = d.getDouble("score");
		 System.out.print(tempStudentId+" "+tempScore);
		 if(id!=tempStudentId)
		 {
			 System.out.print("   delete : "+tempStudentId +" , "+tempScore);
			/* BasicDBObject dcondition = new BasicDBObject();
			 dcondition.append("type","homework");
			 dcondition.append("student_id",tempStudentId);
			 dcondition.append("score",tempScore);
			 
			 mc.deleteOne(dcondition);*/
				
				
			 dcount++;
		 }
		 System.out.println();
		 id = tempStudentId;
		 count++;
		}
		System.out.println(count+"   "+dcount);
	}
	
	private static void findAndRemovew3(MongoDatabase md)
	{
		
		MongoCollection<Document> mc = md.getCollection("students");
		

		
		
		
		
		FindIterable<Document> fi= mc.find();//find(condition).sort(studentSort);//.sort(scoreSort);
	int count =0;
	int dcount = 0;
		MongoCursor<Document> mcursor = fi.iterator();
	//	System.out.println(mcursor.hasNext());
		double id = -1;
		
		while(mcursor.hasNext())
		{
			Document d = mcursor.next();
			
		// System.out.println(d);
			double tempStudentId = d.getDouble("_id");
			List tempScore = (ArrayList)d.get("scores");
			//System.out.print(tempStudentId+" "+tempScore);
			double hwscore = -1;
			for(Object o :tempScore)
			{
				System.out.println(o);
				Document score = (Document)o;
				System.out.println(score.get("type")+"  "+ score.get("score"));
				if(score.get("type").equals("homework"))
				{
					double tempscore = (double)score.get("score");
					if(hwscore==-1)
						hwscore = tempscore;
					else if(hwscore>tempscore)
						hwscore = tempscore;
				}
			}
		 
			Document d1 = new Document();
			d1.put("type", "homework");
			d1.put("score", hwscore);
			
			BasicDBObject condition = new BasicDBObject();
			condition.append("$pull",new BasicDBObject("scores", d1 ));
			
			 BasicDBObject dcondition = new BasicDBObject();
			 dcondition.append("_id",tempStudentId);
			 
			 mc.updateOne(dcondition, condition);
			 dcount++;
		 /*if(id!=tempStudentId)
		 {
			 System.out.print("   delete : "+tempStudentId +" , "+tempScore);
			 BasicDBObject dcondition = new BasicDBObject();
			 dcondition.append("type","homework");
			 dcondition.append("student_id",tempStudentId);
			 dcondition.append("score",tempScore);
			 
			 mc.deleteOne(dcondition);
				
				
			 dcount++;
		 }*/
		 System.out.println();
		 
		 count++;
		}
		System.out.println(count+"   "+dcount);
	}
}
