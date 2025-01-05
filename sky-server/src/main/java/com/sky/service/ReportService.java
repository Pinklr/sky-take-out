package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    UserReportVO getUserReport(LocalDate begin, LocalDate end);
}
