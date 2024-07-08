package com.marklogicquery.run.xquery.model;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Candidate {

    private String candidate_id;

    private String name;

    private String emailId;

    private boolean isStarted;

    private boolean isSubmit;
}
