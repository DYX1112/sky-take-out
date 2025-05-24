package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-20
 * @Description: 数据统计相关接口
 */
public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户量统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO userReportStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO orderReportStatistics(LocalDate begin, LocalDate end);

    /**
     * 销量top10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO salesTop10ReportStatistics(LocalDate begin, LocalDate end);

    void export(HttpServletResponse response);
}
