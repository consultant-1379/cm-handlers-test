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

package com.ericsson.nms.mediation.HandlerTestClient.rest.resources;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.ericsson.nms.mediation.HandlerTestClient.rest.config.TestConfiguration;
import com.ericsson.nms.mediation.HandlerTestClient.rest.config.TestEventHandlerContext;
import com.ericsson.oss.itpf.common.event.handler.EventInputHandler;

@Path("/invoke")
@RequestScoped
public class Invoker {

    private static final String MOCI_HANDLER = "com.ericsson.oss.models.moci.mediation.handlers.MociHandler";
    private TestEventHandlerContext ctx;
    private TestConfiguration config;

    @GET
    @Produces("text/plain")
    @Path("MociHandler/{ipAddress}")
    @SuppressWarnings("PMD.UseProperClassLoader")
    public String invokeMociHandler(@PathParam("ipAddress") final String ipAddress) {
        try {
            setConfigForMociHandler(ipAddress);
            final Class<?> handlerClass = this.getClass().getClassLoader().loadClass(MOCI_HANDLER);
            final EventInputHandler handler = (EventInputHandler) handlerClass.newInstance();
            handler.init(ctx);
            handler.onEvent(null);
        } catch (Exception e) {
            return "Could not successfully invoke MociHandler for " + ipAddress + "\n\n" + e;
        }
        return "Successfully invoked MociHandler for " + ipAddress;
    }

    private void setConfigForMociHandler(final String ipAddress) {
        config = new TestConfiguration();
        config.addProperty("ipAddress", ipAddress);
        config.addProperty("secured", "true");
        ctx = new TestEventHandlerContext(config);
    }

}