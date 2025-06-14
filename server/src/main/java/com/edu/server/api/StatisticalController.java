package com.edu.server.api;

import com.edu.server.collection.OrderEntity;
import com.edu.server.dao.StatisticalRepository;
import com.edu.server.response.StatisticalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/statistical")
public class StatisticalController {
    @Autowired
    private StatisticalRepository statisticalRepository;
    @GetMapping
    public ResponseEntity<StatisticalResponse> getStatisticalReport(@RequestParam(name = "startDate", required = false) String startDate, @RequestParam(name = "toDate", required = false) String toDate) throws ParseException {
        StatisticalResponse statisticalResponse = new StatisticalResponse();
        List<OrderEntity> history;

        if (startDate != null && toDate == null ) {
            history = statisticalRepository.getOrderHistoryFrom(startDate + "T00:00:00.000Z");
        } else if (startDate == null && toDate != null) {
            history = statisticalRepository.getOrderHistoryTo(toDate + "T23:59:59.999Z");
        } else if (startDate == null) {
            history = statisticalRepository.findAll();
        } else {
            history = statisticalRepository.getOrderHistoryBtw(startDate + "T00:00:00.000Z", toDate + "T23:59:59.999Z");
        }



        double revenue = 0;
        double discount = 0;
        double total = 0;
        int discountCount = 0;
        for (var orderEntity : history) {
            discount += orderEntity.getDiscount();
            if (orderEntity.getDiscount() > 0) {
                discountCount++;
            }
            revenue += orderEntity.getTotalPrice() - orderEntity.getDiscount();
            total += orderEntity.getTotalPrice();
        }

        statisticalResponse.setDiscount(discount);
        statisticalResponse.setHistory(history);
        statisticalResponse.setOrderCount(history.size());
        statisticalResponse.setRevenue(revenue);
        statisticalResponse.setTotal(total);
        statisticalResponse.setDiscountCount(discountCount);

        return ResponseEntity.ok(statisticalResponse);

    }
}
