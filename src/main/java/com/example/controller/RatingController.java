package com.example.controller;

import com.example.Exception.*;
import com.example.models.*;
import com.example.service.AverageRatingService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class RatingController {

    @Autowired
    private final RatingService ratingService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final HotelService hotelService;

    @Autowired
    private final AverageRatingService averageRatingService;

    Logger logger = LoggerFactory.getLogger(RatingController.class);

    public RatingController(RatingService ratingService, UserService userService, HotelService hotelService, AverageRatingService averageRatingService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.hotelService = hotelService;
        this.averageRatingService = averageRatingService;
    }

    @PostMapping("/rateHotel")
    public ResponseEntity<Object> rateHotel(@RequestBody(required = false) Rating rating, @RequestHeader(name = "user_token",required = false) String userToken) throws GenericException {
        if(ObjectUtils.isEmpty(rating) || ObjectUtils.isEmpty(userToken)){
            throw new ParameterMissingException("Body is missing");
        }
        Users users = userService.getUserInfo(userToken);
        if (users == null) {
            logger.error("User token is not valid");
            throw new DataNotFoundException("User does not exist");
        }
        rating.setUserid(users.getUserid());
        if(!ObjectUtils.isEmpty(rating.getType()) && !rating.getType().equalsIgnoreCase(users.getType())){
            logger.error("rating type is invalid");
            throw new WrongInformationException("User is not belong to " + rating.getType());
        }
        if (ObjectUtils.isEmpty(rating.getType()) || ObjectUtils.isEmpty(rating.getTypeid()) || ObjectUtils.isEmpty(rating.getRating()) || ObjectUtils.isEmpty(rating.getName())) {
            logger.error("Some information is missing");
            throw new DataNotFoundException("Some fields are empty");
        }
        rating.setType(users.getType());
        if (users.getType().equalsIgnoreCase("Hotel")) {
            logger.info("User is of type Hotel");
            Hotel hotel = hotelService.fetchHotel(rating.getTypeid(),rating.getName());
            if (hotel == null) {
                logger.error("Hotel data incorrect");
                throw new WrongInformationException("Hotel data incorrect");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            RatingHotel ratingHotel = objectMapper.convertValue(rating.getRating(), RatingHotel.class);
            rating.setRating(objectMapper.convertValue(ratingHotel, Map.class));
            try {
                ratingService.save(rating);
            }catch (DataIntegrityViolationException e){
                throw new DuplicateEntryException("Duplicate Entry not allowed");
            }
            logger.info("Rating saved successfully in rating table");
            Double average = hotel.getAveragerating();
            Integer numberofpeople = hotel.getNoofpeople();
            Map<String, Double> map = hotel.getRating();
            Map<String, Double> map1 = rating.getRating();
            map.put("food", (map.get("food") * numberofpeople + map1.get("food")) / (numberofpeople + 1));
            map.put("hygine", (map.get("hygine") * numberofpeople + map1.get("hygine")) / (numberofpeople + 1));
            map.put("service", (map.get("service") * numberofpeople + map1.get("service")) / (numberofpeople + 1));
            map.put("security", (map.get("security") * numberofpeople + map1.get("security")) / (numberofpeople + 1));
            map.put("locality", (map.get("locality") * numberofpeople + map1.get("locality")) / (numberofpeople + 1));
            Double sum = map.get("food") + map.get("hygine") + map.get("service") + map.get("security") + map.get("locality");
            average = (average * numberofpeople + sum/5) / (numberofpeople + 1);
            hotelService.updateAverageRating(map, average, rating.getTypeid());
            logger.info("Rating updated in hotel table");
            return new ResponseEntity<>("Rating Added Successfully",HttpStatus.OK);
        }
        else if(users.getType().equalsIgnoreCase("Inventory")){
            logger.info("User is of type of Inventory");
            ObjectMapper objectMapper = new ObjectMapper();
            RatingInventory ratingInventory = objectMapper.convertValue(rating.getRating(), RatingInventory.class);
            rating.setRating(objectMapper.convertValue(ratingInventory, Map.class));
            try {
                ratingService.save(rating);
            }catch (DataIntegrityViolationException e){
                throw new DuplicateEntryException("Duplicate entry not allowed");
            }
            logger.info("Rating saved successfully in rating table");
            Map<String,Double> map1 = rating.getRating();
            Double sum = map1.get("quality")+map1.get("asAdvertised")+map1.get("satisfaction");
            AverageRating averageRating = averageRatingService.getAverageRatingForInventory(rating.getTypeid());
            if (averageRating == null) {
                averageRating = new AverageRating();
                averageRating.setName(rating.getName());
                averageRating.setType(rating.getType());
                averageRating.setTypeid(rating.getTypeid());
                averageRating.setRating(map1);
                averageRating.setNumberofpeople(1);
                averageRating.setAveragerate(sum/3);
                averageRatingService.save(averageRating);
                logger.info("Rating updated successfully in Average rating table");
                return new ResponseEntity<>("Rating Added Successfully",HttpStatus.OK);
            }
            Map<String,Double> map = averageRating.getRating();
            Integer numberofpeople = averageRating.getNumberofpeople();
            Double average = averageRating.getAveragerate();
            map.put("quality", (map.get("quality") * numberofpeople + map1.get("quality")) / (numberofpeople + 1));
            map.put("asAdvertised", (map.get("asAdvertised") * numberofpeople + map1.get("asAdvertised")) / (numberofpeople + 1));
            map.put("satisfaction", (map.get("satisfaction") * numberofpeople + map1.get("satisfaction")) / (numberofpeople + 1));
            average = (average * numberofpeople + sum/3) / (numberofpeople + 1);
            averageRatingService.updateRatingInventory(map, average,rating.getTypeid());
            logger.info("Rating updated successfully in Average rating table");
            return new ResponseEntity<>("Rating Added Successfully",HttpStatus.OK);
        }
        else if (users.getType().equalsIgnoreCase("Ott")){
            logger.info("User is of type Ott");
            ObjectMapper objectMapper = new ObjectMapper();
            RatingOtt ratingOtt = objectMapper.convertValue(rating.getRating(), RatingOtt.class);
            rating.setRating(objectMapper.convertValue(ratingOtt, Map.class));
            try {
                ratingService.save(rating);
            }catch (DataIntegrityViolationException e){
                throw new DuplicateEntryException("Duplicate Entry not allowed");
            }
            logger.info("Rating saved successfully in rating table");
            Map<String,Double> map1 = rating.getRating();
            Double sum = map1.get("directionAndStory")+map1.get("actorsPerformance")+map1.get("productionValues");
            AverageRating averageRating = averageRatingService.getAverageRatingForOtt(rating.getTypeid());
            if (averageRating == null) {
                averageRating = new AverageRating();
                averageRating.setName(rating.getName());
                averageRating.setType(rating.getType());
                averageRating.setTypeid(rating.getTypeid());
                averageRating.setRating(map1);
                averageRating.setNumberofpeople(1);
                averageRating.setAveragerate(sum/3);
                averageRatingService.save(averageRating);
                logger.info("Rating updated successfully in Average rating table");
                return new ResponseEntity<>("Rating Added Successfully",HttpStatus.OK);
            }
            Map<String,Double> map = averageRating.getRating();
            Integer numberofpeople = averageRating.getNumberofpeople();
            Double average = averageRating.getAveragerate();
            map.put("directionAndStory", (map.get("directionAndStory") * numberofpeople + map1.get("directionAndStory")) / (numberofpeople + 1));
            map.put("actorsPerformance", (map.get("actorsPerformance") * numberofpeople + map1.get("actorsPerformance")) / (numberofpeople + 1));
            map.put("productionValues", (map.get("productionValues") * numberofpeople + map1.get("productionValues")) / (numberofpeople + 1));
            average = (average * numberofpeople + sum/3) / (numberofpeople + 1);
            averageRatingService.updateRatingOtt(map, average,rating.getTypeid());
            logger.info("Rating updated successfully in Average rating table");
            return new ResponseEntity<>("Rating Added Successfully",HttpStatus.OK);
        }
        else {
            logger.error("Something is wrong");
            throw new WrongInformationException("Something is Wrong");
        }
    }
}
