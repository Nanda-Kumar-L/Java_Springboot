package com.marklogicquery.run.xquery.service.serviceImpl;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.eval.EvalResult;
import com.marklogic.client.eval.EvalResultIterator;
import com.marklogic.client.eval.ServerEvaluationCall;
import com.marklogicquery.run.xquery.service.MarkLogicConnectionService;
import org.springframework.stereotype.Service;

@Service
public class MarkLogicConnectionImpl implements MarkLogicConnectionService {

    private final DatabaseClient client;

    public MarkLogicConnectionImpl() {
        client = DatabaseClientFactory.newClient(
                "localhost", 7500, "Documents",
                new DatabaseClientFactory.DigestAuthContext("admin", "admin"));
    }

    @Override
    public String executeXQuery(String xqueryScript) {
        try {
            ServerEvaluationCall call = client.newServerEval();
            call.xquery(xqueryScript);
            EvalResultIterator resultIterator = call.eval();

            StringBuilder result = new StringBuilder();
            while (resultIterator.hasNext()) {
                EvalResult evalResult = resultIterator.next();
                result.append(evalResult.getString()).append("\n");
            }
            return result.toString();
        } catch (Exception e) {
            return "Error executing XQuery: " + e.getMessage() + "\n";
        }
    }
}
