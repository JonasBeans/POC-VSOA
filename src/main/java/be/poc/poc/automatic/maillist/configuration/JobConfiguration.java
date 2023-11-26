package be.poc.poc.automatic.maillist.configuration;

import be.poc.poc.automatic.maillist.mapper.ItemMapper;
import be.poc.poc.automatic.maillist.model.Item;
import be.poc.poc.automatic.maillist.processor.ItemProcessor;
import be.poc.poc.automatic.maillist.writer.NoItemWrite;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {

    @Value("${path.input.file}")
    String pathInputFile;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    ItemProcessor itemProcessor;

    @Autowired
    NoItemWrite noItemWrite;

    @Autowired
    ItemMapper itemMapper;

    @Bean
    public Job sendMails(){
        return new JobBuilder("sendMail", jobRepository)
                .flow(sendMailToItems())
                .end()
                .build();
    }


    @Bean
    public FlatFileItemReader reader(){
        return new FlatFileItemReaderBuilder<Item>().name("itemReader")
                .resource(new ClassPathResource(pathInputFile))
                .delimited()
                    .names(new String[]{"Titel", "email", "Functie"} )
                .fieldSetMapper(itemMapper)
                .build();
    }

    @Bean
    public Step sendMailToItems(){
        return new StepBuilder("step1", jobRepository)
                .<Item, Item> chunk(10, transactionManager)
                .reader(reader())
                .processor(itemProcessor)
                .writer(noItemWrite)
                .build();
    }
}
