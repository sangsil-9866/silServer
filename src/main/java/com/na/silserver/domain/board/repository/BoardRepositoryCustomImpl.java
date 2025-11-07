package com.na.silserver.domain.board.repository;

import com.na.silserver.domain.board.dto.BoardDto;
import com.na.silserver.global.util.UtilCommon;
import com.na.silserver.global.util.UtilQueryDsl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.na.silserver.domain.board.entity.QBoard.board;
import static com.na.silserver.domain.board.entity.QBoardFile.boardFile;
import static com.querydsl.jpa.JPAExpressions.select;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<BoardDto.Response> findAll(BoardDto.Search search, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        List<BoardDto.Response> query = queryFactory.select(
                Projections.bean(BoardDto.Response.class
                        , board.id
                        , board.title
                        , board.content
                        , board.views
                        , board.createdBy
                        , board.createdAt
                        , board.modifiedBy
                        , board.modifiedAt
                        )
                )
                .from(board)
                .where(
                        createdAtBetween(search.getFromDate(), search.getToDate()),
                        searchValueAllCondition(search.getKeyword())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(board.id.count())
                .from(board)
                .where(
                        createdAtBetween(search.getFromDate(), search.getToDate()),
                        searchValueAllCondition(search.getKeyword())
                );

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    @Override
    public List<BoardDto.Response> findAll(BoardDto.Search search) {
        return queryFactory.select(
                        Projections.bean(BoardDto.Response.class
                                , board.id
                                , board.title
                                , board.content
                                , board.views
                                , board.createdBy
                                , board.createdAt
                                , board.modifiedBy
                                , board.modifiedAt
                                , ExpressionUtils.as (
                                        select(boardFile.count())
                                                .from(boardFile)
                                                .where(boardFile.board.eq(board)), "fileCount")
                        )
                )
                .from(board)
                .where(
                        createdAtBetween(search.getFromDate(), search.getToDate()),
                        searchValueAllCondition(search.getKeyword())
                ).fetch();
    }

    /**
     * sort
     */
    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        // pageable의 정렬 정보 사용
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id":
                        ORDERS.add(UtilQueryDsl.getSortedColumn(direction, board, "id"));
                        break;
                    case "createdAt":
                        ORDERS.add(UtilQueryDsl.getSortedColumn(direction, board, "createdAt"));
                        break;
                    case "title":
                        ORDERS.add(UtilQueryDsl.getSortedColumn(direction, board, "title"));
                        break;
                }
            }
        }
        return ORDERS;
    }


    /**
     * 기간조건
     */
    private BooleanExpression createdAtBetween(LocalDate startDate, LocalDate endDate) {
        if (UtilCommon.isEmpty(startDate) && UtilCommon.isEmpty(endDate)) {
            return null;
        }
        if (startDate != null && endDate != null) {
            return board.createdAt.between(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX)
            );
        } else if (startDate != null) {
            return board.createdAt.goe(startDate.atStartOfDay());
        } else {
            return board.createdAt.loe(endDate.atTime(LocalTime.MAX));
        }
    }

    /**
     * 검색조건
     */
    private BooleanBuilder searchValueAllCondition(String searchValue) {
        BooleanBuilder builder = new BooleanBuilder();
        return builder
                .and(titleContains(searchValue))
                .or(contentsContains(searchValue));

    }

    /**
     * 제목포함
     */
    private BooleanExpression titleContains(String searchValue) {
        return UtilCommon.isEmpty(searchValue) ? null : board.title.containsIgnoreCase(searchValue);
    }

    /**
     * 내용포함
     */
    private BooleanExpression contentsContains(String searchValue) {
        return UtilCommon.isEmpty(searchValue) ? null : board.content.containsIgnoreCase(searchValue);
    }



}
