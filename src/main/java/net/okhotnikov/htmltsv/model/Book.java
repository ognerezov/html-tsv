package net.okhotnikov.htmltsv.model;

import java.util.List;

public class Book {
    public String name;
    public List<Article> articles;

    public Book() {
    }

    public Book(String name, List<Article> articles) {
        this.name = name;
        this.articles = articles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
