package net.okhotnikov.htmltsv.model;

public class Article {
    public String id;
    public String keywords;
    public String content;

    public Article() {
    }


    public Article(String id, String keywords) {
        this.id = id;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return  id + '\t'+ keywords + '\t' + content + '\t';
    }
}
