package hexlet.code;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;


public class AppTest {
    Javalin app;
    private static MockWebServer mockWebServer;
    public static final String TEST_HTML_PAGE = "HTMLTestPage.html";
    private static String readFixture(String fileName) throws IOException {
        Path path = Paths.get("src", "test", "resources", "fixtures", fileName);
        return Files.readString(path);
    }


    @BeforeAll
    static void setUpMock() throws Exception {
        mockWebServer = new MockWebServer();
        String htmlContent = readFixture(TEST_HTML_PAGE);
        MockResponse mockResponse = new MockResponse()
                .setBody(htmlContent)
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        mockWebServer.start();
    }
    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterAll
    static void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testWithMock() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            String mockUrlName = mockWebServer.url("/").toString().replaceAll("/$", "");
            Url mockUrl = new Url(mockUrlName);
            UrlRepository.save(mockUrl);

            var response = client.post(NamedRoutes.urlCheckPath(mockUrl.getId()));
            assertThat(response.code()).isEqualTo(200);

            List<UrlCheck> urlChecks = UrlChecksRepository.getEntitiesByUrlId(mockUrl.getId());
            assertThat(urlChecks.size()).isEqualTo(1);

            UrlCheck lastUrlCheck = UrlChecksRepository.getEntitiesByUrlId(mockUrl.getId()).get(0);
            assertThat(lastUrlCheck.getUrlId()).isEqualTo(1);
            assertThat(lastUrlCheck.getStatusCode()).isEqualTo(200);
            assertThat(lastUrlCheck.getTitle()).contains("HTML test page");
            assertThat(lastUrlCheck.getH1()).contains("The best test page for all possible scenarios");
            assertThat(lastUrlCheck.getDescription()).contains("Discover this test HTML page tailored for web "
                    + "applications testing. Featuring headers, paragraphs, title and meta data, ideal for "
                    + "evaluating functionality.");
        });
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
