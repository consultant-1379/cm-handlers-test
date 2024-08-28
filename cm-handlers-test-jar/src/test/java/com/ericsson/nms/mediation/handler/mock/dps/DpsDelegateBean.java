package com.ericsson.nms.mediation.handler.mock.dps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;

import com.ericsson.oss.itpf.datalayer.dps.remote.dto.ManagedObjectDto;
import com.ericsson.oss.itpf.datalayer.dps.remote.dto.PersistenceObjectDto;

@Singleton
public class DpsDelegateBean implements DpsDelegate {

    private Map<String, ManagedObjectDto> mos;
    private Map<Long, PersistenceObjectDto> pos;
    private Map<Long, List<Long>> associations;
    private long id;
    
    @Resource
    private SessionContext context;

    @Override
    public long createPo(String bucketName, String namespace, String type, String version, Map<String, Object> attributes) {
        long poId = id++;
        final PersistenceObjectDto po = new PersistenceObjectDto(namespace, type, version, poId, null);
        pos.put(poId, po);
        return poId;
    }

    @Override
    public void setAttributes(String bucketName, long poId, Map<String, Object> attributes) {

    }

    @Override
    public void setEntityAddressInfo(String bucketName, long poId, long entityAddressInfoId) {
        PersistenceObjectDto po = pos.get(poId);
        if (po instanceof ManagedObjectDto) {
            ManagedObjectDto mo = (ManagedObjectDto) po;
            ManagedObjectDto newMo = new ManagedObjectDto(mo.getNamespace(), mo.getType(), mo.getVersion(), poId, entityAddressInfoId, mo.getFdn(),
                    mo.getName());
            mos.put(mo.getFdn(), newMo);
            pos.put(poId, newMo);
        } else {
            PersistenceObjectDto newPo = new PersistenceObjectDto(po.getNamespace(), po.getType(), po.getVersion(), poId, entityAddressInfoId);
            pos.put(poId, newPo);
        }
    }

    @Override
    public void addAssociation(String bucketName, long poId, long poIdOfAssociatedObject, String endpointName) {
        List<Long> poAssociations = associations.get(poId);
        if(poAssociations == null) {
            poAssociations = new ArrayList<Long>();
        }
        poAssociations.add(poIdOfAssociatedObject);
        associations.put(poId, poAssociations);
    }

    @Override
    public PersistenceObjectDto getPo(String bucketName, long id) {
        return pos.get(id);
    }

    @Override
    public ManagedObjectDto getMo(String bucketName, String fdn) {
        context.setRollbackOnly();
        return mos.get(fdn);
    }

    @Override
    public String createMo(final String fdn) {
        long poId = id++;
        final ManagedObjectDto mo = new ManagedObjectDto(" ", " ", " ", poId, null, fdn, " ");
        mos.put(fdn, mo);
        pos.put(poId, mo);
        return fdn;
    }
    
    @Override
    public List<Long> getAssociationIds(final long poId) {
        return associations.get(poId);
    }

    @Override
    public void reset() {
        this.mos = new HashMap<String, ManagedObjectDto>();
        this.pos = new HashMap<Long, PersistenceObjectDto>();
        this.associations = new HashMap<Long, List<Long>>();
        this.id = 0;
    }

}
