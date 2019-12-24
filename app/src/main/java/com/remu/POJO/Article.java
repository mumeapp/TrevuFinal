package com.remu.POJO;

import android.graphics.drawable.Drawable;

public class Article {

    private Drawable image;
    private String title;
    private String highlight;
    private String article;

    public Article(Drawable image, String title, String article) {
        this.image = image;
        this.title = title;
        this.article = article;
        this.highlight = article.substring(0, 80) + "...";
    }

    public Drawable getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getHighlight() {
        return highlight;
    }

    public String getArticle() {
        return article;
    }
}
