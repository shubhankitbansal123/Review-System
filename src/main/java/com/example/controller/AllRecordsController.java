package com.example.controller;

import com.example.models.AllRecords;
import com.example.models.Comment;
import com.example.models.RatingAndComments;
import com.example.models.RatingAverage;
import com.example.service.CommentService;
import com.example.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AllRecordsController {

    @Autowired
    private final RatingService ratingService;

    @Autowired
    private final CommentService commentService;

    public AllRecordsController(RatingService ratingService, CommentService commentService) {
        this.ratingService = ratingService;
        this.commentService = commentService;
    }


    @GetMapping("/averageRatingAndComments")
    public RatingAndComments averageRatingAndComments(@RequestParam("type_id") Integer id){
        List<RatingAverage> ratingAverages = ratingService.ratingAverage();
        RatingAverage ratingAverage = ratingAverages.stream().filter(ratingAverage1 -> ratingAverage1.getType_id().equals(id)).collect(Collectors.toCollection(()->new ArrayList<RatingAverage>())).get(0);
        List<String> comments = commentService.getCommentFromType_id(id);
        return new RatingAndComments(ratingAverage,comments);
    }

    @GetMapping("/getAllRecordsInPagination")
    public Page<AllRecords> records(@RequestParam("type") String type,@RequestParam("page_number") Integer pageNumber,@RequestParam("page_size") Integer pageSize){
        class SortByAverageRate implements Comparator<AllRecords> {

            @Override
            public int compare(AllRecords o1, AllRecords o2) {
                if( o1.getAverageRate()>o2.getAverageRate()){
                    return 1;
                }
                return -1;
            }
        }
        List<RatingAverage> ratingAverages = ratingService.ratingAverage();
        ratingAverages = ratingAverages.stream().filter(ratingAverage1 -> ratingAverage1.getType().equalsIgnoreCase(type)).collect(Collectors.toCollection(()->new ArrayList<RatingAverage>()));
        List<Comment> comments = commentService.findCommentByType(type);
        List<AllRecords> allRecords = new ArrayList<>();
        Map<Integer,List<String>> comments1= new HashMap<>();
        for(int i=0;i<comments.size();i++){
            Comment comment = comments.get(i);
            List<String> strings = comments1.get(comment.getType_id());
            if(strings == null){
                strings = new ArrayList<>();
            }
            strings.add(comment.getComment());
            comments1.put(comment.getType_id(),strings);
        }
        if(type.equalsIgnoreCase("Hotel")){
            for(int i=0;i<ratingAverages.size();i++){
                float average=0;
                RatingAverage ratingAverage = ratingAverages.get(i);
                average=ratingAverage.getRatingHotel().getFood()+ratingAverage.getRatingHotel().getHygine()+ratingAverage.getRatingHotel().getLocality()+ratingAverage.getRatingHotel().getService()+ratingAverage.getRatingHotel().getSecurity();
                average/=5;
                List<String> comments2 = comments1.get(ratingAverage.getType_id());
                AllRecords allRecords1 = new AllRecords(ratingAverage.getType_id(),type,average,comments2);
                allRecords.add(allRecords1);
            }
        }
        else if(type.equalsIgnoreCase("Inventory")){
            for(int i=0;i<ratingAverages.size();i++) {
                float average = 0;
                RatingAverage ratingAverage = ratingAverages.get(i);
                average = ratingAverage.getRatingInventory().getA() + ratingAverage.getRatingInventory().getB() + ratingAverage.getRatingInventory().getC() + ratingAverage.getRatingInventory().getD() + ratingAverage.getRatingInventory().getE();
                average/=5;
                List<String> comments2 = comments1.get(ratingAverage.getType_id());
                AllRecords allRecords1 = new AllRecords(ratingAverage.getType_id(),type,average,comments2);
                allRecords.add(allRecords1);
            }
        }
        else if(type.equalsIgnoreCase("Ott")){
            for(int i=0;i<ratingAverages.size();i++) {
                float average = 0;
                RatingAverage ratingAverage = ratingAverages.get(i);
                average = ratingAverage.getRatingOtt().getA() + ratingAverage.getRatingOtt().getB() + ratingAverage.getRatingOtt().getC() + ratingAverage.getRatingOtt().getD() + ratingAverage.getRatingOtt().getE();
                average/=5;
                List<String> comments2 = comments1.get(ratingAverage.getType_id());
                AllRecords allRecords1 = new AllRecords(ratingAverage.getType_id(),type,average,comments2);
                allRecords.add(allRecords1);
            }
        }
        Collections.sort(allRecords,new SortByAverageRate());
        System.out.println(allRecords.toString());
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        int start = pageable.getPageNumber()*pageable.getPageSize();
        List<AllRecords> list = new ArrayList<>();
        if(allRecords.size()<start){
            list= Collections.emptyList();
        }
        else{
            int end = Math.min(start+pageable.getPageSize(),allRecords.size());
            list=allRecords.subList(start,end);
        }
        Page<AllRecords> allRecords1 = new PageImpl<>(list,pageable,allRecords.size());
        return allRecords1;
    }
}
