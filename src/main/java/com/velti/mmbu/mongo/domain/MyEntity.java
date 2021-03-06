package com.velti.mmbu.mongo.domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/** @author gvlachogiannis */
@Entity(value = "myEntities", noClassnameStored = true)
public class MyEntity {

    @Id
    private ObjectId id;

    private String name;

    private int counter;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
