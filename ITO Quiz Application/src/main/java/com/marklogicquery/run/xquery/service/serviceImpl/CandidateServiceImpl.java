package com.marklogicquery.run.xquery.service.serviceImpl;

import com.marklogicquery.run.xquery.model.Answer;
import com.marklogicquery.run.xquery.model.Candidate;
import com.marklogicquery.run.xquery.repository.AdminRepository;
import com.marklogicquery.run.xquery.repository.CandidateRepository;
import com.marklogicquery.run.xquery.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    SequenceGenerator.CandidateIdGenerator candidateIdGenerator;

    @Autowired
    SequenceGenerator.AnswerIdGenerator answerIdGenerator;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Override
    public String createCandidate(Candidate candidate) {
        try {
            if (candidate == null) {
                throw new IllegalArgumentException("Request Body is empty");
            }
            if (candidate.getName() == null) {
                throw new IllegalArgumentException("Name is \"null\"");
            }
            if (candidate.getEmailId() == null) {
                throw new IllegalArgumentException("Email is \"null\"");
            }
            candidateIdGenerator.setId(Long.parseLong(candidateIdGenerator.fetchIdFromDatabase().trim()));
            long candidateId = candidateIdGenerator.nextId();
            candidate.setCandidate_id(String.valueOf(candidateId));
            return candidateRepository.createCandidate(candidate, candidateId,candidateIdGenerator);
        } catch (Exception e) {
            return "Error : "+e.getMessage();
        }
    }

    @Override
    public String getQuestionSet(String candidateId, String questionSetId) {
        try {
            if(candidateId == null)
                throw new IllegalArgumentException("candidateId is \"null\"");
            return candidateRepository.getQuestionSet(candidateId, questionSetId);
        } catch (Exception e) {
            return "Error : "+e.getMessage();
        }
    }

    @Override
    public String submitAnswerSheet(String candidateId, Answer answer) {
        try{
            answerIdGenerator.setId(Long.parseLong(answerIdGenerator.fetchIdFromDatabase().trim()));
            long Id = answerIdGenerator.nextId();
            String id = String.valueOf(Id);
            answer.setId(id);
            answer.setCandidate_id(candidateId);
            answerIdGenerator.updateIdInDatabase(Id);
            return candidateRepository.submitAnswerSheet(candidateId, answer);
        } catch (NumberFormatException e) {
            return "Error : "+e.getMessage();
        }
    }

}
