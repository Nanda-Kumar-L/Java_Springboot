package com.marklogicquery.run.xquery.repository;

import com.marklogicquery.run.xquery.model.Question;
import com.marklogicquery.run.xquery.service.serviceImpl.SequenceGenerator;
import com.marklogicquery.run.xquery.service.serviceImpl.MarkLogicConnectionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class AdminRepository {

    @Autowired
    MarkLogicConnectionImpl mlc;

    public String getOptionElements(List<String> options) {
        StringBuilder sb = new StringBuilder();
        for (String s : options) {
            sb.append("element option{\"").append(s).append("\"},");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public void CreateQuestionSet(long questionSetId) {
        mlc.executeXQuery("if(not(exists(doc(\"/itoquiz/Questions/\"||\""+questionSetId+"\")))) then\n" +
                "xdmp:document-insert(\"/itoquiz/Questions/\"||\""+questionSetId+"\", \n" +
                "  element tXML{\n" +
                "    element Questions{\n" +
                "        element Id{\""+questionSetId+"\"}\n" +
                "      }\n" +
                "    }, \n" +
                "  <options xmlns=\"xdmp:document-insert\">\n" +
                "  <collections>\n" +
                "  <collection>Questions</collection>  \n" +
                "  </collections>\n" +
                "  </options>\n" +
                ") else ()");
    }

    public String createQuestion(Question q, long questionSetId, long questionId, SequenceGenerator.QuestionSetIdGenerator questionSetIdGenerator, SequenceGenerator.QuestionIdGenerator questionIdGenerator) {
        try{
            if(q.getOptions()==null)
                throw new Exception("Please enter all the fields");
            if(q.getOptions().size()!=4)
                throw new Exception("Minimum 4 options required");
            if(q.getAnswer() <= 0 || q.getAnswer()>4)
                throw new Exception("Please enter between 1 to 4 for answer field");
            if(q.getQuestionId() == null || q.getQuestion() == null)
                throw new Exception("Please enter all the fields");
            CreateQuestionSet(questionSetId);
            questionSetIdGenerator.updateIdInDatabase(questionSetId);
            return mlc.executeXQuery(
                    "xdmp:node-insert-child(cts:search(/tXML/Questions, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \""+ questionSetId +"\")))), \n" +
                            "element Question{  element questionId{\""+q.getQuestionId()+"\"}, element question{\""+q.getQuestion()+"\"},element options{"+getOptionElements(q.getOptions())+"},element answer{"+q.getAnswer()+"}  })"+
                            ",\"Question <" + q.getQuestionId() + "> generated successfully\""
            );
        }
        catch(Exception e){
            questionIdGenerator.setId(questionId-1);
            return "Error: "+"Failed to Generate Question <"+q.getQuestionId()+">  =>  "+e.getMessage()+"\n";
        }

    }


    public String getAllQuestions() {
        try{
            return mlc.executeXQuery("""
                    import module namespace json="http://marklogic.com/xdmp/json" at "/MarkLogic/json/json.xqy";
                    if(count(cts:search(/tXML/Questions,cts:collection-query("Questions"))) eq 0) then "Error : No Question available"
                    else
                    (
                    let $var := for $i in cts:search(/tXML/Questions,cts:collection-query("Questions"))
                              let $json := map:map()
                              let $Questions:=
                                for $j in $i/Question
                                let $Question := map:map()
                                let $_:= map:put($Question, "QuestionId", data($j/questionId))
                                let $_:= map:put($Question, "Question", data($j/question))
                                let $_:= map:put($Question, "Answer", data($j/answer))
                                let $_:= map:put($Question, "Options", data($j/options/option))
                                return xdmp:to-json($Question)
                              let $questionArray := json:to-array($Questions)
                              let $_ := map:put($json, "questions", $questionArray)
                              let $_ := map:put($json, "id", data($i/Id))
                              return xdmp:to-json($json)
                    return json:to-array($var)
                    )""");
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public String getQuestionSetById(String questionSetId) {
        return mlc.executeXQuery("let $questionSet := cts:search(/tXML/Questions, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \""+ questionSetId +"\"))))\n" +
                "return if(exists($questionSet)) then\n" +
                "(" +
                "let $json := map:map()\n" +
                "let $Questions:=\n" +
                "  for $question in $questionSet/Question\n" +
                "  let $Question := map:map()\n" +
                "  let $_:= map:put($Question, \"QuestionId\", data($question/questionId))\n" +
                "  let $_:= map:put($Question, \"Question\", data($question/question))\n" +
                "  let $_:= map:put($Question, \"Answer\", data($question/answer))\n" +
                "  let $_:= map:put($Question, \"Options\", data($question/options/option))\n" +
                "  return xdmp:to-json($Question)\n" +
                "let $questionArray := json:to-array($Questions)\n" +
                "let $_ := map:put($json, \"questions\", $questionArray)\n" +
                "let $_ := map:put($json, \"questionSetId\", data($questionSet/Id))\n" +
                "return xdmp:to-json($json)" +
                ")\n" +
                "else\n" +
                "  \"Error : Invalid Question Set ID\"");
    }

    public String getQuestionById(String questionSetId, String questionId) {
        return mlc.executeXQuery("let $questionSet := cts:search(/tXML/Questions, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \""+questionSetId+"\"))))\n" +
                "return if(exists($questionSet)) then\n" +
                "(\n" +
                "let $question := $questionSet/Question[questionId eq \""+questionId+"\"]\n" +
                "return if(exists($question)) then\n" +
                "  (\n" +
                "    let $json := map:map()\n" +
                "    let $Questions:=\n" +
                "      let $Question := map:map()\n" +
                "      let $_:= map:put($Question, \"QuestionId\", data($question/questionId))\n" +
                "      let $_:= map:put($Question, \"Question\", data($question/question))\n" +
                "      let $_:= map:put($Question, \"Answer\", data($question/answer))\n" +
                "      let $_:= map:put($Question, \"Options\", data($question/options/option))\n" +
                "      return xdmp:to-json($Question)\n" +
                "    let $questionArray := json:to-array($Questions)\n" +
                "    let $_ := map:put($json, \"questions\", $questionArray)\n" +
                "    let $_ := map:put($json, \"id\", data($questionSet/Id))\n" +
                "    return xdmp:to-json($json)\n" +
                "  )\n" +
                "  else \"Error : Invalid Question ID\"\n" +
                ")\n" +
                "else \"Error : Invalid Question Set ID\"");
    }

    public String updateQuestionById(String questionSetId, String questionId, Question question) {
        try{
            if(questionId==null)
                throw new IllegalArgumentException("Question Id is \"null\"");
            if(question.getOptions()==null)
                throw new Exception("Please enter all the fields");
            if(question.getOptions().size()!=4)
                throw new Exception("Minimum 4 options required");
            if(question.getAnswer() <= 0 || question.getAnswer()>4)
                throw new Exception("Please enter between 1 to 4 for answer field");
            if(question.getQuestion() == null)
                throw new Exception("Please enter all the fields");
            if(Boolean.parseBoolean(mlc.executeXQuery("xs:string(not(exists(cts:search(/tXML/Questions, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \""+questionSetId+"\")))))))").trim()))
                throw new Exception("Invalid Question Set Id");
            if(Boolean.parseBoolean(mlc.executeXQuery("xs:string(not(exists(cts:search(/tXML/Questions, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \""+questionSetId+"\"))))) and exists(cts:search(/tXML/Questions/Question, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Question/questionId\", \"=\", \""+questionId+"\")))))))").trim()))
                throw new Exception("Invalid Question Id");

            return mlc.executeXQuery("let $a := cts:search(/tXML/Questions, cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \"" + questionSetId + "\"))))\n" +
                    "return (\n" +
                    "xdmp:node-replace($a/Question[questionId eq \"" + questionId + "\"]/questionId, element questionId{\"" + questionId + "\"}),\n" +
                    "xdmp:node-replace($a/Question[questionId eq \"" + questionId + "\"]/question, element question{\"" + question.getQuestion() + "\"}),\n" +
                    "xdmp:node-replace($a/Question[questionId eq \"" + questionId + "\"]/options, element options{" + getOptionElements(question.getOptions()) + "}),\n" +
                    "xdmp:node-replace($a/Question[questionId eq \"" + questionId + "\"]/answer, element answer{\"" + question.getAnswer() + "\"})\n" +
                    "),\n" +
                    "'Question <questionSetId=\"" + questionSetId + "\",questionId=\"" + questionId + "\"> Updated successfully'"
            );
        }
        catch (IllegalArgumentException i){
            return "Error : " + i.getMessage() + "\n";
        }
        catch(Exception e){
            return "Error : Failed to Update Question <questionSetId=\""+questionSetId+"\",questionId=\""+questionId+"\">  =>  "+e.getMessage()+"\n";
        }
    }


    public String deleteQuestionById(String questionSetId, String questionId) {
        try{
            return mlc.executeXQuery("let $question := cts:search(/tXML/Questions,cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Id\", \"=\", \"" + questionSetId + "\"))))/Question[questionId eq \"" + questionId + "\"]\n" +
                    "return \n" +
                    "if(exists($question))\n" +
                    "then (xdmp:node-delete($question),'Question <questionSetId=\"" + questionSetId + "\",questionId=\"" + questionId + "\"> Deleted successfully')\n" +
                    "else\n" +
                    "'Error : Question <questionSetId=\"" + questionSetId + "\",questionId=\"" + questionId + "\"> does not exist'");
        }
        catch (Exception e){
            return "Error : Failed to Delete Question <questionSetId=\""+questionSetId+"\",questionId=\""+questionId+"\">  =>  "+"Error: "+e.getMessage()+"\n";
        }
    }

    public String evaluate(String candidateId) {
        try {
            if (Objects.equals(candidateId, "")||candidateId==null)
                throw new Exception("Please enter candidate Id");
            return mlc.executeXQuery("let $candidate := cts:search(/tXML/Candidate,cts:and-query((cts:collection-query(\"Candidate\"),cts:path-range-query(\"/tXML/Candidate/candidate_id\", \"=\", \"" + candidateId + "\"))))\n" +
                    "return\n" +
                    "if(exists($candidate)) then\n" +
                    "(\n" +
                    "  if($candidate/isStarted eq true() and $candidate/isSubmit eq true()) then\n" +
                    "  (" +
                    "    let $_ := xdmp:node-replace($candidate/isStarted, element isStarted{false()})\n" +
                    "    let $_ := xdmp:node-replace($candidate/isSubmit, element isSubmit{false()})\n" +
                    "      let $questionIdsAndAnswers := cts:search(/tXML/Answer,cts:and-query((cts:collection-query(\"Answers\"),cts:path-range-query(\"/tXML/Answer/candidateId\", \"=\", $candidate/candidate_id))))/questionIdsAndAnswers\n" +
                    "      let $marks :=\n" +
                    "      let $count := (0)\n" +
                    "        for $questionIdAndAnswerObject in $questionIdsAndAnswers/Object\n" +
                    "        let $storedQuestionInDatabase := cts:search(/tXML/Questions/Question,cts:and-query((cts:collection-query(\"Questions\"),cts:path-range-query(\"/tXML/Questions/Question/questionId\", \"=\", $questionIdAndAnswerObject/questionId))))\n" +
                    "        let $_ := if($questionIdAndAnswerObject/answer eq $storedQuestionInDatabase/answer) then xdmp:set($count, $count + 1) else ()\n" +
                    "        return $count\n" +
                    "  let $score:= fn:max($marks)\n" +
                    "let $_ := xdmp:document-delete($questionIdsAndAnswers/../../.. ! document-uri(.))" +
                    "  return \n" +
                    "  (if($score > 6) then \n" +
                    "      \"<\"||$candidate/candidate_id/string()||\"> : <\"||$candidate/name/string()||\"> is selected for next Round.&#10;\"\n" +
                    "    else \n" +
                    "      \"<\"||$candidate/candidate_id/string()||\"> : <\"||$candidate/name/string()||\"> is rejected in this Round.&#10;\")\n" +
                    "      ||\"Correct Answer: \"||$score||\"&#10;\"\n" +
                    "      ||\"Incorrect Answer: \"||(10 - $score)\n" +
                    "    )\n" +
                    "    else \"Error : Exam is not submitted\"\n" +
                    ")\n" +
                    "else \"Error : Candidate Id doesn’t exist\"");
        } catch (Exception e) {
            return "Error: "+e.getMessage()+"\n";
        }
    }



}

