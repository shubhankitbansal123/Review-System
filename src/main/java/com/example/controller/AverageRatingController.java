package com.example.controller;

import com.example.Exception.DataNotFoundException;
import com.example.models.AverageRating;
import com.example.models.RatingResponse;
import com.example.service.AverageRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(AverageRatingController.class);

    public AverageRatingController(AverageRatingService averageRatingService) {
        this.averageRatingService = averageRatingService;
    }

    @GetMapping("/findInventoryRecordInPagination")
    private ResponseEntity<Object> findInventoryRecordInPagination(@RequestParam(name = "pageno",required = false,defaultValue = "0") Integer pageno,@RequestParam(name = "pagesize",required = false,defaultValue = "1") Integer pagesize){
        try {
            List<AverageRating> averageRatings = averageRatingService.findInventoryRecordInPagination(pageno, pagesize, "averagerate");
            logger.info("Fetch Inventory records in pagination is successful");
            return new ResponseEntity<>(averageRatings,HttpStatus.OK);
        }catch (Exception e){
            logger.error("Records does not fetched");
            throw new DataNotFoundException("Record does not fetched");
        }
    }

    @GetMapping("/findOttRecordInPagination")
    private ResponseEntity<Object> findOttRecordInPagination(@RequestParam(name = "pageno",required = false,defaultValue = "0") Integer pageno, @RequestParam(name = "pagesize",defaultValue = "1",required = false) Integer pagesize){
        try {
            List<AverageRating> averageRatings = averageRatingService.findOttRecordInPagination(pageno, pagesize, "averagerate");
            logger.info("Fetch Ott records in pagination is successful");
            return new ResponseEntity<>(averageRatings,HttpStatus.OK);
        }catch (Exception e){
            logger.error("Records does not fetched");
            throw new DataNotFoundException("Record does not fetched");
        }
    }

    @GetMapping("/getAverageRatingAndPeople")
    public RatingResponse getAverageRatingAndPeople(@RequestParam(name = "type_id",required = false) String typeId,@RequestParam(name = "type",required = false) String type){
        AverageRating averageRating = averageRatingService.getAverageRatingAndPeople(Integer.parseInt(typeId),type);
        RatingResponse ratingResponse = new RatingResponse(averageRating.getAveragerate(),averageRating.getNumberofpeople());
        return ratingResponse;
    }
}
