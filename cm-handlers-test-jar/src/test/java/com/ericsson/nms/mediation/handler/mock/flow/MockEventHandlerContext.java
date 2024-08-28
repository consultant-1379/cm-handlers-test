package com.ericsson.nms.mediation.handler.mock.flow;

import java.util.Collection;

import com.ericsson.oss.itpf.common.config.Configuration;
import com.ericsson.oss.itpf.common.event.ControlEvent;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventSubscriber;

public class MockEventHandlerContext implements EventHandlerContext {
    
    private Configuration config;
    
    public MockEventHandlerContext(final Configuration config) {
        this.config = config;
    }

    @Override
    public Configuration getEventHandlerConfiguration() {
        return this.config;
    }

    @Override
    public Collection<EventSubscriber> getEventSubscribers() {
        return null;
    }

    @Override
    public void sendControlEvent(ControlEvent arg0) {

    }

}
