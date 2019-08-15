package com.modestasvalauskas.vaida.Fassade;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.kb.FOLKnowledgeBase;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */
public class AimaKnowledgeBase {

    private List<String> facts = new ArrayList<>();

    public AimaKnowledgeBase() {

    }

    public FOLKnowledgeBase runQuery(FOLDomain domain, InferenceProcedure ip) {

        StandardizeApartIndexicalFactory.flush();
        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain, ip);

        for (String fact: facts) {
            kb.tell(fact);
        }

        return kb;
    }

    public void tell(String string) {
        facts.add(string);
    }
}
