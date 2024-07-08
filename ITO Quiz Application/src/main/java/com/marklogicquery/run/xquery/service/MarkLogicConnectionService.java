package com.marklogicquery.run.xquery.service;

public interface MarkLogicConnectionService {
    /**
     *
     * @param xqueryScript
     * @return
     */
    String executeXQuery(String xqueryScript);
}
