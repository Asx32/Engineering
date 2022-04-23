/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 * Stany gry:
 * MENU - menu główne
 * GAME - właściwa gra
 * PAUSE - menu pauzy
 * LOAD - ekran ładowania?
 * INFO - ekran informacyjny
 * OPTIONS - ekran opcji
 * DEAD - koniec gry, porażka
 * @author Asx
 */
public enum GameState {
     MENU, GAME, PAUSE, LOAD, INFO, OPTIONS, DEAD, END;
}
