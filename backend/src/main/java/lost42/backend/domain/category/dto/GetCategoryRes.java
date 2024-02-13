package lost42.backend.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetCategoryRes {
    private String name;
}
