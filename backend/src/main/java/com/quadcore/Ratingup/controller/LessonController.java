package com.quadcore.Ratingup.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fase")
public class LessonController {

    private final LessonController lessonController;

    public LessonController(LessonController lessonController) {
        this.lessonController = lessonController;
    }


}
