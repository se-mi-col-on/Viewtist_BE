package semicolon.viewtist.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name= "user_id")
    private Long userId;

    @Column
    private String title;

    @Column
    private String content;

    public void setTitle(String updateTitleRequest) {
        this.title = updateTitleRequest;
    }

    public void setContent(String updateContentRequest) {
        this.content = updateContentRequest;
    }
}
