package session.Article;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class ArticleDTO {
    private int articleId;
    private String title;
    private String content;
    private String image;
    public static Article toEntity(ArticleDTO articleDTO){
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setImage(articleDTO.getImage());
        return article;
    }
    public static Article toEntity(ArticleDTO articleDTO,int id){
        Article article = new Article();
        article.setArticleId(id);
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setImage(articleDTO.getImage());
        return article;
    }
}