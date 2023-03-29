package com.hanamja.moa.api.entity.group;

import com.hanamja.moa.api.controller.group.SortedBy;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
    List<Group> findAllByPageAndSort(UserAccount userAccount, SortedBy sortedBy, Long cursorId, Pageable pageable){
        JPAQuery<Group> query = jpaQueryFactory
                .selectFrom(group)
                .where(
                    group.department.id.eq(userAccount.getDepartmentId())
                )
                ;
        if (sortedBy.equals(SortedBy.RECENT)) {
            query
                    .limit(pageable.getPageSize()+1)
                    .orderBy(group.createdAt.desc()).fetch();
        } else if (sortedBy.equals(SortedBy.SOON)) {
            query
                    .limit(pageable.getPageSize()+1)
                    .orderBy(group.meetingAt.desc()).fetch();
        } else if (sortedBy.equals(SortedBy.PAST)) {
            query
                    .where(group.state.eq(State.DONE))
                    .limit(pageable.getPageSize()+1)
                    .orderBy(group.createdAt.asc()).fetch();
        }
        return query.fetch();
    }

    private BooleanExpression ltGroupId(Long cursorId) {
        return cursorId == null ? null : group.id.lt(cursorId);
    }
}
