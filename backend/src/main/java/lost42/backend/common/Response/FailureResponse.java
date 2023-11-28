package lost42.backend.common.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "API 실패 응답")
public class FailureResponse<T> {
    private final int code = 400;
    private final String status = "fail";
    private T data;

    @Builder
    private FailureResponse(T data) {
        this.data = data;
    }

    public static <T> FailureResponse<T> from(T data) {
        return FailureResponse.<T>builder()
                .data(data)
                .build();
    }

    public static FailureResponse<?> noContent() {
        return FailureResponse.builder().build();
    }
}
