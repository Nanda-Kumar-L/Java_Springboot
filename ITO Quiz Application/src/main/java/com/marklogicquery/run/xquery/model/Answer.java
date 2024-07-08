package com.marklogicquery.run.xquery.model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class Answer{

    private String id;

    private String candidate_id;

    private List<QuestionIdAndAnswer> objects;

}
