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
package com.ericsson.nms.mediation.handler.mock.flow;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.common.config.Configuration;

/**
 * We only want to implement the getStringProperty and add method to populate
 * String properties
 * 
 * @author eshacow
 * 
 */
public class MockConfiguration implements Configuration {

    private final Map<String, Object> properties = new HashMap<String, Object>();
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MockConfiguration.class);

    @Override
    public Map<String, Object> getAllProperties() {
        return null;
    }

    @Override
    public Boolean getBooleanProperty(String arg0) {
        return null;
    }

    @Override
    public Integer getIntProperty(String arg0) {
        return null;
    }

    @Override
    public String getStringProperty(String key) {
        final Object value = properties.get(key);
        if (value != null && value instanceof String) {
            return (String) value;
        } else {
            LOGGER.error("Could not retrieve String type attribute for key ", key);
            return null;
        }
    }
    
    public void addProperty(final String key, final Object value) {
        properties.put(key, value);
    }

}
