package lost42.backend.domain.category.entity;

import lombok.*;
import lost42.backend.config.Auditable;
import lost42.backend.domain.board.entity.Board;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long categoryId;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<Board> boards = new ArrayList<>();

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;
}
