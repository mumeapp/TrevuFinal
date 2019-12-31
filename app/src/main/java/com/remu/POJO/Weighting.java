package com.remu.POJO;

import java.util.ArrayList;

public class Weighting {
    private final double weightDistance=-0.3 , weightGoogleRate=0.167, weightTrevuRate=0.25, weightIntencity=0.25;

    public ArrayList<Double> doWeighting(double []distance, double []googleRate, double []trevuRate, double []intencity){
        ArrayList<Double> s= new ArrayList<Double>();
        double pembagi=0;
        ArrayList<Double> v = new ArrayList<Double>();
        for(int i=0; i<=distance.length; i++){
            s.add(Math.pow(distance[i], weightDistance)*
                    Math.pow(googleRate[i], weightGoogleRate)*
                    Math.pow(trevuRate[i], weightTrevuRate)*
                    Math.pow(intencity[i], weightIntencity));
            pembagi += s.get(i);
        }
        for (int i=0; i<=s.size(); i++){
            v.add(s.get(i)/pembagi);
        }
        return v;
    }
}
