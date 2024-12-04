@import hexlet.code.dto.urls.UrlChecksPage
@import java.time.format.DateTimeFormatter

@param UrlChecksPage page
!{var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");}
@template.layout.page(
page = page,
content = @`

    <main class="flex-grow-1">

        <section class="container-lg mt-5">
            <h1>Сайт: ${page.getUrl().getName()}</h1>

            <table class="table table-striped">
                <tr>
                    <td>
                        ID
                    </td>
                    <td>
                        ${page.getUrl().getId()}
                    </td>
                </tr>
                <tr>
                    <td>
                        Имя
                    </td>
                    <td>
                        ${page.getUrl().getName()}
                    </td>
                </tr>
                <tr>
                    <td>
                        Дата создания
                    </td>
                    <td>
                        ${page.getUrl().getCreatedAt().toString()}
                    </td>
                </tr>
            </table>
            <h2>Проверки</h2>
            <form method="post" action="/urls/${page.getUrl().getId()}/checks">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr><th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                </tr></thead>
                <tbody>

                @for(var check : page.getUrlChecks())
                    <tr>
                        <td>
                            ${check.getId()}
                        </td>
                        <td>
                            ${check.getStatusCode()}
                        </td>
                        <td>
                            ${check.getTitle()}
                        </td>
                        <td>
                            ${check.getH1()}
                        </td>
                        <td>
                            ${check.getDescription()}
                        </td>
                        <td>
                            ${check.getCreatedAt().format(formatter)}
                        </td>
                    </tr>
                @endfor

                </tbody>

            </table>

        </section>
    </main>
`
)