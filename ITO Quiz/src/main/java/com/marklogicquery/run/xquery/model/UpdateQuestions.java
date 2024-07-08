package com.marklogicquery.run.xquery.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateQuestions {

    private String questionSetId;

    private List<Question> questions;

}
