package com.velti.mmbu.mongo.dao;

import com.mongodb.DBObject;
import com.velti.mmbu.mongo.domain.UserContext;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/** @author gvlachogiannis */
public interface UserContextDao {


    DBObject findUserByNumber(String number);

    UserContext retrieveUser(DBObject dbObject);


}
