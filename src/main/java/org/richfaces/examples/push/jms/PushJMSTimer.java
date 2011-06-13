package org.richfaces.examples.push.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

@Singleton
public class PushJMSTimer {

    private static final Logger LOGGER = Logger.getLogger(PushJMSTimer.class.getName());

    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "topic/chat")
    private Topic topic;

    @Schedule(hour = "*", minute = "*", second = "*")
    public void push() {
        TopicConnection connection = null;
        TopicSession session = null;
        TopicPublisher publisher = null;
        try {
            TopicConnectionFactory tcf = (TopicConnectionFactory) connectionFactory;
            connection = tcf.createTopicConnection();
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            publisher = session.createPublisher(topic);
            ObjectMessage message = session.createObjectMessage("message");
            publisher.publish(message);
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            JMSHelper.close(publisher, session, connection);
        }
    }
}
