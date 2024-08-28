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
package com.ericsson.nms.mediation.handler.test.base;

import java.io.File;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.ericsson.nms.mediation.handler.mock.client.TestClient;
import com.ericsson.nms.mediation.handler.mock.client.api.RemoteTestClient;
import com.ericsson.nms.mediation.handler.mock.client.api.RemoteTestClientHome;
import com.ericsson.nms.mediation.handler.mock.dps.DpsDelegate;
import com.ericsson.nms.mediation.handler.mock.dps.DpsDelegateBean;
import com.ericsson.nms.mediation.handler.mock.dps.RemoteService;
import com.ericsson.nms.mediation.handler.mock.flow.MockConfiguration;
import com.ericsson.nms.mediation.handler.mock.flow.MockEventHandlerContext;
import com.ericsson.nms.mediation.handler.utilities.Artifact;

public class HandlerTestBase {

    public static Archive<?> createMockClientArchive(final Class<? extends HandlerTestBase> testClass) {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "mock-client.ear");
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "testEjb.jar");
        jar.addClass(HandlerTestBase.class);
        jar.addClass(testClass);
        jar.addClass(TestClient.class);
        jar.addClass(MockConfiguration.class);
        jar.addClass(MockEventHandlerContext.class);
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        jar.addAsManifestResource("client-jboss-ejb3.xml", "jboss-ejb3.xml");
        ear.addAsModule(jar);
        ear.addAsLibrary(createRemoteClientApi());
        ear.addAsLibrary(getRemoteDpsApi());
        ear.addAsManifestResource("client-jboss-deployment-structure.xml", "jboss-deployment-structure.xml");
        return ear;
    }

    public static Archive<?> createMockDpsArchive() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dps.ear");
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "testEjb.jar");
        jar.addClass(RemoteService.class);
        jar.addClass(DpsDelegateBean.class);
        jar.addAsManifestResource("dps-jboss-ejb3.xml", "jboss-ejb3.xml");
        ear.addAsModule(jar);
        ear.addAsLibrary(getRemoteDpsApi());
        ear.addAsManifestResource("dps-jboss-deployment-structure.xml", "jboss-deployment-structure.xml");
        return ear;
    }

    private static Archive<?> createRemoteClientApi() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "remote-client-api.jar");
        jar.addClass(RemoteTestClient.class);
        jar.addClass(RemoteTestClientHome.class);
        jar.addClass(EJBHome.class);
        jar.addClass(EJBObject.class);
        jar.addClass(DpsDelegate.class);
        return jar;
    }

    private static Archive<?> getRemoteDpsApi() {
        final File archiveFile = Artifact.resolveArtifactWithoutDependencies(Artifact.DPS_REMOTE_API);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact " + Artifact.DPS_REMOTE_API);
        }
        final JavaArchive jar = ShrinkWrap.createFromZipFile(JavaArchive.class, archiveFile);
        jar.addClass(DpsDelegate.class);
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return jar;
    }

    protected <T> T resolveEnterpriseBean(final String jndiName) throws NamingException {
        final Properties props = new Properties();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final InitialContext ctx = new InitialContext(props);
        @SuppressWarnings("unchecked")
        final T bean = (T) ctx.lookup(jndiName);
        return bean;
    }

    protected RemoteTestClient getTestClient() throws Exception {
        InitialContext ctx = new InitialContext();
        Object iiopObject = ctx.lookup("corbaname:iiop:localhost:4028#" + RemoteTestClientHome.REMOTE_LOOKUP_NAME);
        RemoteTestClientHome ejbHome = (RemoteTestClientHome) PortableRemoteObject.narrow(iiopObject, RemoteTestClientHome.class);
        return ejbHome.create();
    }
    
    /**
     * Creates a mock PersistenceObject with mock values
     * 
     * @param delegate
     * @return the poId of the mock PO
     */
    protected long createMockPo(final DpsDelegate delegate) {
        return delegate.createPo(null, "mockNamespace", "mockType", "mockVersion", null);
    }

}
