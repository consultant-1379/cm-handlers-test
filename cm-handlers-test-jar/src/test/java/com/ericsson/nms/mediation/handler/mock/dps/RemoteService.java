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
package com.ericsson.nms.mediation.handler.mock.dps;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;

import com.ericsson.oss.itpf.datalayer.dps.remote.RemoteDataPersistenceServiceHome;
import com.ericsson.oss.itpf.datalayer.dps.remote.dto.ManagedObjectDto;
import com.ericsson.oss.itpf.datalayer.dps.remote.dto.PersistenceObjectDto;
import com.ericsson.oss.itpf.datalayer.dps.remote.exception.DataPersistenceServiceRemoteException;

@RemoteHome(RemoteDataPersistenceServiceHome.class)
@Stateless
public class RemoteService {

    @EJB
    private DpsDelegate delegate;
    
    public long createPo(final String bucketName, final String namespace, final String type, final String version,
                         final Map<String, Object> attributes) throws RemoteException, DataPersistenceServiceRemoteException {
        return delegate.createPo(bucketName, namespace, type, version, attributes);
    }

    public void setAttributes(final String bucketName, final long poId, final Map<String, Object> attributes) throws RemoteException,
            DataPersistenceServiceRemoteException {

    }

    public void setEntityAddressInfo(final String bucketName, final long poId, final long entityAddressInfoId) throws RemoteException,
            DataPersistenceServiceRemoteException {
        delegate.setEntityAddressInfo(bucketName, poId, entityAddressInfoId);
    }

    public void addAssociation(final String bucketName, final long poId, final long poIdOfAssociatedObject, final String endpointName)
            throws RemoteException, DataPersistenceServiceRemoteException {
        delegate.addAssociation(bucketName, poId, poIdOfAssociatedObject, endpointName);
    }

    public PersistenceObjectDto getPo(final String bucketName, final long id) throws RemoteException, DataPersistenceServiceRemoteException {
        return delegate.getPo(bucketName, id);
    }

    public ManagedObjectDto getMo(final String bucketName, final String fdn) throws RemoteException, DataPersistenceServiceRemoteException {
        return delegate.getMo(bucketName, fdn);
    }

}
