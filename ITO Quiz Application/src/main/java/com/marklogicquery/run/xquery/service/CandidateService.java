package com.marklogicquery.run.xquery.service;

import com.marklogicquery.run.xquery.model.Answer;
import com.marklogicquery.run.xquery.model.Candidate;

public interface CandidateService {
    /**
     *
     * @param candidate
     * @return
     */
    String createCandidate(Candidate candidate);

    /**
     *
     * @param candidateId
     * @param questionSetId
     * @return
     */
    String getQuestionSet(String candidateId, String questionSetId);

    /**
     *
     * @param candidateId
     * @param answer
     * @return
     */
    String submitAnswerSheet(String candidateId, Answer answer);
}
