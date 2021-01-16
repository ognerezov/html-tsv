package net.okhotnikov.htmltsv.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class PathReader implements ItemReader<Path> {

    private final LinkedList<Path> paths;

    public PathReader(@Value("${env.path}") String dir,@Value("${env.extension}") String extension) {
        LinkedList<Path> files;
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {

            files = paths
                    .filter(Files::isRegularFile)
                    .filter(file -> extensionMatches(file, extension))
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (IOException e) {
            e.printStackTrace();
            files = new LinkedList<>();
        }
        paths = files;
    }

    public boolean extensionMatches(Path path,String extension){
        String [] name = path.getFileName().toString().split("\\.");
        return extension.equals(name[name.length -1]);
    }

    @Override
    public Path read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (paths.isEmpty())
            return null;
        return paths.pollFirst();
    }
}
