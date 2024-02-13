package lost42.backend.domain.board.entity;

import lombok.*;
import lost42.backend.config.Auditable;
import lost42.backend.domain.board.converter.BoardStatusConverter;
import lost42.backend.domain.board.converter.BoardTypeConverter;
import lost42.backend.domain.board.dto.ChangeTypeReq;
import lost42.backend.domain.board.dto.UpdateContentReq;
import lost42.backend.domain.category.entity.BoardCategory;
import lost42.backend.domain.category.entity.Category;
import lost42.backend.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOARD")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

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
    @Convert(converter = BoardStatusConverter.class)
    private BoardStatus status;

    @Column(name = "type")
    @Convert(converter = BoardTypeConverter.class)
    private BoardType type;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;

    public Board updateContent(UpdateContentReq req) {
        this.name = req.getBoardName();
        this.image = req.getBoardImage();
        this.foundAt = req.getBoardFoundAt();
        this.foundDate = req.getBoardFoundDate();
        this.keepingAt = req.getBoardKeepingAt();
        this.description = req.getBoardDescription();

        return this;
    }

    public Board changeType(ChangeTypeReq req) {
        this.type = BoardType.fromString(req.getBoardType());
        return this;
    }

    public Board deleteContent() {
        this.deletedDt = LocalDateTime.now();
        return this;
    }

    public Board addCategory(Category category) {
        this.category = category;
        return this;
    }

    public Board deleteCategory() {
        this.category = null;
        return this;
    }
}
