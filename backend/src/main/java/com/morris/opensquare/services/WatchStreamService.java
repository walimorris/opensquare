package com.morris.opensquare.services;

/**
 * The WatchStreamService utilizes MongoDB's {@link com.mongodb.client.ChangeStreamIterable}
 * and Change Data Capture (CDC) functionality to watch various collections within the
 * persistence layer.
 */
public interface WatchStreamService {

    /**
     * Watch storage layer Email Domain collection change stream using MongoDB's
     * {@link com.mongodb.client.ChangeStreamIterable}.
     */
    void watchEmailDomainChangeStream();

    /**
     * Watch storage layer MongoLogger collection change stream using MongoDB's
     * {@link com.mongodb.client.ChangeStreamIterable}.
     */
    void watchMongoLoggerChangeStream();

    /**
     * Watch storage layer GlobalNotifications collection change stream using MongoDB's
     * {@link com.mongodb.client.ChangeStreamIterable}.
     */
    void watchGlobalNotificationsChangeStream();

    /**
     * Watch all possible change streams.
     */
    void watchall();
}
