package hexlet.code.controller;

import hexlet.code.dto.urls.UrlChecksPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlCheckController {

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Сайт не найден"));

        var urlChecks = UrlChecksRepository.getEntitiesByUrlId(id);
        var page = new UrlChecksPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/url.jte", model("page", page));
    }

    public static void createUrlCheck(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).get().getName();

        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            Integer statusCode = response.getStatus();

            // Parse the HTML response
            Document doc = Jsoup.parse(response.getBody());
            String title = doc.title();
            String h1 = doc.select("h1").text();
            String description = doc.select("meta[name=description]").attr("content");

            var urlCheck = new UrlCheck(statusCode, title, h1, description, id);

            UrlChecksRepository.save(urlCheck);

        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }

}
