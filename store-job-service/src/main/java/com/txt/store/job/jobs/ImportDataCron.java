package com.txt.store.job.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImportDataCron {

    @Scheduled(cron = "0 0/2 * * * *")
    public void taskReadingData() {
        int i = 0;
        System.out.println("Start job-----------------");
        while (i < 2000000) {
            log.info("Schedule task read submit data i=" + i++);
        }
        log.info("-------Finish ImportDataCron-------");
    }
}
