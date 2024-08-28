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
package com.ericsson.nms.mediation.handler.mock.client.api;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface RemoteTestClientHome extends EJBHome {

    /**
     * Name which can be used to resolve this home interface via JNDI lookup.
     */
    public String REMOTE_LOOKUP_NAME = "jts/MockRemoteClient";

    RemoteTestClient create() throws RemoteException, CreateException;

}
