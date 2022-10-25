package com.example.criteriabuilder.controller;

import com.example.criteriabuilder.model.Tutorial;
import com.example.criteriabuilder.repository.TutorialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TutorialController {
    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<Map<String,Object>> getAllTutorials(
            @RequestParam(required = false)String title,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size
    ){
        try{
            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable paging = PageRequest.of(page, size);
            Page<Tutorial> pageTuts;
            if(title== null){
                pageTuts = tutorialRepository.findAll(paging);
            }
            else{
                pageTuts = tutorialRepository.findByTitleContaining(title,paging);
            }
            tutorials = pageTuts.getContent();
            Map<String,Object>response = new HashMap<>();
            response.put("tutorials",tutorials);
            response.put("currentPage",pageTuts.getNumber());
            response.put("totalItems",pageTuts.getTotalElements());
            response.put("totalPages",pageTuts.getTotalElements());
            return new ResponseEntity<>(response,HttpStatus.OK);


        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



   // @GetMapping("/tutorials")
   // public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title,
   //                                                       @RequestParam(defaultValue = "0") int page,
   //                                                       @RequestParam(defaultValue = "3") int size,
   //                                                       @RequestParam(defaultValue = "id,desc")String[] sort) {

   //     try {


    //        List<Tutorial> tutorials = new ArrayList<>();
    //        if (title == null) {
                //tutorialRepository.findAll().forEach(tutorials::add);  //mostrar publicado ascendente viene por defecto
                //ordenado de manera descendente
                //tutorialRepository.findAll(Sort.by("published").descending()).forEach(tutorials::add); //publicados de manera descendente
    //            tutorialRepository.findAll(Sort.by("published").descending().and(Sort.by("title"))).forEach(tutorials::add);

                //se puede crear un new Sort Object con la lista de order objects




      //      } else {
      //          tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
       //     }
      //      if (tutorials.isEmpty()) {
      //          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      //      }
      //      return new ResponseEntity<>(tutorials, HttpStatus.OK);
      //  } catch (Exception e) {
      //      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      //  }
   // }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
        if (tutorialData.isPresent()) {
            return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
        Optional<Tutorial>  tutorialData = tutorialRepository.findById(id);
        if(tutorialData.isPresent()){
            Tutorial _tutorial = tutorialData.get();
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());
            return new ResponseEntity<>(tutorialRepository.save(_tutorial),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id){
        try{
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteALlTutorials(){
        try{
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("/tutorials/published")
    public ResponseEntity<Map<String,Object>> findByPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ){

        try{
           List<Tutorial> tutorials = new ArrayList<Tutorial>();
           Pageable paging = PageRequest.of(page, size);
           Page<Tutorial> pageTuts = tutorialRepository.findByPublished(true,paging);
           tutorials = pageTuts.getContent();
           Map<String,Object> response = new HashMap<>();
           response.put("tutorials",tutorials);
           response.put("currentPage",pageTuts.getNumber());
           response.put("totalItems",pageTuts.getTotalElements());
           response.put("totalPages",pageTuts.getTotalPages());
           return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (Exception e){

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }








 //   public ResponseEntity<List<Tutorial>> findByPublished(){
 //       try{
 //           List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
 //           if(tutorials.isEmpty()){
 //               return new ResponseEntity<>(HttpStatus.NO_CONTENT);
 //           }
 //           return new ResponseEntity<>(tutorials,HttpStatus.OK);
 //       }catch (Exception e){
 //           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
 //       }
 //   }


}



