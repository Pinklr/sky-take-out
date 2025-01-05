package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Override
    public TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end) {
        log.info("开始生成报表");
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            log.info("开始查询{}的营业额", date);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin",beginTime);
            map.put("end", endTime);
            Double turnover =  reportMapper.getTurnover(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);

        }

        TurnoverReportVO turnoverReportVO = TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();

        return turnoverReportVO;
    }

    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        log.info("开始生成报表");
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.isAfter(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            log.info("开始查询{}的用户数", date);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end", endTime);
            Integer totalUser =  reportMapper.getTotalUser(map);
            totalUserList.add(totalUser);
            Integer newUser =  reportMapper.getNewUser(map);
            newUserList.add(newUser);

        }
        UserReportVO userReportVO = UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
        return userReportVO;
    }


}
