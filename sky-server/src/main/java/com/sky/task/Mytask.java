package com.sky.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Mytask {

//    @Scheduled(cron = "0/5 * * * * ?")
    public void execute() {
        log.info("执行定时任务");
    }
}
