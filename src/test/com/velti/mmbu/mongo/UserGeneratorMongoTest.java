package com.velti.mmbu.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import com.velti.mmbu.mongo.domain.E164Number;
import com.velti.mmbu.mongo.domain.UserContext;
import com.velti.mmbu.mongo.domain.UserOptState;
import com.velti.mmbu.mongo.domain.UserPropertyName;
import net._01001111.text.LoremIpsum;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * User: flioutsis
 * Date: 9/27/13
 * Time: 12:00 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/appllicationContext.xml")
public class UserGeneratorMongoTest {

//    private static Logger logger = Logger.getLogger(UserGeneratorMongoTest.class); // Logger.
    private static Logger logger = LoggerFactory.getLogger(UserGeneratorMongoTest.class);


    private static final String DB_NAME = "cmpro";

    private static int batchSize = 100;
    private static final int iterations = 1000;

    Random random = new Random();
    RandomData randomData = new RandomDataImpl();
    private static final long NUMBER_MIN = 14045551212L;
    private static final long NUMBER_MAX = 17705551212L;

    private LoremIpsum jlorem = new LoremIpsum();
    private UserPropertyName[] names;

    private AdvancedDatastore ds;
    private DB db;

    @Autowired
    private Mongo mongo;

    @Autowired
    private Morphia morphia;

    @Before
    public void setup() {
        db = mongo.getDB(DB_NAME);
        morphia.map(UserContext.class);
        ds = (AdvancedDatastore) morphia.createDatastore(mongo, "cmpro");
        ds.ensureIndexes();

        names = new UserPropertyName[]{UserPropertyName.FIST_NAME, UserPropertyName.LAST_NAME, UserPropertyName.ADDRESS,
                                       UserPropertyName.AGE};
    }

    @Test
    public void testLogger() throws Exception {

         logger.info("test logger");
    }

    @Test
    public void dropMongoUsers() {
        DBCollection collection = db.getCollection("users");
        collection.drop();
    }


    @Test
    public void insertMongoUsersWithBatch() {
        DBCollection collection = db.getCollection("users");
        collection.drop();


        long start = System.currentTimeMillis();

        for (int i = 1; i <= batchSize; i++) {
            List<UserContext> users = generateRandomUsers(iterations*i);
            logger.info("Created " + iterations*i + " rows");

            try {
                ds.insert(users, WriteConcern.SAFE);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
//            logger.info("Inserted " + collection.count() + " rows");
            logger.info("Processed " + iterations * i + " users");
        }

        long timeSpent= System.currentTimeMillis() - start;
        logger.info("Time taken batch morphia: " + timeSpent + "ms");
        logger.info("Throughput: " + batchSize*iterations*1000/ timeSpent + " users/seconds");

        logger.info("Inserted " + collection.count() + " rows");
    }


    @Test
    public void insertMongoUsersWithoutBatch() {
        DBCollection collection = db.getCollection("users");
        collection.drop();

        logger.info("Database users dropped");

        long start = System.currentTimeMillis();
        int batchSize = 100;
        for (int i = 1; i <= batchSize; i++) {
            List<UserContext> users = generateRandomUsers(iterations*i);
            logger.info("Created " + collection.count() + " rows");

            for (UserContext userContext: users){
                ds.save(userContext);
            }

            //            logger.info("Inserted " + collection.count() + " rows");
            logger.info("Processed " + iterations * i + " users");
        }

        long timeSpent= System.currentTimeMillis() - start;
        logger.info("Time taken batch morphia: " + timeSpent + "ms");
        logger.info("Throughput: " + batchSize*iterations/ timeSpent + " users/seconds");

        logger.info("Inserted " + collection.count() + " rows");
    }


    @Test
    public void dropBulkCollection() {
        DBCollection c = db.getCollection("bulk");
        c.drop();
    }

    private E164Number getRandomNumber(long min, long max) {
        E164Number number = null;
        String msisdn = String.valueOf(randomData.nextLong(min, max));
        try {
            number = new E164Number(msisdn, true);
        } catch (NumberFormatException e) {
        }
        return number;
    }

    private UserOptState getRandomOptState() {
        double r = random.nextDouble();
        // 10% Blocked, 10% Deactivated, 20% Opted-out, 60% Opted-in
        if (r < 0.1) {
            return UserOptState.BLOCKED;
        } else if (r < 0.2) {
            return UserOptState.DEACTIVATED;
        } else if (r < 0.4) {
            return UserOptState.OPTED_OUT;
        } else {
            return UserOptState.OPTED_IN;
        }
    }

    private List<UserContext> generateRandomUsers(int iterations) {
        List<UserContext> users = new ArrayList<UserContext>();
        int invalidNumbers = 0;
        for (int i = 0; i < iterations; i++) {
            UserContext user = new UserContext();
            E164Number number = getRandomNumber(NUMBER_MIN, NUMBER_MAX);
            if (number == null) {
                invalidNumbers++;
                continue;
            }
            user.setMsisdn(number.getNumber());
            user.setCountryCode(number.getCountryCode());
            user.setNpa(number.getNPA());
            user.setNxx(number.getNXX());
            int listN = randomData.nextInt(1, 4);
            Map<Long, String> listMap = new HashMap<Long, String>();
            for (int j = 0; j < listN; j++) {
                listMap.put((long) randomData.nextInt(10000, 10100), getRandomOptState().name());
            }
            user.setLists(listMap);
            int propertyN = randomData.nextInt(0, 2);
            Map<String, String> propertyMap = new HashMap<String, String>();
            for (int p = 0; p < propertyN; p++) {
                propertyMap.put(names[randomData.nextInt(0, 3)].name(), jlorem.randomWord());
            }
            user.setUserProperties(propertyMap);
            users.add(user);
        }
        logger.info("Invalid E164 numbers: " + invalidNumbers);
        return users;
    }


}
