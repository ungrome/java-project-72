package hexlet.code;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.sql.SQLException;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {
    Javalin app;
    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }
    @Test
    public void testMainPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        }));
    }
    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, ((server, client) -> {
            var requestBody = "name=https://urlname.com/";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string().contains("https://urlname.com/"));
        }));
    }
    @Test
    public void testURLPage() throws SQLException {
        var url = new Url("https://urlname.com/");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        }));
    }

}
