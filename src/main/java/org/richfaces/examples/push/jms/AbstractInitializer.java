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

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <p>
 * Abstract initializater and finalizer listening for JSF PostConstructApplicationEvent (needs to be registered
 * explicitly in faces-config.xml).
 * </p>
 *
 * <p>
 * After observing PostConstructApplicationEvent, it registers to PreDestroyApplicationEvent to observe finalization
 * phase.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public abstract class AbstractInitializer implements Initializer, SystemEventListener, ServletContextListener {

    private boolean correctlyInitialized = false;

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.SystemEventListener#processEvent(javax.faces.event.SystemEvent)
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostConstructApplicationEvent) {
            Application application = FacesContext.getCurrentInstance().getApplication();
            application.subscribeToEvent(PreDestroyApplicationEvent.class, this);

            try {
                initialize();
                correctlyInitialized = true;
            } catch (Exception e) {
                throw new AbortProcessingException(e);
            }
        } else {
            try {
                unload();
            } catch (Exception e) {
                throw new AbortProcessingException(e);
            }
        }
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            initialize();
            correctlyInitialized = true;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            unload();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns true if initialization method has been processed without errors.
     *
     * @return true if initialization method has been processed without errors.
     */
    protected boolean isCorrentlyInitialized() {
        return correctlyInitialized;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.SystemEventListener#isListenerForSource(java.lang.Object)
     */
    public boolean isListenerForSource(Object source) {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#unload()
     */
    public void unload() throws Exception {
    }
}
