package com.example.passmeprofessor;

import android.widget.ImageView;

import java.util.Hashtable;
import java.util.Random;

public class Paper {

    Hashtable<Integer, Integer> paper_map = new Hashtable<>();

    private final ImageView view;
    private boolean pass;
    //Sprite Variable
    public Paper(ImageView img){
        view = img;
        //Add all paper sprite file names to hash table to get later
        paper_map.put(0, R.drawable.paper_a);
        paper_map.put(1, R.drawable.paper_b);
        paper_map.put(2, R.drawable.paper_c);
        paper_map.put(3, R.drawable.paper_d);
        paper_map.put(4, R.drawable.paper_e);
    }

    public boolean getIfPass(){
        return pass;
    }

    public void generateRandomPaper(){
        //Pick a new paper sprite to display when called
        Random rand = new Random();
        Integer val = rand.nextInt(5);
        view.setImageResource(paper_map.get(val));

    }
}
