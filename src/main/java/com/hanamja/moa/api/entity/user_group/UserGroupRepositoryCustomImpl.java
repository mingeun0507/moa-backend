package com.hanamja.moa.api.entity.user_group;

import com.hanamja.moa.api.controller.SortedBy;
import com.hanamja.moa.api.entity.group.State;
import com.hanamja.moa.api.entity.user.User;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanamja.moa.api.entity.group.QGroup.group;
import static com.hanamja.moa.api.entity.user.QUser.user;
import static com.hanamja.moa.api.entity.user_group.QUserGroup.userGroup;

@RequiredArgsConstructor
@Repository
public class UserGroupRepositoryCustomImpl implements UserGroupRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<User> findMyAlbumUser(Long userId, State state, SortedBy sortedBy) {

        JPQLQuery<Long> subQuery =  JPAExpressions.select(group.id).distinct()
                .from(userGroup)
                .join(userGroup.group, group)
                .where(userGroup.joiner.id.eq(userId), userGroup.group.state.eq(state))
                .orderBy(userGroup.group.modifiedAt.desc());


        return jpaQueryFactory.select(user).distinct()
                .from(userGroup)
                .join(userGroup.joiner,user)
                .where(userGroup.group.id.in(subQuery), user.id.ne(userId))
                .fetch();



    }
}
