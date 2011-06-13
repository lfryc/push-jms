package org.richfaces.examples.push.jms;

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.TopicPublisher;

public class JMSHelper {
    /**
     * Clean up JMS producer objects. Typically used in a finally block.
     *
     * @param sender the sender
     * @param session the session (may be null)
     * @param connection the connection (may be null)
     */
    public static void close(TopicPublisher publisher, Session session, Connection connection) {
        if (publisher != null) {
            // noinspection EmptyCatchBlock
            try {
                publisher.close();
            } catch (Exception ignore) {
            }
        }
        if (session != null) {
            // noinspection EmptyCatchBlock
            try {
                session.close();
            } catch (Exception ignore) {
            }
        }
        if (connection != null) {
            // noinspection EmptyCatchBlock
            try {
                connection.close();
            } catch (Exception ignore) {
            }
        }
    }
}
