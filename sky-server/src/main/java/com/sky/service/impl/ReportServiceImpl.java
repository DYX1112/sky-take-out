package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-20
 * @Description: 数据统计接口相关
 */
@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 日期表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isEqual(end)){
           begin = begin.plusDays(1);
           dateList.add(begin);
        }

        List<Double> turnoverLists = new ArrayList<>();


        // select sum(amount) from Orders where order_time > beginTime and order_time < endTime and status = 5;

        for(LocalDate i : dateList){
            LocalDateTime beginTime = LocalDateTime.of(i, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(i, LocalTime.MAX);
            Map<String, Object> m  = new HashMap<>();
            m.put("begin",beginTime);
            m.put("end",endTime);
            m.put("status", Orders.COMPLETED);
            Double amount = reportMapper.turnoverStatistics(m);
            if(amount == null){
                amount = 0.0;
            }
            turnoverLists.add(amount);
        }


        return TurnoverReportVO.builder()
                .dateList(StringUtil.join(",", dateList))
                .turnoverList(StringUtil.join(",",turnoverLists))
                .build();
    }

    /**
     * 用户量统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userReportStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUsers = new ArrayList<>();
        List<Integer> totalUsers = new ArrayList<>();
        for (LocalDate i : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(i, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(i, LocalTime.MAX);

            Map<String,Object> m = new HashMap<>();


            m.put("end",endTime);
            //查询新增用户
            // select count(id) from user where create_time > beginTime and creat_time < endTime
            Integer totalUser = userMapper.countUserByTime(m);
            totalUsers.add(totalUser);
            //查询总用户 select count(id) from user where create_time <end
            m.put("begin",beginTime);
            Integer newUser = userMapper.countUserByTime(m);
            newUsers.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtil.join(",", dateList))
                .newUserList(StringUtil.join(",",newUsers))
                .totalUserList(StringUtil.join(",",totalUsers))
                .build();
    }

    /**
     * 订单数据统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO orderReportStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderEveryday = new ArrayList<>();
        List<Integer> validOrderEveryday = new ArrayList<>();


        for(LocalDate i : dateList){
            LocalDateTime beginTime = LocalDateTime.of(i, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(i, LocalTime.MAX);

            // 记录每天的订单数
            Integer totalOrderNum = getOrderNum(beginTime,endTime,null);
            orderEveryday.add(totalOrderNum);
            //记录每天的订单有效数
            Integer validOrderNum = getOrderNum(beginTime,endTime, Orders.COMPLETED);
            validOrderEveryday.add(validOrderNum);
        }
        Integer validOrderCount = validOrderEveryday.stream().reduce(Integer::sum).get();
        Integer totalOrderCount = orderEveryday.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;
        if(totalOrderCount!=0){
            orderCompletionRate = validOrderCount.doubleValue()/totalOrderCount.doubleValue();
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(StringUtils.join(orderEveryday,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderEveryday,","))
                .build();
    }
    private Integer getOrderNum(LocalDateTime begin, LocalDateTime end, Integer status ){
        Map<String,Object> m = new HashMap<>();
        m.put("begin",begin);
        m.put("end",end);
        m.put("status",status);
        return orderMapper.getOrderNum(m);
    }

    @Override
    public SalesTop10ReportVO salesTop10ReportStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

        List<GoodsSalesDTO> lists = orderDetailMapper.getTop10(beginTime,endTime);

        List<String> name = lists.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());

        List<Integer> number = lists.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(name,","))
                .numberList(StringUtils.join(number,","))
                .build();

    }

    @Override
    public void export(HttpServletResponse response) {
        // 获取数据
        LocalDate beginTime = LocalDate.now().plusDays(-30);
        LocalDate endTime = LocalDate.now().plusDays(-1);

        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(beginTime,LocalTime.MIN),LocalDateTime.of(endTime,LocalTime.MAX));

        // 操作文件流
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            sheet1.getRow(1).getCell(1).setCellValue("时间:"+beginTime+"至"+endTime);
            sheet1.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet1.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet1.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheet1.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet1.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());

            for(int i = 0 ; i < 30 ; i ++){
                // 查数据
                LocalDate cur = beginTime.plusDays(i);
                BusinessDataVO businessData1 = workspaceService.getBusinessData(LocalDateTime.of(cur, LocalTime.MIN), LocalDateTime.of(cur, LocalTime.MAX));

                sheet1.getRow(7+i).getCell(1).setCellValue(cur.toString());
                sheet1.getRow(7+i).getCell(2).setCellValue(businessData1.getTurnover());
                sheet1.getRow(7+i).getCell(3).setCellValue(businessData1.getValidOrderCount());
                sheet1.getRow(7+i).getCell(4).setCellValue(businessData1.getOrderCompletionRate());
                sheet1.getRow(7+i).getCell(5).setCellValue(businessData1.getUnitPrice());
                sheet1.getRow(7+i).getCell(6).setCellValue(businessData1.getNewUsers());

            }

            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);


            outputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
