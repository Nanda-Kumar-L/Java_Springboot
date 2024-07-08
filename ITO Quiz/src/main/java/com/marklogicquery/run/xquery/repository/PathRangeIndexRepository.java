package com.marklogicquery.run.xquery.repository;


import com.marklogicquery.run.xquery.service.serviceImpl.MarkLogicConnectionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class PathRangeIndexRepository {

    @Autowired
    MarkLogicConnectionImpl mlc;

    public void createPathRangeIndexes() {
        StringBuilder stringBuilder=new StringBuilder();
        List<String> list= new ArrayList<>(Arrays.asList("/tXML/Questions/Id", "/tXML/Questions/Question/questionId",
                "/tXML/Candidate/candidate_id", "/tXML/Answer/Id", "/tXML/Answer/candidateId"));
        stringBuilder.append("import module namespace admin = \"http://marklogic.com/xdmp/admin\" at \"/MarkLogic/admin.xqy\";");
        for(String item : list)
        {
            stringBuilder.append("""
                            try{
                                             let $config := admin:get-configuration()
                                             let $dbid := xdmp:database("Documents")
                                             let $pri := admin:database-range-path-index($dbid,"string",""").
                    append(item).append("""
                            ,"http://marklogic.com/collation/",fn:false(),"ignore")
                                             let $apri := admin:database-add-range-path-index($config, $dbid, $pri)
                                             return admin:save-configuration($apri)}
                                             catch($e){},""");
        }
         mlc.executeXQuery(stringBuilder +"\"Path Range Indexes Successfully Created\"");
    }
}
