package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.IOException;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(getPort());
    }
    public static Javalin getApp() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        BaseRepository.dataSource = dataSource;


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });
        app.get("/", ctx -> ctx.result("Hello World"));
        return app;
    }
    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL",
                "jdbc:h2:mem:project");
    }
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
}
