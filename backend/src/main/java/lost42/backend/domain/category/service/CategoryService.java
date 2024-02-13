package lost42.backend.domain.category.service;

import lombok.RequiredArgsConstructor;
import lost42.backend.domain.board.entity.Board;
import lost42.backend.domain.board.service.BoardService;
import lost42.backend.domain.category.dto.AddCategoryReq;
import lost42.backend.domain.category.dto.GetCategoryRes;
import lost42.backend.domain.category.entity.Category;
import lost42.backend.domain.category.exception.CategoryErrorCode;
import lost42.backend.domain.category.exception.CategoryErrorException;
import lost42.backend.domain.category.respository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final BoardService boardService;
    private final CategoryRepository categoryRepository;

    public GetCategoryRes getCategory(Long boardId) {
        Category category = boardService.isExist(boardId).getCategory();

        if (category == null) {
            throw new CategoryErrorException(CategoryErrorCode.INVALID_CATEGORY);
        }

        return GetCategoryRes.builder()
                .name(category.getName())
                .build();
    }

    public void addCategory(Long boardId, AddCategoryReq req) {
        Board content = boardService.isExist(boardId);
        Category category = categoryRepository.findByName(req.getType())
                .orElseThrow(() -> new CategoryErrorException(CategoryErrorCode.INVALID_CATEGORY));

        content.addCategory(category);
    }

    public void deleteCategory(Long boardId) {
        Board content = boardService.isExist(boardId);
        if (content.getCategory() == null) {
            throw new CategoryErrorException(CategoryErrorCode.INVALID_CATEGORY);
        }

        content.deleteCategory();
    }
}
