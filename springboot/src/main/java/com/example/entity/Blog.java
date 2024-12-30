package com.example.entity;

import java.util.Objects;

public class Blog {
    /** ID */
    private Integer id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 简介 */
    private String descr;
    /** 封面 */
    private String cover;
    /** 标签 */
    private String tags;
    /** 发布人ID */
    private Integer userId;
    /** 发布日期 */
    private String date;
    /** 浏览量 */
    private Integer readCount;
    private Integer categoryId;

    private String categoryName;
    private String userName;

    private User user;

    private Integer likesCount;

    private Boolean userLike;//返回当前浏览的数据是否被当前登陆的用户点赞

    private Integer collectCount;
    private Boolean userCollect;

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Boolean getUserCollect() {
        return userCollect;
    }

    public void setUserCollect(Boolean userCollect) {
        this.userCollect = userCollect;
    }

    public Boolean getUserLike() {
        return userLike;
    }

    public void setUserLike(Boolean userLike) {
        this.userLike = userLike;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescr() {
        return descr;
    }

    public String getCover() {
        return cover;
    }

    public String getTags() {
        return tags;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return Objects.equals(id, blog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
