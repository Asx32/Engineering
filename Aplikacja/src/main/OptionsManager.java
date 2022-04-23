/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.LinkedList;

/**
 *
 * @author Asx
 */
public class OptionsManager {
    
    public static enum OptionsIndex
    {
        EnemiesPerWave, GenomeSize, CrossingProb, MutationProb, WavePopulationSize
    }
    
    /* Dostępne opcje
    Liczba wrogów w fali
    Rozmiar genomu
    
    Pdp krzyżowania
    Pdp mutacji
    
    Liczba fal do krzyżowania
    */
    //...na liście + enum
    
    private int[] options;  //czy lista?
    
    public OptionsManager()
    {
        int n = OptionsIndex.values().length;
        options = new int[n];
    }
    
    public int getSize()
    {
        return options.length;
    }
    
    public int getValue(int optNo)
    {
        if(optNo< options.length)
            return options[optNo];
        return Integer.MIN_VALUE;
    }
    
    //zmiana wartości - interakcja z menu

    /**
     * Zwiększ o 1 wartość wskazanej opcji
     * @param optNo Nr opcji
     */
    public void incValue(int optNo)
    {
        if(optNo < options.length)
            if(options[optNo]<100)
                options[optNo]++;
    }
    
    /**
     * Zmniejsz o 1 wartość wskazanej opcji
     * @param optNo Nr opcji
     */
    public void decValue(int optNo)
    {
        if(optNo < options.length)
            if(options[optNo]>0)
                options[optNo]--;
    }
    
    /**
     * Odczyt danych ze zmiennych
     */
    public void loadValues()
    {
        options[OptionsIndex.EnemiesPerWave.ordinal()] = GlobalVars.enemyCount;
        options[OptionsIndex.GenomeSize.ordinal()] = GlobalVars.behaviourSize;
        options[OptionsIndex.CrossingProb.ordinal()] = GlobalVars.probMix;
        options[OptionsIndex.MutationProb.ordinal()] = GlobalVars.probMutation;
        options[OptionsIndex.WavePopulationSize.ordinal()] = GlobalVars.wavesTillCross;
    }
    
    /**
     * Zapis do zmiennych
     */
    public void saveValues()
    {
        GlobalVars.enemyCount = options[OptionsIndex.EnemiesPerWave.ordinal()];
        GlobalVars.behaviourSize = options[OptionsIndex.GenomeSize.ordinal()];
        GlobalVars.probMix = options[OptionsIndex.CrossingProb.ordinal()];
        GlobalVars.probMutation = options[OptionsIndex.MutationProb.ordinal()];
        GlobalVars.wavesTillCross = options[OptionsIndex.WavePopulationSize.ordinal()];
    }
    
    //zapis do pliku
    
}
