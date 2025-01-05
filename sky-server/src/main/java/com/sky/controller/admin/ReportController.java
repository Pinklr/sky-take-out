package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额数据统计")
    Result<TurnoverReportVO> getTurnoverReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverReport(begin, end);
        log.info("营业额数据统计");
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    Result<UserReportVO> getUserReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        UserReportVO userReportVO = reportService.getUserReport(begin, end);
        log.info("用户数据统计");
        return Result.success(userReportVO);
    }
}
