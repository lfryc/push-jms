/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.examples.push.jms;

/**
 * Initializer which manages start of associated MessageProducer.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 *
 */
public abstract class AbstractMessageProducerInitializer extends AbstractInitializer {

    private Thread messageProducerThread;
    private MessageProducerRunnable messageProducerRunnable;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#initialize()
     */
    public void initialize() throws Exception {
        messageProducerRunnable = new MessageProducerRunnable(createMessageProducer());
        messageProducerThread = new Thread(messageProducerRunnable, "MessageProducerThread");
        messageProducerThread.setDaemon(false);
        messageProducerThread.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#unload()
     */
    @Override
    public void unload() throws Exception {
        if (messageProducerThread != null) {
            messageProducerRunnable.stop();
            messageProducerThread.interrupt();
        }
    }

    public abstract MessageProducer createMessageProducer();
}
