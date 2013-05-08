package com.thoughtworks.i1.commons.db;

import com.googlecode.flyway.core.Flyway;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import org.junit.Before;
import org.junit.Test;

import static com.thoughtworks.i1.commons.config.DatabaseConfiguration.H2;
import static org.mockito.Mockito.*;

public class MigrationTest {
    private Flyway flyway;
    private Migration migration;

    @Before
    public void before(){
        flyway = mock(Flyway.class);
        migration = new Migration(flyway);
    }

    @Test
    public void should_not_migrate_when_migration_is_not_auto(){

        DatabaseConfiguration databaseConfiguration = Configuration.config()
                .database().with(H2.driver, H2.tempFileDB, H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("user").password("")
                .migration().auto(false).end()
                .build();

        migration.migrate(databaseConfiguration);

        verify(flyway, times(0)).migrate();
    }

    @Test
    public void should_migrate_when_migration_is_auto(){

        DatabaseConfiguration databaseConfiguration = Configuration.config()
                .database().with(H2.driver, H2.tempFileDB, H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("user").password("")
                .migration().auto(true).end()
                .build();

        migration.migrate(databaseConfiguration);

        verify(flyway, times(1)).migrate();
    }
}
