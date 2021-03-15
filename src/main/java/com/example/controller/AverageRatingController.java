package com.example.controller;

import com.example.models.AverageRating;
import com.example.service.AverageRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AverageRatingController {

    @Autowired
    private AverageRatingService averageRatingService;

    @GetMapping("/findInventoryRecordInPagination")
    private List<AverageRating> findInventoryRecordInPagination(@RequestParam("pageno") Integer pageno,@RequestParam("pagesize") Integer pagesize){
        return averageRatingService.findInventoryRecordInPagination(pageno,pagesize,"averagerate");
    }

    @GetMapping("/findOttRecordInPagination")
    private List<AverageRating> findOttRecordInPagination(@RequestParam("pageno") Integer pageno, @RequestParam("pagesize") Integer pagesize){
        return averageRatingService.findOttRecordInPagination(pageno,pagesize,"averagerate");
    }
}
