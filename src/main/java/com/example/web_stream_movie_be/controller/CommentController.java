package com.example.web_stream_movie_be.controller;

import com.example.web_stream_movie_be.model.Comment;
import com.example.web_stream_movie_be.model.ReviewComment;
import com.example.web_stream_movie_be.model.User;
import com.example.web_stream_movie_be.model.response.StringResponse;
import com.example.web_stream_movie_be.model.temporary.Temporary;
import com.example.web_stream_movie_be.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private Temporary temporary;

    @GetMapping("/comment/get")
    public ResponseEntity<List<Comment>> getListCommentByMovie(String movieId) {
        return ResponseEntity.ok().body(commentService.getListCommentByMovie(movieId));
    }

    @GetMapping("/comment/count")
    public ResponseEntity<Integer> getCountComment(String movieId) {
        return ResponseEntity.ok().body(commentService.getCountComment(movieId));
    }

    @PostMapping("/comment/submit")
    public ResponseEntity<StringResponse> submitComment(@ModelAttribute Comment comment) {
        StringResponse stringResponse = new StringResponse();
        String userId = temporary.getIdUser();
        if (userId == null) {
            stringResponse.setResponse("not login yet");
            return ResponseEntity.ok().body(stringResponse);
        }
        comment.setUser(new User());
        comment.getUser().setId(userId);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String currentTime = formatter.format(date);
        comment.setCreateAt(currentTime);
        this.commentService.insertComment(comment);
        stringResponse.setResponse("ok");
        return ResponseEntity.ok().body(stringResponse);
    }



}
