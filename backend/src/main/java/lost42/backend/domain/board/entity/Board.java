package lost42.backend.domain.board.entity;

import lombok.*;
import lost42.backend.config.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "image")
    private String image; // 추후 변경 가능성 있음

    @Column(name = "found_at", length = 50, nullable = false)
    private String foundAt;

    @Column(name = "found_date", length = 50, nullable = false)
    private String foundDate;

    @Column(name = "keeping_at", length = 50)
    private String keepingAt;

    @Column(name = "contract", length = 13) // 010-0000-0000형
    private String contract;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "managed_number", length = 100)
    private String managedNumber;

    @Column(name = "status")
    private String status; // 추후 enum 으로 변경

    @Column(name = "type")
    private String type; // 추후 enum 으로 변경

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;

}
