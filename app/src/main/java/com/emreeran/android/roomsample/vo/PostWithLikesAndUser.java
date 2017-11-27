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
    public UserWithFollowers user;

//    @Embedded
//    public List<LikeWithUser> likes;

    @Relation(entity = Like.class, entityColumn = "LikePostId", parentColumn = "PostId")
    public List<Like> likes;

//    @Relation(entity = Like.class, entityColumn = "LikePostId", parentColumn = "PostId")
//    public List<LikeWithUser> likes;


    public PostWithLikesAndUser(@NonNull String id, String content, @NonNull Date createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
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
