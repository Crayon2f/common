package com.crayon2f.common.pojo;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Created by feifan.gou@gmail.com on 2017/8/21 17:53.
 */
@Data
public class Article {

    private String title;

    private String author;

    private Integer count;

    private Double price = 0d;

    public static List<Article> data;

    private Boolean whetherPrivate = false;

    static {
        data = Lists.newArrayList(
                new Article("标题4", "作者1", 12),
                new Article("标题4", "作者2", 1),
                new Article("标题4", "作者3", 10),
                new Article("标题4", "作者4", 11, 3.4),
                new Article("标题10", "作者5", 3, 3d),
                new Article("标题6", "作者6", 4, 7d),
                new Article("标题2", "作者7", 7),
                new Article("标题8", "作者8", 5),
                new Article("标题9", "作者9", 2, 8.3),
                new Article("标题5", "作者10", 6),
                new Article("标题11", "作者11", 9),
                new Article("标题12", "作者12", 8, 9.0d)
        );
    }

    public Article(String title, String author, Integer count) {
        this.title = title;
        this.author = author;
        this.count = count;
    }

    public Article(String title, String author, Integer count, Double price) {
        this.title = title;
        this.author = author;
        this.count = count;
        this.price = price;
    }

    public Article(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("\r{title:%s,author:%s,count:%d,price:%.2f}\r\n",
                title, author, count, price);
    }

    public void read(Integer count) {

        System.out.println(String.format("this article count is %d", count));
    }

    public void read(String title) {

        System.out.println(String.format("this article title is %s", title));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equal(title, article.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
