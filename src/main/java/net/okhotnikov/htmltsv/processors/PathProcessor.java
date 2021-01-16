package net.okhotnikov.htmltsv.processors;

import net.okhotnikov.htmltsv.model.Article;
import net.okhotnikov.htmltsv.model.Book;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class PathProcessor implements ItemProcessor<Path, Book> {

    private final String extension;

    public PathProcessor(@Value("${env.output-extension}") String extension) {
        this.extension = extension;
    }

    @Override
    public Book process(Path path) throws Exception {
        //String fileContent = Files.readString(path, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()),"windows-1251"));){
            String s;
            while ((s = bf.readLine()) !=null){
                sb.append(s);
            }
        }
        return new Book(getName(path),parseContent(sb.toString()));
    }

    private String getName(Path path) {
        String [] data = path.getFileName().toString().split("\\.");

        data[data.length -1] = extension;

        return String.join(".",data);
    }

    private List<Article> parseContent(String fileContent) {
        List<Article> articles = new ArrayList<>();
        String prepared = prepare(fileContent);

        int searchStart = 0;
        int index = 0;

        Article article = null;

        while (searchStart < prepared.length() - 1){
            int articleStart = prepared.indexOf("<b>",searchStart);
            int articleNameEnd = prepared.indexOf("</b>",searchStart);

            if (article != null){
                article.content = prepared
                        .substring(searchStart, articleStart >0 ? articleStart : prepared.length())
                        .replaceAll("<br>|<div>|</body>|</html>","");
            }

            if (articleStart < 0 || articleNameEnd <0)
                return articles;

            article = new Article(String.valueOf(index++), prepared.substring(articleStart + 3, articleNameEnd));
            articles.add(article);

            searchStart = articleNameEnd + 4;
        }

        return articles;
    }

    private String prepare(String fileContent) {
        return fileContent.replaceAll("\t|</div>|<div>|</body>|</html>", "");
    }
}
