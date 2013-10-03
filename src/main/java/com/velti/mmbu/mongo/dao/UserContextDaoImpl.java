package com.velti.mmbu.mongo.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.velti.mmbu.mongo.domain.UserContext;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * User: flioutsis
 * Date: 9/26/13
 * Time: 6:16 PM
 */
public class UserContextDaoImpl extends BasicDAO<UserContext, ObjectId> implements UserContextDao {

    private DB db;

    private Morphia morphia;

    private DBCollection collection;

    public UserContextDaoImpl(Mongo mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
        morphia.map(UserContext.class);
        ds.ensureIndexes();
        this.morphia = morphia;

        db = mongo.getDB(dbName);
        collection = db.getCollection("UserContext");
        collection.createIndex(new BasicDBObject("lists", 1));
        collection.createIndex(new BasicDBObject("userProperties", 1));
    }

    @Override
    public DBObject findUserByNumber(String number) {
        BasicDBObject query = new BasicDBObject();
        query.put("msisdn", Long.valueOf(number));
        return collection.findOne(query);
    }

    public UserContext retrieveUser(DBObject dbObject) {
        return morphia.fromDBObject(UserContext.class, dbObject);
    }


}