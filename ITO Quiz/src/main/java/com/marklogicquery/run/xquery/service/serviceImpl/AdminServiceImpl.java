package com.marklogicquery.run.xquery.service.serviceImpl;

import com.marklogicquery.run.xquery.model.Question;
import com.marklogicquery.run.xquery.model.UpdateQuestions;
import com.marklogicquery.run.xquery.repository.AdminRepository;
import com.marklogicquery.run.xquery.repository.PathRangeIndexRepository;
import com.marklogicquery.run.xquery.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    SequenceGenerator.QuestionSetIdGenerator questionSetIdGenerator;

    @Autowired
    SequenceGenerator.QuestionIdGenerator questionIdGenerator;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PathRangeIndexRepository pathRangeIndexRepository;


    @Override
    public String createQuestions(List<Question> questions) {
        try {
            if (questions == null || questions.isEmpty()) {
                throw new IllegalArgumentException("Questions list cannot be empty");
            }

            pathRangeIndexRepository.createPathRangeIndexes();
            questionSetIdGenerator.setId(Long.parseLong(questionSetIdGenerator.fetchIdFromDatabase().trim()));
            long questionSetId = questionSetIdGenerator.nextId();
            StringBuilder stringBuilder = new StringBuilder();
            for (Question q : questions) {
                long questionId = questionIdGenerator.nextId();
                q.setQuestionId(String.valueOf(questionId));
                stringBuilder.append(adminRepository.createQuestion(q, questionSetId,questionId,questionSetIdGenerator,questionIdGenerator));
            }
            questionIdGenerator.setId(0);
            return stringBuilder.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String getAllQuestions() {
        return adminRepository.getAllQuestions();
    }

    @Override
    public String getQuestionSetById(String questionSetId) {
        try {
            if (questionSetId == null) {
                throw new IllegalArgumentException("Question Set Id can't be \"null\"");
            }
            return adminRepository.getQuestionSetById(questionSetId);
        } catch (Exception e) {
            return "Error : "+e.getMessage();
        }
    }

    @Override
    public String getQuestionById(String questionSetId, String questionId) {
        try {
            if (questionSetId == null) {
                throw new IllegalArgumentException("Question Set Id is \"null\"");
            }
            if (questionId == null) {
                throw new IllegalArgumentException("Question Id is \"null\"");
            }
            return adminRepository.getQuestionById(questionSetId, questionId);
        } catch (Exception e) {
            return "Error : "+e.getMessage();
        }
    }

    @Override
    public String updateQuestionsById(UpdateQuestions updateQuestions) {
        try {
            if (updateQuestions == null || updateQuestions.getQuestions().isEmpty()) {
                throw new IllegalArgumentException("Questions list cannot be empty");
            }
            if (updateQuestions.getQuestionSetId() == null) {
                throw new IllegalArgumentException("Question Set Id is \"null\"");
            }
            StringBuilder result = new StringBuilder();
            for (Question question : updateQuestions.getQuestions()) {
                    question.setQuestionId(question.getQuestionId());
                    result.append(adminRepository.updateQuestionById(updateQuestions.getQuestionSetId(), question.getQuestionId(), question));
            }
            return result.toString();
        } catch (Exception e) {
            return "Error : "+e.getMessage();
        }
    }

    @Override
    public String deleteQuestionById(String questionSetId, String questionId) {
        try {
            if (questionSetId == null) {
                throw new IllegalArgumentException("Question Set Id is \"null\"");
            }
            if (questionId == null) {
                throw new IllegalArgumentException("Question Id is \"null\"");
            }
            StringBuilder sb = new StringBuilder();
            String[] questionIds = questionId.split(",");
            for (String id : questionIds) {
                sb.append(adminRepository.deleteQuestionById(questionSetId, id));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error : "+e.getMessage();
        }
    }

    @Override
    public String evaluate(String candidateId) {
        return adminRepository.evaluate(candidateId);
    }

}
