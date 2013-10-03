package com.velti.mmbu.mongo;

import com.velti.mmbu.mongo.categories.PerformanceTests;
import com.velti.mmbu.mongo.domain.MyEntity;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.util.StopWatch;

import java.util.List;

/** @author gvlachogiannis */
@Category(PerformanceTests.class)
public class MorfiaBasicStressTest {


    @Test
    public void testMorfiaInsertWithoutBatching() throws Exception {


        Datastore ds = new Morphia().createDatastore("myDB"); //best to use (Mongo, String) method, where Mongo is a singleton.

        //at application start map classes before calling with morphia map* methods
        ds.ensureIndexes(); //creates indexes from @Index annotations in your entities
        ds.ensureCaps(); //creates capped collections from @Entity


        int numberOfElements=10000;
        System.out.println("Inserting "+ numberOfElements+ " numberOfElements");

        StopWatch watch = new StopWatch();
        watch.start();
        for(int i=0; i < numberOfElements; i++){
             MyEntity myEntity= new MyEntity();
              myEntity.setName(String.valueOf(i));
              ds.save(myEntity);
        }
        watch.stop();
        System.out.println("Insertion of data took:"+watch.getLastTaskTimeMillis()+" milliseconds for inserting :"+numberOfElements+" numberOfElements");
        System.out.println("Throughput:"+numberOfElements*1000/watch.getLastTaskTimeMillis()+" numberOfElements/sec :");

        //Querying
//        List<MyEntity> myEntityList = ds.find(MyEntity.class).asList();
//        printList(myEntityList);

        System.out.println("Deleting list");
        watch.start();
        ds.delete(ds.createQuery(MyEntity.class));
        watch.stop();
        System.out.println("Deletion of data took:"+watch.getLastTaskTimeMillis()+" milliseconds for deleting :"+numberOfElements+" numberOfElements");
        System.out.println("Throughput:"+numberOfElements*1000/watch.getLastTaskTimeMillis()+" numberOfElements/sec :");


    }


}
