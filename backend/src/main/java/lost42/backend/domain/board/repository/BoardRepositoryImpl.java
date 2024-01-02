package lost42.backend.domain.board.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lost42.backend.domain.board.dto.GetBoardReq;
import lost42.backend.domain.board.entity.Board;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static lost42.backend.domain.board.entity.QBoard.board;
import static lost42.backend.domain.category.entity.QBoardCategory.boardCategory;
import static lost42.backend.domain.category.entity.QCategory.category;


@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findAllContents(GetBoardReq req) {
        return jpaQueryFactory.selectFrom(board)
                .where(allCondition(req))
                .fetch();
    }

    private BooleanBuilder allCondition(GetBoardReq req) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder
                .and(categoryEq(req.getCategoryId()))
                .and(nameEq(req.getName()))
                .and(foundAtEq(req.getFoundAt()))
                .and(managedNumberEq(req.getManagedNumber()))
                .and(withIn(req.getStartDate(), req.getEndDate()));
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? boardCategory.category.categoryId.eq(categoryId) : null;
    }

    private BooleanExpression nameEq(String name) {
        return StringUtils.hasText(name) ? board.name.eq(name) : null;
    }

    private BooleanExpression foundAtEq(String foundAt) {
        return StringUtils.hasText(foundAt) ? board.foundAt.eq(foundAt) : null;
    }

    private BooleanExpression managedNumberEq(String managedNumber) {
        return StringUtils.hasText(managedNumber) ? board.managedNumber.eq(managedNumber) : null;
    }

    private BooleanExpression withIn(String startDate, String endDate) {
        if (startDate != null && endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);

            return board.createdAt.between(startDateTime, endDateTime);
        } else {
            return null;
        }
    }
}
