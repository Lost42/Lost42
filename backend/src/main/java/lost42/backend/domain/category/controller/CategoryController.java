package lost42.backend.domain.category.controller;

import lombok.RequiredArgsConstructor;
import lost42.backend.common.response.SuccessResponse;
import lost42.backend.domain.category.dto.AddCategoryReq;
import lost42.backend.domain.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{board_id}")
    public ResponseEntity<?> getCategory(@PathVariable(name = "board_id") Long boardId) {
        return ResponseEntity.ok().body(SuccessResponse.from(categoryService.getCategory(boardId)));
    }

    @PostMapping("/{board_id}")
    public ResponseEntity<?> addCategory(@PathVariable(name = "board_id") Long boardId,
                                         @RequestBody AddCategoryReq req) {
        categoryService.addCategory(boardId, req);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "board_id") Long boardId) {
        categoryService.deleteCategory(boardId);
        return ResponseEntity.ok().body(SuccessResponse.noContent());
    }
}
