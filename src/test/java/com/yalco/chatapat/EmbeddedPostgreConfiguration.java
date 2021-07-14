package com.yalco.chatapat;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@Configuration
public class EmbeddedPostgreConfiguration {

    @Bean(destroyMethod = "stop")
    public PostgresProcess postgresProcess() throws IOException {

        PostgresConfig postgresConfig = new PostgresConfig(Version.V11_1,
                new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
                new AbstractPostgresConfig.Storage("database_for_tests"), new AbstractPostgresConfig.Timeout(60_000),
                new AbstractPostgresConfig.Credentials("chatapat_test", "chatapat_test"));
        postgresConfig.withAdditionalInitDbParams(Arrays.asList("-E UTF8"));

        PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getInstance(EmbeddedPostgres
                .cachedRuntimeConfig(Paths.get(System.getProperty("java.io.tmpdir") + "/postgres_binaries")));

        PostgresExecutable exec = runtime.prepare(postgresConfig);
        PostgresProcess process = exec.start();

        return process;
    }

    @Bean(destroyMethod = "close")
    @DependsOn("postgresProcess")
    public DataSource dataSource(PostgresProcess postgresProcess) {
        PostgresConfig postgresConfig = postgresProcess.getConfig();

        HikariConfig config = new HikariConfig();
        config.setUsername(postgresConfig.credentials().username());
        config.setPassword(postgresConfig.credentials().password());
        config.setJdbcUrl(
                "jdbc:postgresql://localhost:" + postgresConfig.net().port() + "/" + postgresConfig.storage().dbName());

        return new HikariDataSource(config);
    }
}
