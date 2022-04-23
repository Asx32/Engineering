/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.LinkedList;
import java.util.Random;

/**
 * Klasa odpowiadajaca za krzyżowanie wzorców zachowań wrogów
 * @author Asx
 */
public class CrossingManager {
    private LinkedList<LinkedList<Integer>> behaviours;
    private LinkedList<Integer> values;
    private LinkedList<Integer> ids;
    private LinkedList<Integer[]> parents;
    
    public CrossingManager()
    {
        behaviours = new LinkedList<LinkedList<Integer>>();
        values = new LinkedList<Integer>();
        ids = new LinkedList<Integer>();
        parents = new LinkedList<Integer[]>();
        
        Random r = new Random();
        for(int i=0; i<GlobalVars.enemyCount; i++)
        {
            behaviours.add(new LinkedList<Integer>());
            values.add(10);
            ids.add(-1);
            
            Integer[] tmpInt = new Integer[2];
            tmpInt[0] = tmpInt[1] = -1;
            parents.add(tmpInt);
            
            for(int j=0; j<GlobalVars.behaviourSize; j++)
            {
                int m = r.nextInt(3);
                behaviours.get(i).add(m);
            }
        }
    }
    
    /**
     * Pobiera "martwych" wrogów i zapisuje ich zachowanie do późniejszego krzyżowania
     * Zakładamy, że wszystkie listy zostały wcześniej opróżnione
     * @param handler Kontener przechowujący wrogów
     */
    public void collectSpecimens(ObjHandler handler)
    {
        int n = handler.getSize();
        for(int i=0; i<n; i++)
        {
            GameObject spec = handler.get(i);
            if(!spec.isAlive()) //pobieramy tylko te, które już odpadły z gry
            {
                behaviours.add(((BasicEnemy)spec).copyBehaviour());
                values.add(((BasicEnemy)spec).getValue());
                ids.add(((BasicEnemy)spec).getId());
                
                Integer[] tmpInt = new Integer[2];
                int[] tmpint = ((BasicEnemy)spec).getParents();
                tmpInt[0] = tmpint[0];
                tmpInt[1] = tmpint[1];
                parents.add(tmpInt);
            }
        }
    }
    
    public void loadSpecimens(LinkedList<LinkedList<Integer>> lBehaviours, LinkedList<Integer> lIds, LinkedList<Integer[]> lParents)
    {
        int n = lBehaviours.size();
        for(int i=0; i<n; i++)
        {
            behaviours.add(lBehaviours.get(i));
            values.add(10);
            ids.add(lIds.get(i));
            Integer[] tmpInt = new Integer[2];
            tmpInt[0] = lParents.get(i)[0];
            tmpInt[1] = lParents.get(i)[1];
            parents.add(tmpInt);
        }
    }
    
//    public void injectGenes(ObjHandler handler)
//    {
//        int n = behaviours.size(); //powinno być równe handler.getSize()
//        for(int i=0; i<n; i++)
//        {
//            ((BasicEnemy)handler.get(i)).setBehaviour(behaviours.get(i));
//        }
//        behaviours.clear();
//        values.clear();
//    }

    /**
     * Wprowadza do podanego obiektu listę zachowań ze wskazanej pozycji
     * @param spec Obiekt do którego zostaną wprowadzone zachowania
     * @param no Nr obiektu/pozycji z listy zachowań
     */ 
    public void injectGenes(GameObject spec, int no)
    {
        if(no < this.behaviours.size())
        {
            //System.out.println("Wprowadzam geny do osobnika " +no+ " z " +behaviours.size());
            ((BasicEnemy) spec).setBehaviour(behaviours.get(no));
            ((BasicEnemy) spec).setParents(parents.get(no)[0], parents.get(no)[1]);
        }
        else
        {
            //utwórz nową listę zachowań i przekaż osobnikowi z rodzizcami (-1,-1)
            Random r = new Random();
            LinkedList<Integer> behaviour = new LinkedList<Integer>();
            for(int j=0; j<GlobalVars.behaviourSize; j++)
            {
                int m = r.nextInt(3);
                behaviour.add(m);
                //System.out.print(" "+m);
            }
            ((BasicEnemy) spec).setBehaviour(behaviour);
            ((BasicEnemy) spec).setParents(-1,-1);
        }
    }
    
    /**
     * Usuwa z list stare elementy
     */
    public void clear()
    {
        //System.out.println("Czyszczenie list zachowań");
        int n = behaviours.size();
        //if(n < GlobalVars.enemyCount) n = GlobalVars.enemyCount;
        
        for(int i=0; i<n; i++) 
        {
            behaviours.get(0).clear();
            behaviours.remove(0);
            parents.remove(0);
        }
        //behaviours.clear();
        values.clear();
        ids.clear();
    }
    
    /**
     * Krzyżowanie w aktualnej populacji
     */
    public void crossSpecimens()
    {
        //System.out.println("Krzyżuję osobniki");
        LinkedList<LinkedList<Integer>> tmpPop = new LinkedList<LinkedList<Integer>>();
        LinkedList<Integer> tmpVal = new LinkedList<Integer>();
        LinkedList<Integer> tmpId = new LinkedList<Integer>();
        LinkedList<Integer[]> tmpParentsList = new LinkedList<Integer[]>();
        
        Random rand = new Random();
        
        int n = behaviours.size();
        
        //selekcja
        double[] chances = new double[n];
        double valSum = 0.0;
        for(int i=0; i<n; i++)
        {
            chances[i] = values.get(i);
            valSum += chances[i];
        }
        //oblicz prawdopodobieństwo wylosowania
        for(int i=0; i<n; i++)
            chances[i] /= valSum;
        //przekształć na dystrybuantę
        for(int i=1; i<n; i++)
            chances[i] += chances[i-1];
        //losowanie do krzyżowania
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
            tmpPop.add(behaviours.get(pick));
            tmpId.add(ids.get(pick));
        }
        //wyczyść populacje i przepisz elementy z tab. pomocniczej
        behaviours.clear();
        ids.clear();
        for(int i=0; i<n; i++)
        {
            LinkedList<Integer> tmp1 = new LinkedList<Integer>();
            LinkedList<Integer> tmp2 = tmpPop.get(i);
            for(int j=0; j< GlobalVars.behaviourSize; j++)
            {
                tmp1.add(tmp2.get(j));
            }
            behaviours.add(tmp1);
            ids.add(tmpId.get(i));
        }
        tmpPop.clear();
        tmpId.clear();
        
        //Sprawdzenie!
        /*
        for(int i=0; i<n; i++)
        {
            System.out.println("Zachowanie " +i);
            for(int j=0; j<GlobalVars.behaviourSize; j++)
            {
                int m = behaviours.get(i).get(j);
                System.out.print(" "+m);
            }
            System.out.println();
            System.out.println("Pozycji: " + behaviours.get(i).size());
        }   */
        
        //krzyżowanie
        while(n>1)
        {
            //losuj dwa klucze
            int x1 = rand.nextInt(n);
            int x2 = rand.nextInt(n);
            if(x1!=x2)	//jeśli różne...
            {
                LinkedList<Integer> a = behaviours.get(x1);
                LinkedList<Integer> b = behaviours.get(x2);
                
                //losuj czy krzyżują
                if((rand.nextInt(100)+1)<=GlobalVars.probMix)
                {
                    //miejsce cięcia
                    int cut = rand.nextInt(GlobalVars.behaviourSize-1)+1;
                    //listy tymczasowe
                    LinkedList<Integer> na = new LinkedList<Integer>();
                    LinkedList<Integer> nb = new LinkedList<Integer>();
                    //przeniesienie elementów...
                    for(int i=0; i<GlobalVars.behaviourSize; i++)
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
                    //zapisz id rodziców
                    Integer[] tmpParents = new Integer[2];
                    tmpParents[0] = ids.get(x1);
                    tmpParents[1] = ids.get(x2);
                    tmpParentsList.add(tmpParents);
                    tmpParents = new Integer[2];
                    tmpParents[0] = ids.get(x2);
                    tmpParents[1] = ids.get(x1);
                    tmpParentsList.add(tmpParents);
                }
                else
                {
                    //zapisz id rodziców
                    Integer[] tmpParents = new Integer[2];
                    tmpParents[0] = ids.get(x1);
                    tmpParents[1] = ids.get(x1);
                    tmpParentsList.add(tmpParents);
                    tmpParents = new Integer[2];
                    tmpParents[0] = ids.get(x2);
                    tmpParents[1] = ids.get(x2);
                    tmpParentsList.add(tmpParents);
                }
                tmpPop.add(a);
                //tmpVal.add(values.get(x1));
                tmpPop.add(b);
                //tmpVal.add(values.get(x2));
                
                //dodaj do tab. tymczasowej, usuń z populacji, zmniejsz zakres
                behaviours.remove(x1);
                values.remove(x1);
                ids.remove(x1);
                parents.remove(x1);
                if(x1<x2) x2--;	//korekta, bo drugą wartość usuwamy z pomniejszonej tablicy
                behaviours.remove(x2);
                values.remove(x2);
                ids.remove(x2);
                parents.remove(x2);
                n -= 2;
            }
        }
        //przepisz nowe osobniki z powrotem do populacji
        n = tmpPop.size();
        for(int k=0; k<n; k++)
        {
            behaviours.add(tmpPop.get(k));
            //values.add(tmpVal.get(k));
            parents.add(tmpParentsList.get(k));
        }
        tmpPop.clear();
        tmpVal.clear();
        tmpParentsList.clear();
        //mutacja
        n = behaviours.size();
        for(int i=0; i<n; i++) //dla każdego osobnika
        {
            for(int j=0; j<GlobalVars.behaviourSize; j++)  //dla każdego genu
                if((rand.nextInt(100)+1)<=GlobalVars.probMutation)
                {
                    behaviours.get(i).set(j, rand.nextInt(3));
                }
        }
        
        //Do przeniesienia
        //selekcja
        //double[] chances = new double[n];
        //double valSum = 0.0;
        /*
        for(int i=0; i<n; i++)
        {
            chances[i] = values.get(i);
            valSum += chances[i];
        }
        */
        //oblicz prawdopodobieństwo wylosowania
        //for(int i=0; i<n; i++)
        //    chances[i] /= valSum;
        //przekształć na dystrybuantę
        //for(int i=1; i<n; i++)
        //    chances[i] += chances[i-1]; 
        //losowanie do następnej populacji/pokolenia(?)
        /*
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
            tmpPop.add(behaviours.get(pick));
        }
        //wyczyść populacje i przepisz elementy z tab. pomocniczej
        behaviours.clear();
        for(int i=0; i<n; i++) behaviours.add(tmpPop.get(i));
        tmpPop.clear();
        */
    }
}
