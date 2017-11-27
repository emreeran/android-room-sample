package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

/**
 * Created by Emre Eran on 27.11.2017.
 */

public class PostWithLikesAndUser {

    @NonNull
    @ColumnInfo(name = "PostId")
    public String id;

    @ColumnInfo(name = "PostContent")
    public String content;

    @NonNull
    @ColumnInfo(name = "PostCreatedAt")
    public Date createdAt;

    @Embedded
    public User user;

//    @Embedded
//    public List<LikeUser> likes;

    @Relation(entity = Like.class, entityColumn = "LikePostId", parentColumn = "PostId")
    public List<Like> likes;

    public PostWithLikesAndUser() {
    }

    @Override
    public String toString() {
        return "PostWithLikesAndUser{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user.toString() +
                ", likes=" + likes +
                '}';
    }
}
