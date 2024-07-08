package com.marklogicquery.run.xquery.service;

import com.marklogicquery.run.xquery.model.Question;
import com.marklogicquery.run.xquery.model.UpdateQuestions;

import java.util.List;

public interface AdminService {
    /**
     *
     * @param questions
     * @return
     */
    String createQuestions(List<Question> questions);

    /**
     *
     * @return
     */
    String getAllQuestions();

    /**
     *
     * @param questionSetId
     * @return
     */
    String getQuestionSetById(String questionSetId);

    /**
     *
     * @param questionSetId
     * @param questionId
     * @return
     */
    String getQuestionById(String questionSetId, String questionId);

    /**
     *
     * @param updateQuestions
     * @return
     */
    String updateQuestionsById(UpdateQuestions updateQuestions);

    /**
     *
     * @param questionSetId
     * @param questionId
     * @return
     */
    String deleteQuestionById(String questionSetId, String questionId);

    /**
     *
     * @param candidateId
     * @return
     */
    String evaluate(String candidateId);
}
