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
package com.ericsson.nms.mediation.handler.utilities;

import java.io.File;

import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

public class Artifact {
    
    public static final String DPS_REMOTE_API = "com.ericsson.oss.itpf.datalayer.dps:dps-remote-api";
    
    /**
     * Maven resolver that will try to resolve dependencies using pom.xml of the
     * project where this class is located.
     * 
     * @return MavenDependencyResolver
     */
    public static MavenDependencyResolver getMavenResolver() {
        return DependencyResolvers.use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

    }
    
    /**
     * Resolve artifacts without dependencies
     * 
     * @param artifactCoordinates
     * @return
     */
    public static File resolveArtifactWithoutDependencies(
            final String artifactCoordinates) {
        final File[] artifacts = getMavenResolver()
                .artifact(artifactCoordinates).exclusion("*").resolveAsFiles();
        if (artifacts == null) {
            throw new IllegalStateException("Artifact with coordinates "
                    + artifactCoordinates + " was not resolved");
        }
        if (artifacts.length != 1) {
            throw new IllegalStateException(
                    "Resolved more then one artifact with coordinates "
                            + artifactCoordinates);
        }
        return artifacts[0];
    }

}
