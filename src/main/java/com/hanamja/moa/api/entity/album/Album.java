package com.hanamja.moa.api.entity.album;

import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_ALBUM")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;

    @Column(name = "is_badged", nullable = false)
    private Boolean isBadged;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "met_user_id", nullable = false)
    private User metUser;

    @Builder
    public Album(Boolean isBadged, User owner, User metUser) {
        this.isBadged = isBadged;
        this.owner = owner;
        this.metUser = metUser;
    }
}
