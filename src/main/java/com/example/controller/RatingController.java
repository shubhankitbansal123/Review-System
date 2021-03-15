package com.example.controller;

import com.example.models.*;
import com.example.service.AverageRatingService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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
    public String rateHotel(@RequestBody Rating rating, @RequestHeader("user_token") String userToken){

        try {
            Users users = userService.getUserInfo(userToken);
            if (users == null) {
                return "User is not exist";
            }
            rating.setUserid(users.getUserid());
            rating.setType(users.getType());
            if (users.getType().equalsIgnoreCase("Hotel")) {
                if (ObjectUtils.isEmpty(rating.getHotelid()) || ObjectUtils.isEmpty(rating.getRateHotel()) || ObjectUtils.isEmpty(rating.getName())) {
                    return "Some fields are empty";
                }
                rating.setRatingOtt(new RatingOtt(0, 0, 0, 0, 0));
                rating.setRatingInventory(new RatingInventory(0, 0, 0));
                Hotel hotel = hotelService.fetchHotel(rating.getHotelid());
                if (hotel == null) {
                    return "Invalid request";
                }
                ratingService.save(rating);
                RatingHotel ratingHotel = hotel.getRateAverageHotel();
                float average = hotel.getAveragerating();
                Integer numberofpeople = hotel.getNoofpeople();
                ratingHotel.setFood((ratingHotel.getFood() * numberofpeople + rating.getRateHotel().getFood()) / (numberofpeople + 1));
                ratingHotel.setHygine((ratingHotel.getHygine() * numberofpeople + rating.getRateHotel().getHygine()) / (numberofpeople + 1));
                ratingHotel.setService((ratingHotel.getService() * numberofpeople + rating.getRateHotel().getService()) / (numberofpeople + 1));
                ratingHotel.setSecurity((ratingHotel.getSecurity() * numberofpeople + rating.getRateHotel().getSecurity()) / (numberofpeople + 1));
                ratingHotel.setLocality((ratingHotel.getLocality() * numberofpeople + rating.getRateHotel().getLocality()) / (numberofpeople + 1));
                float sum = rating.getRateHotel().getFood() + rating.getRateHotel().getHygine() + rating.getRateHotel().getSecurity() + rating.getRateHotel().getService() + rating.getRateHotel().getLocality();
                sum /= 5;
                average = (average * numberofpeople + sum) / (numberofpeople + 1);
                hotelService.updateAverageRating(ratingHotel, average, rating.getHotelid());
                return "rating added successfully";
            } else if (users.getType().equalsIgnoreCase("Inventory")) {
                if (ObjectUtils.isEmpty(rating.getInventoryid()) || ObjectUtils.isEmpty(rating.getRatingInventory()) || ObjectUtils.isEmpty(rating.getName())) {
                    return "Some fields are empty";
                }
                rating.setRateHotel(new RatingHotel(0, 0, 0, 0, 0));
                rating.setRatingOtt(new RatingOtt(0, 0, 0, 0, 0));
                ratingService.save(rating);

                float sum = rating.getRatingInventory().getQuality() + rating.getRatingInventory().getAsAdvertised() + rating.getRatingInventory().getSatisfaction();
                sum /= 5;
                AverageRating averageRating = averageRatingService.getAverageRatingForInventory(rating.getInventoryid());
                if (averageRating == null) {
                    averageRating = new AverageRating();
                    averageRating.setName(rating.getName());
                    averageRating.setType(rating.getType());
                    averageRating.setInventoryid(rating.getInventoryid());
                    averageRating.setRateAverageInventory(rating.getRatingInventory());
                    averageRating.setNumberofpeople(1);
                    averageRating.setRateAverageOtt(new RatingOtt(0, 0, 0, 0, 0));
                    averageRating.setAveragerate(sum);
                    averageRatingService.save(averageRating);
                    return "rate added successfully";
                }
                RatingInventory ratingInventory = averageRating.getRateAverageInventory();
                float average = averageRating.getAveragerate();
                Integer numberofpeople = averageRating.getNumberofpeople();
                ratingInventory.setQuality((ratingInventory.getQuality() * numberofpeople + rating.getRatingInventory().getQuality()) / (numberofpeople + 1));
                ratingInventory.setAsAdvertised((ratingInventory.getAsAdvertised() * numberofpeople + rating.getRatingInventory().getAsAdvertised()) / (numberofpeople + 1));
                ratingInventory.setSatisfaction((ratingInventory.getSatisfaction() * numberofpeople + rating.getRatingInventory().getSatisfaction()) / (numberofpeople + 1));
                average = (average * numberofpeople + sum) / (numberofpeople + 1);
                averageRatingService.updateRatingInventory(ratingInventory, average, rating.getInventoryid());
                return "rating added successfully";
            } else if (users.getType().equalsIgnoreCase("Ott")) {
                if (ObjectUtils.isEmpty(rating.getOttid()) || ObjectUtils.isEmpty(rating.getRatingOtt()) || ObjectUtils.isEmpty(rating.getName())) {
                    return "Some fields are empty";
                }
                rating.setRateHotel(new RatingHotel(0, 0, 0, 0, 0));
                rating.setRatingInventory(new RatingInventory(0, 0, 0));
                ratingService.save(rating);

                float sum = rating.getRatingOtt().getA() + rating.getRatingOtt().getB() + rating.getRatingOtt().getC() + rating.getRatingOtt().getD() + rating.getRatingOtt().getE();
                sum /= 5;
                AverageRating averageRating = averageRatingService.getAverageRatingForOtt(rating.getOttid());
                if (averageRating == null) {
                    averageRating = new AverageRating();
                    averageRating.setName(rating.getName());
                    averageRating.setType(rating.getType());
                    averageRating.setOttid(rating.getOttid());
                    averageRating.setRateAverageOtt(rating.getRatingOtt());
                    averageRating.setNumberofpeople(1);
                    averageRating.setRateAverageInventory(new RatingInventory(0, 0, 0));
                    averageRating.setAveragerate(sum);
                    averageRatingService.save(averageRating);
                    return "rate added successfully";
                }
                RatingOtt ratingOtt = averageRating.getRateAverageOtt();
                float average = averageRating.getAveragerate();
                Integer numberofpeople = averageRating.getNumberofpeople();
                ratingOtt.setA((ratingOtt.getA() * numberofpeople + rating.getRatingOtt().getA()) / (numberofpeople + 1));
                ratingOtt.setB((ratingOtt.getB() * numberofpeople + rating.getRatingOtt().getB()) / (numberofpeople + 1));
                ratingOtt.setC((ratingOtt.getC() * numberofpeople + rating.getRatingOtt().getC()) / (numberofpeople + 1));
                ratingOtt.setD((ratingOtt.getD() * numberofpeople + rating.getRatingOtt().getD()) / (numberofpeople + 1));
                ratingOtt.setE((ratingOtt.getE() * numberofpeople + rating.getRatingOtt().getE()) / (numberofpeople + 1));
                average = (average * numberofpeople + sum) / (numberofpeople + 1);
                averageRatingService.updateRatingOtt(ratingOtt, average, rating.getOttid());
                return "rating added successfully";
            } else {
                return "Something is wrong";
            }
        }catch (Exception e){
            return "Post rating is unsuccessful";
        }
    }
}
