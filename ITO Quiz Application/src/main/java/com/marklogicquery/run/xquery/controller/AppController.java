package com.marklogicquery.run.xquery.controller;

import com.marklogicquery.run.xquery.model.Answer;
import com.marklogicquery.run.xquery.model.Candidate;
import com.marklogicquery.run.xquery.model.Question;
import com.marklogicquery.run.xquery.model.UpdateQuestions;
import com.marklogicquery.run.xquery.service.serviceImpl.AdminServiceImpl;
import com.marklogicquery.run.xquery.service.serviceImpl.CandidateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    AdminServiceImpl adminServiceImpl;

    @Autowired
    CandidateServiceImpl candidateServiceImpl;

    @PostMapping("/createQuestions")
    public String createQuestions(@RequestBody List<Question> questions)
    {
        return adminServiceImpl.createQuestions(questions);
    }

    @GetMapping(value="/getQuestions", produces = "application/json")
    public String getQuestions()
    {
        return adminServiceImpl.getAllQuestions();
    }

    @GetMapping(value="/getQuestionSetById",produces="application/json")
    public String getQuestionSetById(@RequestParam(required = false) String questionSetId)
    {
        return adminServiceImpl.getQuestionSetById(questionSetId);
    }

    @GetMapping(value="/getQuestionById",produces="application/json")
    public String getQuestionById(@RequestParam(required = false) String questionSetId, @RequestParam(required = false) String questionId)
    {
        return adminServiceImpl.getQuestionById(questionSetId,questionId);
    }

    @PutMapping("/updateQuestionsById")
    public String updateQuestionsById(@RequestBody UpdateQuestions updateQuestions)
    {
        return adminServiceImpl.updateQuestionsById(updateQuestions);
    }


    @DeleteMapping("/deleteQuestion")
    public String deleteQuestionById(@RequestParam(required = false) String questionSetId, @RequestParam(required = false) String questionId)
    {
        return adminServiceImpl.deleteQuestionById(questionSetId,questionId);
    }

    @GetMapping("/Evaluate")
    public String evaluate(@RequestParam(required = false) String candidateId)
    {
        return adminServiceImpl.evaluate(candidateId);
    }

    @PostMapping("/assessment/createCandidate")
    public String createCandidate(@RequestBody(required = false) Candidate candidate)
    {
        return candidateServiceImpl.createCandidate(candidate);
    }

    @GetMapping(value="/assessment/getQuestionSet",produces="application/json")
    public String getQuestionSet(@RequestParam(required = false) String candidateId, @RequestParam(required = false) String questionSetId)
    {
        return candidateServiceImpl.getQuestionSet(candidateId,questionSetId);
    }

    @PostMapping("/Assessment/submitAnswerSheet")
    public String submitAnswerSheet(@RequestParam String candidateId, @RequestBody Answer answer)
    {
        return candidateServiceImpl.submitAnswerSheet(candidateId,answer);
    }


}
