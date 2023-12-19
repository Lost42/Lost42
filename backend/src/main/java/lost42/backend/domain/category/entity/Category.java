package lost42.backend.domain.category.entity;

import lombok.*;
import lost42.backend.config.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;
}
