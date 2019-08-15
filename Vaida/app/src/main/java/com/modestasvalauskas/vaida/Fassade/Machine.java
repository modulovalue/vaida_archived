package com.modestasvalauskas.vaida.Fassade;

import android.widget.TextView;

import com.modestasvalauskas.vaida.Tabs.adapter.KBFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.kb.FOLKnowledgeBase;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */
public class Machine {

    AimaDomain domain = new AimaDomain();
    AimaKnowledgeBase kb = new AimaKnowledgeBase();
    private List<String> facts = new ArrayList<>();

    public Machine(KBFile kbFile) {

        for (String temp : kbFile.getConstants()) {
            addConstant(temp);
        }

        for (String temp : kbFile.getPredicates()) {
            addPredicate(temp);
        }

        for (String temp : kbFile.getFunctions()) {
            addFunction(temp);
        }

        for (String temp : kbFile.getFacts()) {
            tell(temp);
        }

    }

    public void addConstant(String string) {
        domain.getDomain().addConstant(string);
    }

    public void addPredicate(String string) {
        domain.getDomain().addPredicate(string);
    }

    public void addFunction(String string) {
        domain.getDomain().addFunction(string);
    }

    public Set<String> getConstant() {
        return domain.getDomain().getConstants();
    }

    public Set<String> getPredicate() {
        return domain.getDomain().getPredicates();
    }

    public Set<String> getFunction() {
        return domain.getDomain().getFunctions();
    }

    public void tell(String string) {
        facts.add(string);
    }

    public void ask(String query, TextView tv) {

        StandardizeApartIndexicalFactory.flush();
        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain.getDomain(), new FOLBCAsk());

        for (String fact: facts) {
            kb.tell(fact);
        }
        InferenceResult answer = kb.ask(query);

        //tv.append("Knowledge Base:\n" +  "\"");
        //tv.append(kb.toString()+" \n");
        tv.append("Query: " + query + " \n");

        if(answer.getProofs().size() > 0) {
            for (Proof p : answer.getProofs()) {
                tv.append(p.getAnswerBindings().toString()+"\n");
            }
        } else {
            tv.append("No Proof found \n");
        }
        //for (Proof p : answer.getProofs()) {
        //    tv.append(ProofPrinter.printProof(p));
        //    tv.append("");
        //}

    }
    public InferenceResult ask(String query, InferenceProcedure ip) {
        StandardizeApartIndexicalFactory.flush();
        FOLKnowledgeBase kb = new FOLKnowledgeBase(domain.getDomain(), ip);
        for (String fact: facts) {
            kb.tell(fact);
        }
        return kb.ask(query);
    }
}
