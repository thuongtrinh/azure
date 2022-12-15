package com.txt.store.job.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImportDataCron {

    @Scheduled(cron = "0 31 * * * *")
    public void taskReadingData() {
        log.info("Schedule task read submit data");
    }
}
