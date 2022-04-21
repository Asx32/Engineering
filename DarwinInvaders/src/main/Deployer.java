/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.LinkedList;
import java.util.Random;

/**
 * Klasa do umieszczania wrogów
 * Ma przechowywać informacje o punktach "wejściowych" wrogów dla kilku fal.
 * @author Asx
 */
public class Deployer {
    private int waveNo; //numer aktualnej fali
    private LinkedList<Wave> waves;
    
    public Deployer()
    {
        waveNo =-1;
        waves = new LinkedList<Wave>();
    }
    
    /**
     * Zerowanie stanu obiektu na potrzeby ponownego rozpoczęcia gry
     * w tamach jednego uruchomienia programu.
     */
    public void reset()
    {
        waveNo =-1;
        waves.clear();
    }
    
    /**
     * Metoda przełączająca fale.
     * Jeśli nie wszystkie fale (w ramach założonego limitu) zostały wygenerowane - dodaje nową.
     * Jeśli wszystkie fale zostały obsłużone - dokonuje ich krzyżowania
     */
    public void nextWave()
    {
        waveNo++;
        if(waveNo >= GlobalVars.wavesTillCross)  
        {
            //jeśli osiągnięto limit fal,
            //to wyzeruj licznik i skrzyżuj fale
            waveNo = 0;
            this.cross();
        }
        else
            if(waveNo >= waves.size())  //jeśli mamy za mało fal...
            {
                waves.add(new Wave());
            }
    }
    
    /**
     * Metoda zwracająca pozycję poziomą dla danego wroga 
     * @param enemyNo Nr wroga w ramach aktualnej fali
     * @return Pozycja startowa
     */
    public int getX(int enemyNo)
    {
        int x =0;
        if((waveNo >= 0) && (enemyNo < GlobalVars.enemyCount))
        {
            x = waves.get(waveNo).getPosition(enemyNo);
            if(enemyNo == GlobalVars.enemyCount) waveNo++;
        }
        return x;
    }
    
    /**
     * Metoda zwracająca "watość" aktualnej fali
     * @return "Wartość" fali, służąca do oceny fali podczas krzyżowania
     */
    public int getValue()
    {
        return waves.get(waveNo).getValue();
    }
    
    /**
     * Metoda przypisująca aktualnej fali podaną "wartość"
     * @param val Nowa "wartość" dla aktualnej fali
     */
    public void setValue(int val)
    {
        waves.get(waveNo).setValue(val);
    }
    
    public int getNewId()
    {
        return GlobalVars.enemyIdCounter++;
    }
    
    private void cross()
    {
        LinkedList<Wave> tmpPop = new LinkedList<Wave>();
        LinkedList<Integer> tmpVal = new LinkedList<Integer>();
        
        Random rand = new Random();
        
        int n = waves.size();
        
        //selekcja
        double[] chances = new double[n];
        double valSum = 0.0;
        for(int i=0; i<n; i++)
        {
            chances[i] = waves.get(i).getValue();
            valSum += chances[i];
        }
        //oblicz prawdopodobieństwo wylosowania
        for(int i=0; i<n; i++)
            chances[i] /= valSum;
        //przekształć na dystrybuantę
        for(int i=1; i<n; i++)
            chances[i] += chances[i-1]; 
        //losowanie do następnej populacji/pokolenia(?)
        for(int i=0; i<n; i++)
        {
            double attempt = rand.nextDouble(); //losuj 0..1
            int pick =-1;	//wybrany element
            int j = 0;		//aktuajnie rozpatrywany element
            //wybierz pierwszy element, którego wart. dystrybuanty > od wylosowanej wart.
            //przeglądaj kolejne elementy do momentu wyboru lub do końca tablicy
            while((pick<0)&&(j<n))
            {
                if(chances[j]>=attempt) pick = j;
                j++;
            }
            //jeśli nie dokonano wyboru - przypisz ostatni element
            //Może zajść, jeśli dystrybuanta nie zsumuje się do 1.0
            if(pick<0) pick = n-1;
            tmpPop.add(waves.get(pick));
        }
        //wyczyść populacje i przepisz elementy z tab. pomocniczej
        waves.clear();
        for(int i=0; i<n; i++)
        {
            Wave tmpWave = new Wave(tmpPop.get(i).getEntryPoints());
            waves.add(tmpWave);
        }
        tmpPop.clear();
        
        while(n>1)
        {
            //losuj dwa klucze      - TO TRZEBA ZMIENIĆ! Musi uwzględniać wartość fali!!!
            int x1 = rand.nextInt(n);
            int x2 = rand.nextInt(n);
            if(x1!=x2)
            {
                LinkedList<Integer> a = waves.get(x1).getEntryPoints();
                LinkedList<Integer> b = waves.get(x2).getEntryPoints();
                //losuj czy krzyżują
                if((rand.nextInt(100)+1)<=GlobalVars.probMix)
                {
                    //znacznik pozycji
                    int pos =0;
                    //miejsce cięcia
                    int cut = rand.nextInt(GlobalVars.enemyCount-1)+1;
                    //listy tymczasowe
                    LinkedList<Integer> na = new LinkedList<Integer>();
                    LinkedList<Integer> nb = new LinkedList<Integer>();
                    //przeniesienie elementów...
                    for(int i=0; i<GlobalVars.enemyCount; i++)
                    {
                        if(i<cut)
                        {
                            //a -> na, b-> nb
                            na.add(a.get(i));
                            nb.add(b.get(i));
                        }
                        else
                        {
                            //a -> nb, b-> na
                            na.add(b.get(i));
                            nb.add(a.get(i));
                        }
                    }
                    a.clear();
                    a = na;
                    b.clear();
                    b = nb;
                }
                tmpPop.add(new Wave(a));
                tmpVal.add(waves.get(x1).getValue());
                tmpPop.add(new Wave(b));
                tmpVal.add(waves.get(x2).getValue());
                //dodaj do tab. tymczasowej, usuń z populacji, zmniejsz zakres
                waves.remove(x1);
                if(x1<x2) x2--;	//korekta, bo drugą wartość usuwamy z pomniejszonej tablicy
                waves.remove(x2);
                n -= 2;
            }
        }
        //System.out.println("Przepisywanie listy");
        //przepisz nowe osobniki z powrotem do populacji
        n = tmpPop.size();
        for(int k=0; k<n; k++)
        {
            waves.add(tmpPop.get(k));
            waves.get(k).setValue(tmpVal.get(k));
        }
        tmpPop.clear();
        tmpVal.clear();
        //mutacja
        n = waves.size();
        for(int i=0; i<n; i++) //dla każdego osobnika
        {
            for(int j=0; j<GlobalVars.enemyCount; j++)  //dla każdego genu
                if((rand.nextInt(100)+1)<=GlobalVars.probMutation)
                {
                    waves.get(i).getEntryPoints().set(j, (rand.nextInt(8)*GlobalVars.gameWidth)/8);
                }
        }
    }
}
