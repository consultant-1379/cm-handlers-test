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

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.datalayer.dps.remote.dto.ManagedObjectDto;
import com.ericsson.oss.itpf.datalayer.dps.remote.dto.PersistenceObjectDto;

/**
 * Class used for test execution to mock out the database and to be able to setup expectations for test
 * 
 * @author eshacow
 * 
 */
@Remote
public interface DpsDelegate {

    long createPo(final String bucketName, final String namespace, final String type, final String version, final Map<String, Object> attributes);

    void setAttributes(final String bucketName, final long poId, final Map<String, Object> attributes);

    void setEntityAddressInfo(final String bucketName, final long poId, final long entityAddressInfoId);

    void addAssociation(final String bucketName, final long poId, final long poIdOfAssociatedObject, final String endpointName);

    PersistenceObjectDto getPo(final String bucketName, final long id);

    ManagedObjectDto getMo(final String bucketName, final String fdn);

    String createMo(final String fdn);
    
    List<Long> getAssociationIds(final long poId);

    void reset();

}
