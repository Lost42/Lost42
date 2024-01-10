package lost42.backend.domain.board.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 *	categoryId: 1,
 * 	name: "아이폰",
 * 	foundAt: "4클러스터",
 * 	startDate: "2023-11-13",
 * 	endDate: "2023-11-13",
 * 	managedNumber: "123",
 * 	status: "보관중", "처리 완료", "미해결",
 * 	type: "FOUND", "STORAGE", "ALL",
 * 	page: 1
 */
@Getter
public class GetBoardReq {
    private Long categoryId;
    private String name;
    private String foundAt;
    private String startDate;
    private String endDate;
    private String managedNumber;
    private String status;
    private String type;
    @NotNull
    private Long page;
}
