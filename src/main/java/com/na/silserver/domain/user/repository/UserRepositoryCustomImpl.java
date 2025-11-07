package com.na.silserver.domain.user.repository;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.global.util.UtilCommon;
import com.na.silserver.global.util.UtilQueryDsl;
import com.querydsl.core.BooleanBuilder;
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

import static com.na.silserver.domain.user.entity.QUser.user;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UserDto.Response> findAll(UserDto.Search search, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        List<UserDto.Response> query = queryFactory.select(
                Projections.bean(UserDto.Response.class
                        , user.id
                        , user.username
                        , user.name
                        , user.email
                        , user.role
                        , user.signindAt
                        , user.signupAt
                        , user.createdBy
                        , user.createdAt
                        , user.modifiedBy
                        , user.modifiedAt
                        )
                )
                .from(user)
                .where(
                        createdAtBetween(search.getFromDate(), search.getToDate()),
                        searchValueAllCondition(search.getKeyword())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.id.count())
                .from(user)
                .where(
                        createdAtBetween(search.getFromDate(), search.getToDate()),
                        searchValueAllCondition(search.getKeyword())
                );

        return PageableExecutionUtils.getPage(query, pageable, countQuery::fetchOne);
    }

    @Override
    public List<UserDto.Response> findAll(UserDto.Search search) {
        return queryFactory.select(
                        Projections.bean(UserDto.Response.class
                                , user.id
                                , user.username
                                , user.name
                                , user.email
                                , user.role
                                , user.signindAt
                                , user.signupAt
                                , user.createdBy
                                , user.createdAt
                                , user.modifiedBy
                                , user.modifiedAt
                        )
                )
                .from(user)
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
                    case "username":
                        ORDERS.add(UtilQueryDsl.getSortedColumn(direction, user, "username"));
                        break;
                    case "name":
                        ORDERS.add(UtilQueryDsl.getSortedColumn(direction, user, "name"));
                        break;
                    case "createdAt":
                        ORDERS.add(UtilQueryDsl.getSortedColumn(direction, user, "createdAt"));
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
            return user.createdAt.between(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX)
            );
        } else if (startDate != null) {
            return user.createdAt.goe(startDate.atStartOfDay());
        } else {
            return user.createdAt.loe(endDate.atTime(LocalTime.MAX));
        }
    }

    /**
     * 검색조건
     */
    private BooleanBuilder searchValueAllCondition(String searchValue) {
        BooleanBuilder builder = new BooleanBuilder();
        return builder
                .and(usernameContains(searchValue))
                .or(nameContains(searchValue))
                .or(emailContains(searchValue))
                ;

    }

    /**
     * 아이디포함
     */
    private BooleanExpression usernameContains(String searchValue) {
        return UtilCommon.isEmpty(searchValue) ? null : user.username.containsIgnoreCase(searchValue);
    }

    /**
     * 이름포함
     */
    private BooleanExpression nameContains(String searchValue) {
        return UtilCommon.isEmpty(searchValue) ? null : user.name.containsIgnoreCase(searchValue);
    }

    /**
     * 이메일포함
     */
    private BooleanExpression emailContains(String searchValue) {
        return UtilCommon.isEmpty(searchValue) ? null : user.email.containsIgnoreCase(searchValue);
    }
}
