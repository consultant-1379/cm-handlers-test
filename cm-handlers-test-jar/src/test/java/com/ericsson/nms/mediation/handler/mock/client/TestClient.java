/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.mediation.handler.mock.client;

import javax.annotation.Resource;
import javax.ejb.RemoteHome;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.nms.mediation.handler.mock.client.api.RemoteTestClientHome;
import com.ericsson.nms.mediation.handler.mock.flow.MockConfiguration;
import com.ericsson.nms.mediation.handler.mock.flow.MockEventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventInputHandler;

@RemoteHome(RemoteTestClientHome.class)
@Stateless
public class TestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);
    private MockConfiguration config = new MockConfiguration();
    private MockEventHandlerContext ctx;
    
    @Resource
    private SessionContext context;
    
    public boolean executeAndCheckTxStatus(String className, String fdn) throws Exception {
        execute(className, fdn);
        return context.getRollbackOnly();
    }

    private void execute(String className, String fdn) throws Exception {
        LOGGER.info("Client#execute called with parameter: " + className);
        setContext(fdn);
        Class<?> handlerClass = this.getClass().getClassLoader().loadClass(className);
        EventInputHandler handler = (EventInputHandler) handlerClass.newInstance();
        handler.init(ctx);
        handler.onEvent(null);
        LOGGER.info("Called the onEvent method of: " + className);
    }
    
    private void setContext(String fdn) {
        config.addProperty("fdn", fdn);
        config.addProperty("remoteHost", "localhost");
        config.addProperty("remotePort", "4028"); //3528 + offset (700)
        ctx = new MockEventHandlerContext(config);
    }

}
