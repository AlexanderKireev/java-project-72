package hexlet.code;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.domain.query.QUrl;
import hexlet.code.domain.query.QUrlCheck;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static java.nio.file.Files.readString;
import static org.assertj.core.api.Assertions.assertThat;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;
import io.ebean.Database;
//import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static Database database;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        database = DB.getDefault();
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        database.script().run("/truncate.sql");
        database.script().run("/seed-test-db.sql");
    }

    @Nested
    class UrlTest {

        @Test
        void testMain() {
            HttpResponse<String> response = Unirest.get(baseUrl).asString();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody()).contains("Анализатор страниц");
        }

        @Test
        void testListUrls() {
            HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains("https://www.example1.com");
            assertThat(body).contains("https://www.example2.com");
            assertThat(body).contains("https://www.example3.com");
        }

        @Test
        void testShowUrl() {
            HttpResponse<String> response = Unirest.get(baseUrl + "/urls/1").asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains("https://www.example1.com");
        }

        @Test
        void testAddUrl() {
            String input = "https://www.example4.com";
            HttpResponse responsePost = Unirest.post(baseUrl + "/urls")
                    .field("url", input).asEmpty();

            assertThat(responsePost.getStatus()).isEqualTo(302);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

            HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(body).contains(input);
            assertThat(body).contains("Страница успешно добавлена");

            Url actualUrl = new QUrl()
                    .name.equalTo(input)
                    .findOne();

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(input);
        }

        @Test
        void testCreateExistingUrl() {
            String inputName = "https://www.example1.com";
            HttpResponse responsePost = Unirest.post(baseUrl + "/urls")
                    .field("url", inputName).asEmpty();

            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

            HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
            String body = response.getBody();

            assertThat(body).contains(inputName);
            assertThat(body).contains("Страница уже существует");
        }

        @Test
        void testCreateBadUrl() {
            String inputName = "badurl";
            HttpResponse responsePost = Unirest.post(baseUrl + "/urls")
                    .field("url", inputName).asEmpty();

            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/");

            HttpResponse<String> response = Unirest.get(baseUrl + "/").asString();
            String body = response.getBody();

            assertThat(body).contains("Некорректный URL");
        }

        @Test
        public void testCheckUrl() throws Exception {
            MockWebServer server = new MockWebServer();
            String mockHTML = readString(Paths.get("src/test/resources/mock.html")/*, StandardCharsets.US_ASCII*/);
            server.enqueue(new MockResponse().setBody(mockHTML));
            server.start();

            // get temporary Mock-web-address (e.g. http://na1r.services.adobe.com:62168/)
            String mockUrl = server.url("/").toString();

            // add this temporary Mock-web-address to DB
            Unirest.post(baseUrl + "/urls").field("url", mockUrl).asEmpty();

            // find this Mock-web-address in DB
            Url url = new QUrl()
                    .name.equalTo(mockUrl.substring(0, mockUrl.length() - 1))
                    .findOne();

            // run method addChek
            assert url != null;
            Unirest.post(baseUrl + "/urls/" + url.getId() + "/checks").asEmpty();

            HttpResponse<String> responseShow = Unirest.get(baseUrl + "/urls/" + url.getId()).asString();
            server.shutdown();

            UrlCheck check = new QUrlCheck()
                    .findList().get(0);
            assertThat(check).isNotNull();

            assertThat(responseShow.getStatus()).isEqualTo(200);
            assertThat(responseShow.getBody()).contains("Some Title");
            assertThat(responseShow.getBody()).contains("Some H1");
            assertThat(responseShow.getBody()).contains("Some Description");
        }
    }
}
