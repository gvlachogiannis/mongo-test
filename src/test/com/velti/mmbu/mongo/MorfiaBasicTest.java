package com.velti.mmbu.mongo;

import com.velti.mmbu.mongo.domain.MyEntity;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

/** @author gvlachogiannis */
public class MorfiaBasicTest {

    @Test
    public void testName() throws Exception {

        Datastore ds = new Morphia().createDatastore("myDB"); //best to use (Mongo, String) method, where Mongo is a singleton.

        //at application start map classes before calling with morphia map* methods
        ds.ensureIndexes(); //creates indexes from @Index annotations in your entities
        ds.ensureCaps(); //creates capped collections from @Entity

        MyEntity myEntity = new MyEntity();
        myEntity.setName("George");
        ds.save(myEntity);

        MyEntity myEntity2 = new MyEntity();
        myEntity2.setName("Kostas");
        ds.save(myEntity2);

        //Querying
        List<MyEntity> myEntityList = ds.find(MyEntity.class).asList();
        printList(myEntityList);

        System.out.println("Deleting list");
        ds.delete(ds.createQuery(MyEntity.class));

        List<MyEntity> myEntityList2 = ds.find(MyEntity.class).asList();
        printList(myEntityList2);
    }



    void printList(List<MyEntity> myEntityList) {

        for (MyEntity myEntityLoad : myEntityList) {
            System.out.println(myEntityLoad);
            System.out.println(myEntityLoad.getId());
            System.out.println(myEntityLoad.getName());
        }
    }
}
