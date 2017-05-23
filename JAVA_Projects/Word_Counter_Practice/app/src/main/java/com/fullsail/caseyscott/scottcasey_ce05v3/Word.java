// Casey Scott
// JAVA 1 - 1704
// Word
package com.fullsail.caseyscott.scottcasey_ce05v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Word {

    ArrayList<String> words;
    ArrayList<String> wordsEntryOrder;


    double getMedian(){

        double sum = 0.0;


        //Sort the words
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String word2, String word1) {

                return word1.length() - word2.length();
            }
        });

        for (String s : words){

            sum = sum + s.length();
        }

        double median;

        if(this.words.size() % 2 == 0){
            //The words count is even

            median = ((double)words.get(words.size()/2).length() + (double)(words.get((words.size()/2)-1).length()))/2;

        }else{
            //The words count is odd

            median = words.get(words.size()/2).length();
        }

        return median;
    }

    double getAverage(){

        double average;

        double sum = 0.0;

        for (String s : words) {

            sum = sum + s.length();
        }
        average = sum/words.size();

        return average;
    }
}
