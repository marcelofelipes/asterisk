package com.asterisk.backend._integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class AsteriskPostgresContainer extends PostgreSQLContainer<AsteriskPostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:12.5";
    private static AsteriskPostgresContainer container;

    private AsteriskPostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static AsteriskPostgresContainer getInstance() {
        if (container == null) {
            container = new AsteriskPostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
