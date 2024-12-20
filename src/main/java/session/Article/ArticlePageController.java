package session.Article;

import org.hibernate.validator.internal.constraintvalidators.hv.Mod10CheckValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ArticlePageController {
    private final ArticleRepository articleRepository;

    public ArticlePageController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;

    }

    @GetMapping("/all-articles")
    public String showAllArticlesPage() {
        return "all-articles";
    }

    @GetMapping("/article/{articleId}")
    public String showArticlePage() {
        return "article";
    }
}