package com.example.service;

import com.example.models.*;
import com.example.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RatingService {

    @Autowired
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public void deleteDataOfHotel(Integer id) {
        ratingRepository.deleteDataOfHotel(id);
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public List<RatingAverage> ratingAverage(){

        List<Rating> ratings = ratingRepository.ratingAverage();
        Map<Integer, RatingHotel> ratingHotelMap = new HashMap<>();
        Map<Integer, RatingInventory> ratingInventoryMap = new HashMap<>();
        Map<Integer,RatingOtt> ratingOttMap = new HashMap<>();
        RatingHotel ratingHotel = new RatingHotel(0,0,0,0,0);
        RatingInventory ratingInventory = new RatingInventory(0,0,0,0,0);
        RatingOtt ratingOtt = new RatingOtt(0,0,0,0,0);
        int countHotel=0,countInventory=0,countOtt=0;
        for(int i=0;i<ratings.size();i++){
            if(ratings.get(i).getType().equalsIgnoreCase("Hotel")){
                Rating rating = ratings.get(i);
                ratingHotel.setFood(ratingHotel.getFood()+rating.getRateHotel().getFood());
                ratingHotel.setHygine(ratingHotel.getHygine()+rating.getRateHotel().getHygine());
                ratingHotel.setLocality(ratingHotel.getLocality()+rating.getRateHotel().getLocality());
                ratingHotel.setService(ratingHotel.getService()+rating.getRateHotel().getService());
                ratingHotel.setSecurity(ratingHotel.getSecurity()+rating.getRateHotel().getSecurity());
                countHotel++;
            }
            else if(ratings.get(i).getType().equalsIgnoreCase("Inventory")){
                Rating rating = ratings.get(i);
                ratingInventory.setA(ratingInventory.getA()+rating.getRatingInventory().getA());
                ratingInventory.setB(ratingInventory.getB()+rating.getRatingInventory().getB());
                ratingInventory.setC(ratingInventory.getC()+rating.getRatingInventory().getC());
                ratingInventory.setD(ratingInventory.getD()+rating.getRatingInventory().getD());
                ratingInventory.setE(ratingInventory.getE()+rating.getRatingInventory().getE());
                countInventory++;
            }
            else if(ratings.get(i).getType().equalsIgnoreCase("Ott")){
                Rating rating = ratings.get(i);
                ratingOtt.setA(ratingOtt.getA()+rating.getRatingOtt().getA());
                ratingOtt.setB(ratingOtt.getB()+rating.getRatingOtt().getB());
                ratingOtt.setC(ratingOtt.getC()+rating.getRatingOtt().getC());
                ratingOtt.setD(ratingOtt.getD()+rating.getRatingOtt().getD());
                ratingOtt.setE(ratingOtt.getE()+rating.getRatingOtt().getE());
                countOtt++;
            }

            if(i+1==ratings.size() || (!ratings.get(i).getType().equals(ratings.get(i + 1).getType()) || !ratings.get(i).getType_id().equals(ratings.get(i + 1).getType_id()))){
                if(ratings.get(i).getType().equalsIgnoreCase("Hotel")){
                    if(countHotel==0) countHotel=1;
                    ratingHotel.setFood(ratingHotel.getFood()/countHotel);
                    ratingHotel.setHygine(ratingHotel.getHygine()/countHotel);
                    ratingHotel.setLocality(ratingHotel.getLocality()/countHotel);
                    ratingHotel.setService(ratingHotel.getService()/countHotel);
                    ratingHotel.setSecurity(ratingHotel.getSecurity()/countHotel);
                    ratingHotelMap.put(ratings.get(i).getType_id(),ratingHotel);
                    countHotel=0;
                    ratingHotel=new RatingHotel(0,0,0,0,0);
                }
                else if(ratings.get(i).getType().equalsIgnoreCase("Inventory")){
                    if(countInventory==0) countInventory=1;
                    ratingInventory.setA(ratingInventory.getA()/countInventory);
                    ratingInventory.setB(ratingInventory.getB()/countInventory);
                    ratingInventory.setC(ratingInventory.getC()/countInventory);
                    ratingInventory.setD(ratingInventory.getD()/countInventory);
                    ratingInventory.setE(ratingInventory.getE()/countInventory);
                    ratingInventoryMap.put(ratings.get(i).getType_id(),ratingInventory);
                    countInventory=0;
                    ratingInventory=new RatingInventory(0,0,0,0,0);
                }
                else if(ratings.get(i).getType().equalsIgnoreCase("Ott")){
                    if(countOtt==0) countOtt=1;
                    ratingOtt.setA(ratingOtt.getA()/countOtt);
                    ratingOtt.setB(ratingOtt.getB()/countOtt);
                    ratingOtt.setC(ratingOtt.getC()/countOtt);
                    ratingOtt.setD(ratingOtt.getD()/countOtt);
                    ratingOtt.setE(ratingOtt.getE()/countOtt);
                    ratingOttMap.put(ratings.get(i).getType_id(),ratingOtt);
                    countOtt=0;
                    ratingOtt=new RatingOtt(0,0,0,0,0);
                }
            }
        }

        List<RatingAverage> ratingAverages = new ArrayList<>();

        ratingHotelMap.forEach((integer, ratingHotel1) -> {
            RatingAverage ratingAverage = new RatingAverage();
            ratingAverage.setType("Hotel");
            ratingAverage.setType_id(integer);
            ratingAverage.setRatingHotel(ratingHotel1);
            ratingAverage.setRatingInventory(new RatingInventory(0,0,0,0,0));
            ratingAverage.setRatingOtt(new RatingOtt(0,0,0,0,0));
            ratingAverages.add(ratingAverage);
        });

        ratingInventoryMap.forEach((integer, ratingInventory1) -> {
            RatingAverage ratingAverage = new RatingAverage();
            ratingAverage.setType("Inventory");
            ratingAverage.setType_id(integer);
            ratingAverage.setRatingInventory(ratingInventory1);
            ratingAverage.setRatingHotel(new RatingHotel(0,0,0,0,0));
            ratingAverage.setRatingOtt(new RatingOtt(0,0,0,0,0));
            ratingAverages.add(ratingAverage);
        });

        ratingOttMap.forEach((integer, ratingOtt1) -> {
            RatingAverage ratingAverage = new RatingAverage();
            ratingAverage.setType("Ott");
            ratingAverage.setType_id(integer);
            ratingAverage.setRatingOtt(ratingOtt1);
            ratingAverage.setRatingHotel(new RatingHotel(0,0,0,0,0));
            ratingAverage.setRatingInventory(new RatingInventory(0,0,0,0,0));
            ratingAverages.add(ratingAverage);
        });
        return ratingAverages;
    }

    public boolean getRatingCount(Integer id) {
        return ratingRepository.getRatingCount(id);
    }
}
