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
package com.ericsson.nms.mediation.handler.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.nms.mediation.handler.mock.dps.DpsDelegate;
import com.ericsson.nms.mediation.handler.test.base.HandlerTestBase;
import com.ericsson.oss.itpf.datalayer.dps.remote.dto.ManagedObjectDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class HandlerTest extends HandlerTestBase {

    private static final String CLIENT_EAR = "client.ear";
    private static final String DPS_EAR = "dps.ear";
    private static final String EAI_CREATION_HANDLER = "com.ericsson.oss.models.base.mediation.handlers.EaiCreationHandler";
    private static final String CI_ASSOCIATION_HANDLER = "com.ericsson.oss.models.base.mediation.handlers.CiAssociationHandler";
    private static final String MANAGED_FUNCTION_FDN = "mockManagedFunctionFdn";
    private static final String CONNECTIVITY_INFO_FDN = "mockManagedFunctionFdn,connectivityInfoFdn";
    private static final String DELEGATE_JNDI_NAME = "java:global/dps/testEjb/DpsDelegateBean!com.ericsson.nms.mediation.handler.mock.dps.DpsDelegate";

    @Deployment(name = DPS_EAR, testable = false, managed = true, order = 1)
    public static Archive<?> createDps() {
        return createMockDpsArchive();
    }

    @Deployment(name = CLIENT_EAR, testable = true, managed = true, order = 2)
    public static Archive<?> deployClientArchive() {
        return createMockClientArchive(HandlerTest.class);
    }

    @Before
    public void setup() throws Exception {
        final DpsDelegate delegate = resolveEnterpriseBean(DELEGATE_JNDI_NAME);
        delegate.reset();
    }

    /**
     * Tests that the EaiCreationHandler creates the EAI and sets this on the ManagedFunction. This test verifies that the handler participates in the
     * calling transaction
     * 
     * @throws Exception
     */
    @Test
    @OperateOnDeployment(CLIENT_EAR)
    public void callEaiCreationHandler() throws Exception {
        final DpsDelegate delegate = resolveEnterpriseBean(DELEGATE_JNDI_NAME);
        delegate.createMo(MANAGED_FUNCTION_FDN);
        assertTrue("The handler did not participate in the calling transaction",
                getTestClient().executeAndCheckTxStatus(EAI_CREATION_HANDLER, MANAGED_FUNCTION_FDN));

        ManagedObjectDto mo = delegate.getMo(null, MANAGED_FUNCTION_FDN);
        assertEquals("The poId of the EAI is " + mo.getEntityAddressInfoId() + ", not 1 (Long) as expected", new Long(1), mo.getEntityAddressInfoId());
    }

    @Test
    @OperateOnDeployment(CLIENT_EAR)
    public void eaiCreationFailInit() throws Exception {
        try {
            getTestClient().executeAndCheckTxStatus(EAI_CREATION_HANDLER, null);
            fail();
        } catch (Exception e) {
            //EJB 2 call to remote client throws java.rmi.RemoteException. IllegalArgumentException thrown from handler
            assertEquals(java.rmi.RemoteException.class, e.getClass());
        }
    }

    /**
     * Tests that the CiAssociationHandler creates the association between the CI and EAI. This test verifies that the handler participates in the
     * calling transaction
     * 
     * @throws Exception
     */
    @Test
    @OperateOnDeployment(CLIENT_EAR)
    public void callCiAssociationHandler() throws Exception {
        final DpsDelegate delegate = resolveEnterpriseBean(DELEGATE_JNDI_NAME);
        delegate.createMo(MANAGED_FUNCTION_FDN);
        delegate.createMo(CONNECTIVITY_INFO_FDN);
        ManagedObjectDto mo = delegate.getMo(null, MANAGED_FUNCTION_FDN);
        long eaiId = createMockPo(delegate);
        delegate.setEntityAddressInfo(null, mo.getPoId(), eaiId);
        assertTrue("The handler did not participate in the calling transaction",
                getTestClient().executeAndCheckTxStatus(CI_ASSOCIATION_HANDLER, CONNECTIVITY_INFO_FDN));
        mo = delegate.getMo(null, MANAGED_FUNCTION_FDN);
        eaiId = mo.getEntityAddressInfoId();
        ManagedObjectDto ciMo = delegate.getMo(null, CONNECTIVITY_INFO_FDN);
        assertTrue("The CI " + ciMo.getFdn() + " was not associated with the EAI with poId " + eaiId,
                delegate.getAssociationIds(eaiId).contains(ciMo.getPoId()));
    }

    @Test
    @OperateOnDeployment(CLIENT_EAR)
    public void ciAssociationFailInit() throws Exception {
        try {
            getTestClient().executeAndCheckTxStatus(CI_ASSOCIATION_HANDLER, null);
            fail();
        } catch (Exception e) {
            //EJB 2 call to remote client throws java.rmi.RemoteException. IllegalArgumentException thrown from handler
            assertEquals(java.rmi.RemoteException.class, e.getClass());
        }
    }

}
