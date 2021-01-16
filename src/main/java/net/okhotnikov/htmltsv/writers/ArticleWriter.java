package net.okhotnikov.htmltsv.writers;


import net.okhotnikov.htmltsv.model.Article;
import net.okhotnikov.htmltsv.model.Book;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ArticleWriter implements ItemWriter<Book> {

    private final String dir;

    public ArticleWriter(@Value("${env.output}") String dir) {
        this.dir = dir;
    }

    @Override
    public void write(List<? extends Book> list) throws Exception {
        for(Book book: list){
            try(PrintWriter printer = new PrintWriter(new FileWriter(dir +"/"+ book.name))){
                for (Article article: book.articles)
                    printer.println(article);
            }
        }
    }
}
