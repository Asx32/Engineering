/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asx
 */
public class StatDisplayer {
    //Pola
    /*
    Rozmiar - zależny od rozmiaru populacji (j wielkości sieci?)?
    ...a może skala -> zmieniać rozmiar fontu w zależności od ilości elementów
    */
    private Font font;
    //kolor?
    
    private ObjHandler popCurrent;
    private boolean update;
    private String[] stringsCurrent;
    private LinkedList<String> stringsLegacy;
    private LinkedList<String> stringsAvg;
    private String strBestAvg;
    private String strLastAvg;
    private int cycleCounter;
    private int startLegacy, endLegacy;
    private int yPosLegacy;
    private int yPosAvg;
    
    private int avgValue;
    private int lastAvgValue;
    private int bestAvgValue;
    private int deadCount;
    private int loggedCount;
    
    private DecimalFormat df;
    
    /*
    Zachowania są przekazywane jako nowa lista?
    Więc raczej trzeba będzie utworzyc lokalną listę j ją aktualizować?
    Albo utworzyć nową metodę przekazujacą zachowania...
    */
    
    public StatDisplayer(ObjHandler pop)
    {
        this.popCurrent = pop;
        
        this.font = new Font("arial", Font.PLAIN, 12);
        stringsCurrent = new String[0];
        stringsLegacy = new LinkedList<String>();
        update = false;
        cycleCounter = 0;
        startLegacy = 0;
        endLegacy = 0;
        yPosAvg = GUIConsts.statsY + 16*(GlobalVars.enemyCount +1);
        yPosLegacy = yPosAvg + 16*2;
        
        avgValue = 0;
        lastAvgValue = 0;
        bestAvgValue = 0;
        deadCount = 0;
        loggedCount = 0;
        
        stringsAvg = new LinkedList<String>();
        strLastAvg = "";
        strBestAvg = "-";
        
        df = new DecimalFormat("#.##");
        
        try {
            FileWriter file = new FileWriter(GlobalVars.logFile);
        } catch (IOException ex) {
            Logger.getLogger(StatDisplayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Update()
    {
        //System.out.println("StatDisplayer Update");
        this.update = true;
        
        //System.out.println("PopSize: " +popSize);
       
    }
    
    public void Reset()
    {
        update = false;
        cycleCounter = 0;
        startLegacy = 0;
        endLegacy = 0;
        
        avgValue = 0;
        lastAvgValue = 0;
        bestAvgValue = 0;
        deadCount = 0;
        
        stringsCurrent = new String[0];
        stringsLegacy.clear();
        stringsAvg.clear();
        strLastAvg = "";
        strBestAvg = "-";
        
        GlobalVars.enemyIdCounter = 0;
    }
    
    private void SaveToLog(int diff, boolean dump)
    {
        if(dump)
        {
            int n = stringsAvg.size();
            try(FileWriter file = new FileWriter(GlobalVars.logFile,true))
            {
                for(int i=0; i< n; i++)
                {
                    String intro = "### Gen:" +loggedCount++;
                    file.write(intro+"\n");
                    for(int j=0; j<GlobalVars.enemyCount; j++)
                    {
                        String s = stringsLegacy.pop();
                        file.write(s+"\n");
                    }
                    int k = stringsAvg.size()-1;
                    String tmp = "Avg value: " +stringsAvg.get(k);
                    file.write(tmp+"\n");
                    stringsAvg.remove(k);
                }
                
                n = stringsLegacy.size();
                if(n>0)
                {
                    String intro = "### Gen:" +loggedCount;
                    file.write(intro+"\n");
                    for(int i=0; i< n; i++)
                    {
                        String s = stringsLegacy.pop();
                        file.write(s+"\n");
                    }
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(StatDisplayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        if(diff >= GlobalVars.logBuffer)
        {
            try(FileWriter file = new FileWriter(GlobalVars.logFile,true))
            {
                //zapisz osobniki z pokolenia wykraczającego
                //-> n*m pierwszych osobników z listy?
                //albo osobniki od 0 do startLegacy
                //zapisz średnie z pokolenia wykraczającego
                //pomiędzy grupami n osobników
                for(int i=0; i< diff; i++)
                {
                    String intro = "### Gen:" +loggedCount++;
                    file.write(intro+"\n");
                    for(int j=0; j<GlobalVars.enemyCount; j++)
                    {
                        String s = stringsLegacy.pop();
                        file.write(s+"\n");
                    }
                    int k = stringsAvg.size()-1;
                    String tmp = "Avg value: " +stringsAvg.get(k);
                    file.write(tmp+"\n");
                    stringsAvg.remove(k);
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(StatDisplayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void DumpToLog()
    {
        this.SaveToLog(0, true);
    }
    
    public void SaveTheDead()
    {
        int popSize = popCurrent.getSize();
        for(int i=0; i<popSize; i++)
        {
            BasicEnemy specimen = ((BasicEnemy) popCurrent.get(i));
            if(!specimen.isAlive())
            {
                deadCount++;
                avgValue += specimen.getValue();
                if(deadCount == GlobalVars.enemyCount)
                {
                    deadCount = 0;
                    lastAvgValue = avgValue/GlobalVars.enemyCount;
                    avgValue = 0;
                    if(lastAvgValue > bestAvgValue)
                        bestAvgValue = lastAvgValue;
                    stringsAvg.push(Integer.toString(lastAvgValue));
                    strBestAvg = Integer.toString(bestAvgValue);
                    strLastAvg = "";
                    
                    int avgsToShow = stringsAvg.size();
                    if(avgsToShow > GlobalVars.generationsToShow)   //tu wywołaj metodę zapisującą do pliku... ale czy za każdym razem?
                    {
                        int delta = avgsToShow - GlobalVars.generationsToShow;
                        avgsToShow = GlobalVars.generationsToShow;
                        SaveToLog(delta,false);
                    }
                    for(int j=0; j< avgsToShow; j++)
                    {
                        strLastAvg += stringsAvg.get(j);
                        if(j < avgsToShow-1)
                            strLastAvg += ", ";
                    }
                }
                String tmpStr = specimen.getId() +" = "+ specimen.getParents()[0] +" + "+ specimen.getParents()[1] +" : ";
                int n = specimen.getBehaviour().size();
                for(int j=0; j<n; j++)
                {
                    int move = specimen.getBehaviour().get(j);
                    tmpStr += Integer.toString(move);
                    if(j < n-1)
                        tmpStr += " | ";
                    else
                    {
                        tmpStr += " -> " +specimen.getValue();
                    }
                }
                this.stringsLegacy.add(tmpStr);
            }
        }
        
        int p = stringsLegacy.size() - GlobalVars.generationsToShow*GlobalVars.enemyCount;
        if(p > 0) startLegacy = p;
        else
            startLegacy = 0;
        if(stringsLegacy.size() > GlobalVars.generationsToShow*GlobalVars.enemyCount)
            endLegacy = GlobalVars.generationsToShow*GlobalVars.enemyCount;
        else
            endLegacy = stringsLegacy.size();
    }
    
    public void tick()
    {
        if(this.update)
        {
            this.update = false;
            this.cycleCounter = 0;
            
            int popSize = popCurrent.getSize();
             if(popSize >0)
            {
                this.stringsCurrent = new String[popSize];
                for(int i=0; i<popSize; i++)
                {
                    BasicEnemy specimen = ((BasicEnemy) popCurrent.get(i));
                    int n = specimen.getBehaviour().size();
                    stringsCurrent[i] = specimen.getId() +" = "+ specimen.getParents()[0] +" + "+ specimen.getParents()[1] +" : ";

                    for(int j=0; j<n; j++)
                    {
                        int move = specimen.getBehaviour().get(j);
                        stringsCurrent[i] += Integer.toString(move);
                        if(j < n-1)
                            stringsCurrent[i] += " | ";
                        else
                        {
                            stringsCurrent[i] += " -> " +specimen.getValue();
                        }
                    }
                }
            } 
        }
        else
        {
            if(this.cycleCounter < GlobalVars.cooldownEnemyCycle)
                this.cycleCounter++;
            else
            {
                this.update = true;
                this.cycleCounter = 0;
            }
        }
    }
    
    public void render(Graphics g)
    {
        g.setColor(Color.GRAY);
        g.drawLine(GlobalVars.gameWidth, 0, GlobalVars.gameWidth, GlobalVars.gameHeight);
        
        g.setFont(this.font);
        g.setColor(Color.CYAN);
        g.drawString("ID | Parents | Genes | Value", GUIConsts.statsX, GUIConsts.statsY);
        //wyświetl aktualną 
        for(int i=0; i< this.stringsCurrent.length; i++)
        {
            g.drawString(stringsCurrent[i], GUIConsts.statsX, GUIConsts.statsY+16+(16*i));
        }
        //średnie wartości
        g.drawString("Last avg values: "+strLastAvg, GUIConsts.statsX, yPosAvg);
        g.drawString("Best avg value: "+strBestAvg, GUIConsts.statsX, yPosAvg+16);
        //poprzednie osobniki
        g.drawString("Previous specimens:", GUIConsts.statsX, this.yPosLegacy);
        for(int i=0; i< this.endLegacy; i++)
        {
            g.drawString(stringsLegacy.get(i+ startLegacy), GUIConsts.statsX, this.yPosLegacy+16+(16*i));
        }
    }
}
