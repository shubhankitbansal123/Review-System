package com.example.controller;

import com.example.models.*;
import com.example.service.AverageRatingService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RatingController(RatingService ratingService, UserService userService, HotelService hotelService, AverageRatingService averageRatingService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.hotelService = hotelService;
        this.averageRatingService = averageRatingService;
    }

    @PostMapping("/rateHotel")
    public ResponseEntity<Object> rateHotel(@RequestBody Rating rating, @RequestHeader("user_token") String userToken) {

        try {
            Users users = userService.getUserInfo(userToken);
            if (users == null) {
                return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
            }
            rating.setUserid(users.getUserid());
            if(!ObjectUtils.isEmpty(rating.getType()) && !rating.getType().equalsIgnoreCase(users.getType())){
                return new ResponseEntity<>("User is not belong to " + rating.getType(),HttpStatus.BAD_REQUEST);
            }
            if (ObjectUtils.isEmpty(rating.getType()) || ObjectUtils.isEmpty(rating.getTypeid()) || ObjectUtils.isEmpty(rating.getRating()) || ObjectUtils.isEmpty(rating.getName())) {
                return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
            }
            rating.setType(users.getType());
            if (users.getType().equalsIgnoreCase("Hotel")) {
                Hotel hotel = hotelService.fetchHotel(rating.getTypeid(),rating.getName());
                if (hotel == null) {
                    return new ResponseEntity<>("Invalid Data",HttpStatus.BAD_REQUEST);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                RatingHotel ratingHotel = objectMapper.convertValue(rating.getRating(), RatingHotel.class);
                rating.setRating(objectMapper.convertValue(ratingHotel, Map.class));
                ratingService.save(rating);
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
                return new ResponseEntity<>("Rating Added Successfully",HttpStatus.ACCEPTED);
            }
            else if(users.getType().equalsIgnoreCase("Inventory")){
                ObjectMapper objectMapper = new ObjectMapper();
                RatingInventory ratingInventory = objectMapper.convertValue(rating.getRating(), RatingInventory.class);
                rating.setRating(objectMapper.convertValue(ratingInventory, Map.class));
                ratingService.save(rating);
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
                    return new ResponseEntity<>("Rating Added Successfully",HttpStatus.ACCEPTED);
                }
                Map<String,Double> map = averageRating.getRating();
                Integer numberofpeople = averageRating.getNumberofpeople();
                Double average = averageRating.getAveragerate();
                map.put("quality", (map.get("quality") * numberofpeople + map1.get("quality")) / (numberofpeople + 1));
                map.put("asAdvertised", (map.get("asAdvertised") * numberofpeople + map1.get("asAdvertised")) / (numberofpeople + 1));
                map.put("satisfaction", (map.get("satisfaction") * numberofpeople + map1.get("satisfaction")) / (numberofpeople + 1));
                average = (average * numberofpeople + sum/3) / (numberofpeople + 1);
                averageRatingService.updateRatingInventory(map, average,rating.getTypeid());
                return new ResponseEntity<>("Rating Added Successfully",HttpStatus.ACCEPTED);
            }
            else if (users.getType().equalsIgnoreCase("Ott")){
                ObjectMapper objectMapper = new ObjectMapper();
                RatingOtt ratingOtt = objectMapper.convertValue(rating.getRating(), RatingOtt.class);
                rating.setRating(objectMapper.convertValue(ratingOtt, Map.class));
                ratingService.save(rating);
                Map<String,Double> map1 = rating.getRating();
                Double sum = map1.get("a")+map1.get("b")+map1.get("c");
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
                    return new ResponseEntity<>("Rating Added Successfully",HttpStatus.ACCEPTED);
                }
                Map<String,Double> map = averageRating.getRating();
                Integer numberofpeople = averageRating.getNumberofpeople();
                Double average = averageRating.getAveragerate();
                map.put("a", (map.get("a") * numberofpeople + map1.get("a")) / (numberofpeople + 1));
                map.put("b", (map.get("b") * numberofpeople + map1.get("b")) / (numberofpeople + 1));
                map.put("c", (map.get("c") * numberofpeople + map1.get("c")) / (numberofpeople + 1));
                average = (average * numberofpeople + sum/3) / (numberofpeople + 1);
                averageRatingService.updateRatingOtt(map, average,rating.getTypeid());
                return new ResponseEntity<>("Rating Added Successfully",HttpStatus.ACCEPTED);
            }
            else {
                return new ResponseEntity<>("Something is Wrong",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Post Rating is Unsuccessful",HttpStatus.BAD_REQUEST);
        }
    }
}
