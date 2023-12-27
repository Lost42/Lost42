package lost42.backend.common.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "API 응답: 성공")
public class SuccessResponse<T> {
    private final int code = 200;
    private final String status = "success";
    private T data;

    @Builder
    private SuccessResponse(T data) {
        this.data = data;
    }

    public static <T> SuccessResponse<T> from(T data) {
        return SuccessResponse.<T>builder()
                .data(data)
                .build();
    }

    public static SuccessResponse<?> noContent() {
        return SuccessResponse.builder().build();
    }
}
