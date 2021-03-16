package com.example.controller;

import com.example.models.AverageRating;
import com.example.service.AverageRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AverageRatingController {

    @Autowired
    private final AverageRatingService averageRatingService;

    public AverageRatingController(AverageRatingService averageRatingService) {
        this.averageRatingService = averageRatingService;
    }

    @GetMapping("/findInventoryRecordInPagination")
    private ResponseEntity<Object> findInventoryRecordInPagination(@RequestParam("pageno") Integer pageno,@RequestParam("pagesize") Integer pagesize){
        try {
            return new ResponseEntity<>(averageRatingService.findInventoryRecordInPagination(pageno, pagesize, "averagerate"),HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Record does not fetched", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findOttRecordInPagination")
    private ResponseEntity<Object> findOttRecordInPagination(@RequestParam("pageno") Integer pageno, @RequestParam("pagesize") Integer pagesize){
        try {
            return new ResponseEntity<>(averageRatingService.findOttRecordInPagination(pageno, pagesize, "averagerate"),HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Record does not fetched", HttpStatus.NOT_FOUND);
        }
    }
}
