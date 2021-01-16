package net.okhotnikov.htmltsv.config;



import net.okhotnikov.htmltsv.model.Book;
import net.okhotnikov.htmltsv.processors.PathProcessor;
import net.okhotnikov.htmltsv.readers.PathReader;

import net.okhotnikov.htmltsv.service.JobCompletionNotificationListener;
import net.okhotnikov.htmltsv.writers.ArticleWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.nio.file.Path;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;
    private final PathProcessor processor;
    private final PathReader reader;

    @Value("${env.threads}")
    private int numThreads;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, PathProcessor processor, PathReader reader) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.processor = processor;
        this.reader = reader;
    }

    @Override
    public void setDataSource(DataSource dataSource) {

    }

    @Bean
    public Step step1(ArticleWriter writer) {
        return stepBuilderFactory.get("step1")
                .<Path, Book>chunk(numThreads)
                .reader(reader)
                .processor(processor)
                .writer(writer)

                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
