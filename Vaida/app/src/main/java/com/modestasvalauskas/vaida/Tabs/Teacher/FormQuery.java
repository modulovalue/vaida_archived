package com.modestasvalauskas.vaida.Tabs.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Created by Modestas Valauskas on 19.09.2016.
 */
public class FormQuery {

    public List<String> args = new ArrayList<>();

    //how many Arguments should trigger which predicate
    public Map<Integer, String> predTriggerCount = new HashMap<>();

    //from the argument, which should be displayed
    public Map<String, Integer> answerStartDisplayArgumentCount = new HashMap<>();

    //how Many Arguments that Predicate has
    public Map<String, Integer> predArgCount = new HashMap<>();

    public static String[] randomVars = new String[]{
            "a","b","c","d","e","f","g",
            "h","i","j","k","l","m","n",
            "o","p","q","r","s","t","u",
            "v","w","x","y","z"};

    public String getQuery() {
        String predicate = predTriggerCount.get(args.size());

        if(predicate != null) {
            int countOfPredArg = -1;
            countOfPredArg = predArgCount.get(predicate);
                if(countOfPredArg != -1) {
                    String query = "";

                    query += predicate;
                    query += "(";

                    for (String arg : args) {
                        query += arg;
                        query += ",";
                    }
                    int countArgsLeft = countOfPredArg - args.size();

                    for (int i = countArgsLeft; i > 0; i--) {
                        query += randomVars[i];
                        query += ",";
                    }

                    query = query.substring(0, query.length() - 1);
                    query += ")";


                    return query;
                }
        }

        return "";
    }

    public ArrayList<ArrayList<String>> getVars(TeacherClass teacherClass, String query) {
        ArrayList<ArrayList<String>> vars = new ArrayList<>();

        InferenceResult result = teacherClass.ask(query);

        for (Proof p : result.getProofs()) {

            System.out.println("ANSWER BIDNGINS FORREAL " + p.getAnswerBindings());
            ArrayList<String> temp = new ArrayList<>();

            for (Map.Entry<Variable, Term> entry : p.getAnswerBindings().entrySet()) {
                Variable key = entry.getKey();
                Term value = entry.getValue();

                temp.add(value.toString());
                System.out.println("key " + key);
                System.out.println("value " + value);
            }
            vars.add(temp);

        }
        System.out.println("ARRRRR " + vars);
        System.out.println("ARRRRR result proofs " + result.getProofs());

        return vars;
    }

    public String getPred() {
        return predTriggerCount.get(args.size());
    }

    public void putPred(String pred, int triggerCount, int predArgCountNr, int displayFromArgumentNumber) {
        predTriggerCount.put(triggerCount,pred);
        predArgCount.put(pred,predArgCountNr);
        //TODO THAT ANSWER START DISPLAY ARGUMENT COUNT
        answerStartDisplayArgumentCount.put(pred,displayFromArgumentNumber);
    }

}
