@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@param BasePage page

@template.layout.page(page = page,
content = @`
    <section>
        <h1 class="display-3 mb-0">Анализатор страниц</h1>
    </section>
`
)