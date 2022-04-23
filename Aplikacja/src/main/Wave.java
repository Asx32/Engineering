/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.LinkedList;
import java.util.Random;

/**
 * Klasa służąca do przechowywania i obsługi danych dotyczących fali orzeciwników.
 * Zawiera listę pozycji wejściowych dla wrogów oraz "wartość" fali słuzącą do oceny danej fali
 * w ramach alorytmu genetycznego.
 * @author Asx
 */
public class Wave {
    
    private LinkedList<Integer> EntryPoints;
    private int value;
    
    public Wave()
    {
        EntryPoints = new LinkedList<Integer>();
        value = 10;
        
        Random rand = new Random();
        int n = GlobalVars.enemyCount;
        for(int i=0; i<n; i++)
        {
            EntryPoints.add((rand.nextInt(GlobalVars.enemyEntryPointNo)*GlobalVars.gameWidth)/GlobalVars.enemyEntryPointNo);
        }
    }
    
    public Wave(LinkedList<Integer> points)
    {
        value = 10;
        EntryPoints = new LinkedList<Integer>();
        int n = GlobalVars.enemyCount;
        if(n <= points.size())
            for(int i=0; i<n; i++)
            {
                EntryPoints.add(points.get(i));
            }
    }
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int val)
    {
        value = val;
    }
    
    public LinkedList<Integer> getEntryPoints()
    {
        return EntryPoints;
    }
    
    public void setEntryPoints(LinkedList<Integer> points)
    {
        EntryPoints.clear();
        int n = GlobalVars.enemyCount;
        if(n <= points.size())
            for(int i=0; i<n; i++)
            {
                EntryPoints.add(points.get(i));
            }
    }
    
    public int getPosition(int posNo)   //o ile posNo nie przekracza długości listy...
    {
        return EntryPoints.get(posNo);
    }
}
