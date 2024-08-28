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

import javax.ejb.EJBObject;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

public interface RemoteTestClient extends EJBObject {
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean executeAndCheckTxStatus(final String className, String fdn) throws RemoteException;
    
}
