package com.hanamja.moa.api.entity.group;

import com.hanamja.moa.api.entity.user_group.QUserGroup;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanamja.moa.api.entity.group.QGroup.group;
import static com.hanamja.moa.api.entity.user_group.QUserGroup.userGroup;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryCustomImpl {

    private final JPAQueryFactory jpaQueryFactory;

    List<Group> findAllByUserId(Long userId){
        return jpaQueryFactory.selectFrom(group)
                .join(userGroup.group, group)
                .where(userGroup.joiner.id.eq(userId))
                .fetch();
    }
}
