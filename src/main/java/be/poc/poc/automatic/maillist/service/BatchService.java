package be.poc.poc.automatic.maillist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class BatchService {
    final static String resourceFolderPath = "src/main/resources/";

    private final JobLauncher jobLauncher;
    private final Job sendMails;

    public BatchService(JobLauncher jobLauncher, Job sendMails) {
        this.jobLauncher = jobLauncher;
        this.sendMails = sendMails;
    }

    private static ResponseEntity<Object> logFileContents(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity processFile(MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(resourceFolderPath + file.getOriginalFilename());
        Files.write(path, bytes);

        JobParameters jobParameter = new JobParametersBuilder().toJobParameters();
        JobExecution jobExecution = jobLauncher.run(sendMails, jobParameter);
        return logFileContents(file);
    }
}
