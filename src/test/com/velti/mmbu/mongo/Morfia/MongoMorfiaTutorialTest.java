package com.velti.mmbu.mongo.Morfia;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.velti.mmbu.mongo.domain.MyEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/** @author gvlachogiannis */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/appllicationContext.xml")
public class MongoMorfiaTutorialTest {

    private static final String DB_NAME = "cmpro";

    private AdvancedDatastore ds;
    private DB db;

    @Autowired
    private Mongo mongo;

    @Autowired
    private Morphia morphia;

    @Before
    public void setup() {
        db = mongo.getDB(DB_NAME);
        morphia.map(MyEntity.class);
        ds = (AdvancedDatastore) morphia.createDatastore(mongo, "cmpro");
        ds.ensureIndexes();
    }



    @Test
    public void testMongoMorfia() throws Exception {

        MyEntity e1 = new MyEntity();
        e1.setName("George");
        e1.setCounter(2);

        ds.save(e1);
        MyEntity e2 = new MyEntity();
        e2.setName("Kostas");
        e1.setCounter(1);
        ds.save(e2);

        //Get all the elements
        System.out.println("USE FIND FOR GETTING ALL THE ELEMENTS");
        List myEntitiesList = ds.find(MyEntity.class).asList();
        System.out.println("All elements");
        printList(myEntitiesList);

        //Use some criteria for getiting specific results
        System.out.println("USE FIND WITH counter >= 2");
        myEntitiesList= ds.find(MyEntity.class, "counter >=", 2).asList();
        printList(myEntitiesList);


        //Use query filter


        System.out.println("USE QUERY FILTER WITH STRING OPERATIONS");
        Query query= ds.createQuery(MyEntity.class).filter("counter >", 2);
        myEntitiesList= query.asList();
        printList(myEntitiesList);

        System.out.println("USE QUERY FILTER WITH METHOD CALL");
        Query query2= ds.createQuery(MyEntity.class).field("counter").greaterThanOrEq(2);
        myEntitiesList= query2.asList();
        printList(myEntitiesList);

        System.out.println("USE QUERY FILTER WITH Fluent Interface");
        Query query3= ds.createQuery(MyEntity.class);
        query3.field("counter").greaterThanOrEq(2);
        myEntitiesList= query3.asList();
        printList(myEntitiesList);


        //Drop previous
        dropMongoEntities();
    }


    private void dropMongoEntities() {
        DBCollection collection = db.getCollection("myEntities");
        collection.drop();
    }

    private void printList(List<MyEntity> myEntityList) {

        for (MyEntity myEntityLoad : myEntityList) {
            System.out.println(myEntityLoad);
            System.out.println(myEntityLoad.getId());
            System.out.println(myEntityLoad.getName());
            System.out.println(myEntityLoad.getCounter());
        }
    }
}
