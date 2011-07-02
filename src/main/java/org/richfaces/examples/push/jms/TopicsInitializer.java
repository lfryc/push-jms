package org.richfaces.examples.push.jms;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

public class TopicsInitializer implements SystemEventListener {

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        TopicsContext topicsContext = TopicsContext.lookup();
        topicsContext.getOrCreateTopic(new TopicKey("test"));
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }
}
