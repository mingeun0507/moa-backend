package com.hanamja.moa.api.entity.group;

import com.hanamja.moa.api.controller.group.SortedBy;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.hanamja.moa.api.entity.group.QGroup.group;
import static com.hanamja.moa.api.entity.user_group.QUserGroup.userGroup;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    public List<Group> findAllByUserId(Long userId){
        return jpaQueryFactory.selectFrom(group)
                .join(userGroup.group, group)
                .where(userGroup.joiner.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Group> findAllByPageAndSort(UserAccount userAccount, LocalDateTime now, SortedBy sortedBy, Long cursorId, Pageable pageable){
        JPAQuery<Group> query = jpaQueryFactory
                .selectFrom(group)
                .where(
                        cursorIdBySortedBy(cursorId, sortedBy, now),
                        group.department.id.eq(userAccount.getDepartmentId())
                )
                ;
        if (sortedBy.equals(SortedBy.RECENT)) {
            query
                    .orderBy(group.createdAt.desc());
        }
//        else if (sortedBy.equals(SortedBy.SOON)) {
//            query
//                    .orderBy(
//                            group.meetingAt.asc().nullsLast(),
//                            group.createdAt.asc()
//                    );
//        }
        else if (sortedBy.equals(SortedBy.PAST)) {
            query
                    .orderBy(group.createdAt.asc());
        }
        System.out.println(query.toString());
        return query.limit(pageable.getPageSize()).fetch();
    }

    private BooleanExpression cursorIdBySortedBy(Long cursorId, SortedBy sortedBy, LocalDateTime now) {
        if (sortedBy.equals(SortedBy.RECENT)) {
            return cursorId == null ?
                    group.state.eq(State.RECRUITING).and(group.meetingAt.coalesce(now.plusDays(1)).gt(now))
                    :
                    group.state.eq(State.RECRUITING).and(group.meetingAt.coalesce(now.plusDays(1)).gt(now))
                    .and(group.id.lt(cursorId));
        }
//        else if (sortedBy.equals(SortedBy.SOON)) {
//            return cursorId == null ?
//                    group.state.eq(State.RECRUITING).and(group.meetingAt.coalesce(now.plusYears(5)).gt(now))
//                    :
//                    group.state.eq(State.RECRUITING).and(group.meetingAt.coalesce(now.plusYears(5)).gt(now))
//                    .and(group.meetingAt.after(meetingAt));
//        }
        else if (sortedBy.equals(SortedBy.PAST)) {
            return cursorId == null ?
                    group.state.eq(State.DONE)
                    :
                    group.state.eq(State.DONE)
                    .and(group.id.gt(cursorId));
        } else {
            return group.id.lt(cursorId);
        }
    }
    private BooleanExpression ltGroupId(Long cursorId) {
        return cursorId == null ? null : group.id.lt(cursorId);
    }
}
