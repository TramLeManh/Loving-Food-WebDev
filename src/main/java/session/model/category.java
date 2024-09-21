package session.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class category implements RowMapper<category> {
    private String category_id;
    private String category_name;
    private String category_image;

    public category() {
    }

    public category(String categoryName, String categoryId, String categoryImage) {
        category_name = categoryName;
        category_id = categoryId;
        category_image = categoryImage;
    }

    @Override
    public category mapRow(ResultSet rs, int rowNum) throws SQLException {
        category res = new category();
        res.setCategory_name(rs.getString("category_name"));
        res.setCategory_id(rs.getString("category_id"));
        res.setCategory_image(rs.getString("category_image"));
        return res;
    }
}