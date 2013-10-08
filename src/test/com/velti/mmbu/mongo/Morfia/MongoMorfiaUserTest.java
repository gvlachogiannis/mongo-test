package com.velti.mmbu.mongo.Morfia;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.velti.mmbu.mongo.domain.MyEntity;
import com.velti.mmbu.mongo.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/** @author gvlachogiannis */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/appllicationContext.xml")
public class MongoMorfiaUserTest {

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
        morphia.map(User.class);
        ds = (AdvancedDatastore) morphia.createDatastore(mongo, "cmpro");
        ds.ensureIndexes();
    }

    @Test
    public void testName() throws Exception {

        //login first time
        User user = new User();
        user.setName("George");
        long now = System.currentTimeMillis();
        user.setLastLogin(now);
        ds.save(user);

        //Print users
        List<User> userList = ds.find(User.class).filter("name", "George").asList();
        printList(userList);

        //Drop previous
        dropMongoUsers();
    }

    private void dropMongoUsers() {

        DBCollection collection = db.getCollection("usersTest");
        collection.drop();
    }

    private void printList(List<User> userList) {

        for (User user : userList) {
            System.out.println(user);
            System.out.println(user.getId());
            System.out.println(user.getName());
            System.out.println(user.getLastLogin());
        }
    }

    //    private Query<User> queryToFindMe() {
    //        return datastore.createQuery(User.class).field(Mapper.ID_KEY).equal(id);
    //    }
    //
    //    public void loggedIn() {
    //        long now = System.currentTimeMillis();
    //        UpdateOperations<User> ops = datastore.createUpdateOperations(User.class).set("lastLogin", now);
    //        ds.update(queryToFindMe(), ops);
    //        lastLogin = now;
    //    }
}
