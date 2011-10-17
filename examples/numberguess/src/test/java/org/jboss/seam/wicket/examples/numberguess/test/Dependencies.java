package org.jboss.seam.wicket.examples.numberguess.test;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

public class Dependencies {

    private static final String SETTINGS_DOT_XML = "../../settings.xml";

    public static final Archive<?>[] SOLDER = DependencyResolvers.use(MavenDependencyResolver.class)
            .configureFrom(SETTINGS_DOT_XML).loadMetadataFromPom("pom.xml").artifact("org.jboss.solder:solder-impl")
            .resolveAs(GenericArchive.class).toArray(new Archive<?>[0]);
    public static final Archive<?>[] WICKET = DependencyResolvers.use(MavenDependencyResolver.class)
            .configureFrom(SETTINGS_DOT_XML).loadMetadataFromPom("pom.xml").artifact("org.apache.wicket:wicket")
            .resolveAs(GenericArchive.class).toArray(new Archive<?>[0]);
    public static final Archive<?>[] SLF4J = DependencyResolvers.use(MavenDependencyResolver.class)
            .configureFrom(SETTINGS_DOT_XML).loadMetadataFromPom("pom.xml").artifact("org.slf4j:slf4j-simple")
            .resolveAs(GenericArchive.class).toArray(new Archive<?>[0]);
}
