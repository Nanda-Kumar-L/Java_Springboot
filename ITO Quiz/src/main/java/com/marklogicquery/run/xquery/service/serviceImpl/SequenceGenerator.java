package com.marklogicquery.run.xquery.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceGenerator {
    @Service
    public static class AnswerIdGenerator {

        @Autowired
        MarkLogicConnectionImpl mlc;

        private final AtomicLong sequence = new AtomicLong();

        public long nextId() {
            return sequence.incrementAndGet();
        }

        public void setId(long initialValue) {
            sequence.set(initialValue);
        }

        public String fetchIdFromDatabase() {
            return mlc.executeXQuery("let $AnswerIdGenerator := data(doc(\"/itoquiz/SequenceGenerator\")/tXML/SequenceGenerator/AnswerIdGenerator)\n" +
                    "return if(exists($AnswerIdGenerator)) then $AnswerIdGenerator else \"0\"");
        }

        public void updateIdInDatabase(long id) {
            mlc.executeXQuery("xdmp:node-replace(doc(\"/itoquiz/SequenceGenerator\")/tXML/SequenceGenerator/AnswerIdGenerator, element AnswerIdGenerator{"+id+"})");
        }
    }

    @Service
    public static class CandidateIdGenerator {

        @Autowired
        MarkLogicConnectionImpl mlc;

        private final AtomicLong sequence = new AtomicLong();

        public long nextId() {
            return sequence.incrementAndGet();
        }

        public void setId(long initialValue) {
            sequence.set(initialValue);
        }

        public String fetchIdFromDatabase() {
            return mlc.executeXQuery("let $CandidateIdGenerator := data(doc(\"/itoquiz/SequenceGenerator\")/tXML/SequenceGenerator/CandidateIdGenerator)\n" +
                    "return if(exists($CandidateIdGenerator)) then $CandidateIdGenerator else \"0\"");
        }

        public void updateIdInDatabase(long id) {
            mlc.executeXQuery("xdmp:node-replace(doc(\"/itoquiz/SequenceGenerator\")/tXML/SequenceGenerator/CandidateIdGenerator, element CandidateIdGenerator{"+id+"})");
        }
    }

    @Service
    public static class QuestionIdGenerator {

        @Autowired
        MarkLogicConnectionImpl mlc;

        private final AtomicLong sequence = new AtomicLong();

        public long nextId() {
            return sequence.incrementAndGet();
        }

        public void setId(long initialValue) {
            sequence.set(initialValue);
        }

    }

    @Service
    public static class QuestionSetIdGenerator {

        @Autowired
        MarkLogicConnectionImpl mlc;

        private final AtomicLong sequence = new AtomicLong();

        public long nextId() {
            return sequence.incrementAndGet();
        }

        public void setId(long initialValue) {
            sequence.set(initialValue);
        }

        public String fetchIdFromDatabase() {
            mlc.executeXQuery("""
                    if(not(exists(doc("/itoquiz/SequenceGenerator")))) then
                    xdmp:document-insert("/itoquiz/SequenceGenerator",\s
                      element tXML{
                        element SequenceGenerator{
                            element QuestionSetIdGenerator{"0"},
                            element QuestionIdGenerator{"0"},
                            element CandidateIdGenerator{"0"},
                            element AnswerIdGenerator{"0"}
                          }
                        },\s
                      <options xmlns="xdmp:document-insert">
                      <collections>
                      <collection>SequenceGenerator</collection> \s
                      </collections>
                      </options>
                    )
                    else
                    (
                    )""");
            return mlc.executeXQuery("data(doc(\"/itoquiz/SequenceGenerator\")/tXML/SequenceGenerator/QuestionSetIdGenerator)");
        }

        public void updateIdInDatabase(long id) {
            mlc.executeXQuery("xdmp:node-replace(doc(\"/itoquiz/SequenceGenerator\")/tXML/SequenceGenerator/QuestionSetIdGenerator, element QuestionSetIdGenerator{"+id+"})");
        }
    }
}
