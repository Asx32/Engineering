/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;

/**
 * Główna klasa programu, odpowiedzialna za działanie gry.
 * @author Asx
 */
public class GameController extends Canvas implements Runnable {
    
    private static final int WIDTH = GlobalVars.gameWidth, HEIGHT = GlobalVars.gameHeight;
    private Thread thread;
    private boolean running = false;
    
    private Player player;
    private ObjHandler enemyHdlr;
    private ObjHandler playerProjectileHdlr;
    private ObjHandler enemyProjectileHdlr;
    private HUD hud;
    private CrossingManager crMan;
    private Deployer deployer;
    private StatDisplayer displayer;
    private OptionsManager optMan;
    private FileManager slMan;
    
    private int enemyWeaponCd;
    private int enemySpawnCd;
    private int enemySpawnCounter;
    private boolean newWave;
    private boolean newGame;
    
    private GameState gameState;
    
    private Menu mainMenu;
    private Menu pauseMenu;
    private OptionsMenu optMenu;
    private EndScreen endScr;
    
    public GameController()
    {
        enemyHdlr = new ObjHandler();
        playerProjectileHdlr = new ObjHandler();
        enemyProjectileHdlr = new ObjHandler();
        player = new Player(GlobalVars.playerPosX, GlobalVars.playerPosY, playerProjectileHdlr);
        
        hud = new HUD();
        
        crMan = new CrossingManager();
        deployer = new Deployer();
        displayer = new StatDisplayer(enemyHdlr);
        optMan = new OptionsManager();
        slMan = new FileManager(this, hud);
        
        enemyWeaponCd =0;
        enemySpawnCd = 0;
        enemySpawnCounter = 0;
        newWave = false;
        newGame = true;
        
        gameState = GameState.MENU;
        mainMenu = new Menu(this, Menu.MenuType.MAIN);
        pauseMenu = new Menu(this, Menu.MenuType.PAUSE);
        optMenu = new OptionsMenu(this, optMan);
        endScr = new EndScreen(this, hud);
        
        this.addKeyListener(new KeyInput(player));
        this.addMouseListener(mainMenu);
        this.addMouseListener(pauseMenu);
        this.addMouseListener(optMenu);
        this.addMouseListener(endScr);
        
        new Window(GlobalVars.extWidth, HEIGHT, "Darwin Invaders", this);
    }
    
    /**
     * Metoda dostępowa zwracająca stan gry
     * @return Stan gry: GAME, MENU, PAUSE lub DEAD
     */
    public GameState getState()
    {
        return this.gameState;
    }
    
    /**
     * Metoda dostępowa ustawiajaca stan gry
     * @param state Nowy stan gry
     */
    public void setState(GameState state)
    {
        this.gameState = state;
    }
    
    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
        
        this.requestFocus();
    }
    
    public synchronized void stop()
    {
        try
        {
            thread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Głóna pętla programu.
     * Działąnie gry niezależne od częstotliwości odświeżania.
     */
    @Override
    public void run()
    {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        //int frames = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1)
            {
                tick();
                delta--;
            }
            if(running)
                render();
            //frames++;
            
            if(System.currentTimeMillis() - timer >1000)
            {
                timer +=1000;
                //System.out.println("FPS: " +frames);
                //frames = 0;
            }
        }
        stop();
    }
    
    /**
     * "Zeruje" pola obiektu na potrzeby ponownego rozpoczęcia gry od nowa w ramach danego uruchomienia programu.
     */
    public void reset()
    {
        if(newGame) newGame = false;
        else
        {
            player.reset();
            enemyHdlr.reset();
            playerProjectileHdlr.reset();
            enemyProjectileHdlr.reset();

            crMan.clear();
            crMan = new CrossingManager();
            deployer.reset();
            slMan.reset();

            enemyWeaponCd =0;
            enemySpawnCd = 0;
            enemySpawnCounter = 0;
            newWave = false;
            
            hud.updateHP(player);
            hud.setScore(0);
            endScr = new EndScreen(this, hud);
            
            displayer.Reset();
        }
    }
    
    public void saveGame()
    {
        /* Do zapisania:
        player HP
        last generation
        options
        */
        slMan.Save();
    }
    
    public void loadEnemies(LinkedList<LinkedList<Integer>> _lBehaviours, LinkedList<Integer> _lIds, LinkedList<Integer[]> _lParents)
    {
        crMan.clear();
        crMan.loadSpecimens(_lBehaviours, _lIds, _lParents);
    }
    
    public void loadPlayer(int _hp)
    {
        this.player.setHP(_hp);
    }
    
    public void loadGame()
    {
        this.reset();
        slMan.Load();
        deployer.nextWave();
        newWave = true;
        hud.updateHP(player);
    }
    
    private void tick()
    {
        if(gameState == GameState.MENU)
        {
            mainMenu.tick();
            //to trzeba zmienić
            //gameState = GameState.GAME;
        }
        else
        if(gameState == GameState.GAME)
        {
            Random r = new Random(); //do wyrzucenia?
            if(enemyHdlr.getSize() == 0)    //nie ma już wrogów?
            {
                if(!newWave)    //koniec dotychczasowej fali
                {
                    newWave = true;
                    enemySpawnCounter = 0;
                    crMan.crossSpecimens();
                    deployer.nextWave();
                    //System.out.println("Nowa fala");
                    //displayer.Update();   ???
                }
            }
            else //są wrogowie -> atakują
            {
                int s = enemyHdlr.getSize();
                enemyWeaponCd++;
                if(enemyWeaponCd >= s*GlobalVars.cooldownEnemyWeapon)
                {
                    enemyWeaponCd = 0;
                    ((BasicEnemy)enemyHdlr.get(r.nextInt(s))).shoot();
                }
            }

            if(enemySpawnCounter < GlobalVars.enemyCount)   //Wprowadzanie wrogów
            {
                //System.out.println("Liczba zespawnowanych wrogów: " +enemySpawnCounter);
                enemySpawnCd++;
                if(enemySpawnCd == GlobalVars.cooldownEnemySpawn)
                {
                    //System.out.println("Tworzenie wroga nr " +enemySpawnCounter);
                    enemySpawnCd = 0;
                    BasicEnemy newEnemy = new BasicEnemy(deployer.getNewId(), deployer.getX(enemySpawnCounter),GlobalVars.enemyPosY, enemyProjectileHdlr);
                    crMan.injectGenes(newEnemy, enemySpawnCounter);
                    //TU : przekaż geny do zapisu do pliku
                    slMan.AddData(newEnemy);
                    enemyHdlr.add(newEnemy);  
                    enemySpawnCounter++;
                    //Aktualizacja wartości fali
                    //Tu zakładamy, że wartość ta ma być równa maksymalnej liczbie wrogów obecnych w grze *10
                    int v = enemyHdlr.getSize()*10;
                    if(v > deployer.getValue()) 
                        deployer.setValue(v);
                    //zaktualizuj wyświetlanie zawartości populacji
                    displayer.Update();
                }
            }
            else
                if(newWave) //osiągnięto limit -> to już nie nowa fala
                {
                    newWave = false;
                    crMan.clear();
                }

            //główna część...
            player.tick();
            enemyHdlr.tick();
            enemyProjectileHdlr.tick();
            playerProjectileHdlr.tick();
            displayer.tick();

            //kolizje!
            //kolizje z graczem
            boolean colDetected = false;
            //kolizja wroga z graczem lub dolną krawędzią obszaru gry
            int n = enemyHdlr.getSize();
            int i = 0;
            while((i<n) && !colDetected)
            {
                GameObject tmpObj = enemyHdlr.get(i);
                if(player.getHitbox().intersects(tmpObj.getHitbox()))
                {
                    player.kill();
                    tmpObj.kill();
                    hud.updateHP(player);
                    hud.incScore(((BasicEnemy)tmpObj).getValue());
                    colDetected = true;
                }
                else
                    if(tmpObj.y > GlobalVars.gameHeight-44)
                    {
                        tmpObj.kill();
                        hud.decScore(((BasicEnemy)tmpObj).getValue());
                    }
                i++;
            }
            
            //kolizja pocisku z graczem
            n =  enemyProjectileHdlr.getSize();
            i = 0;
            while((i<n) && !colDetected)
            {
                GameObject tmpObj = enemyProjectileHdlr.get(i);
                if(player.getHitbox().intersects(tmpObj.getHitbox()))
                {
                    player.kill();
                    tmpObj.kill();
                    hud.updateHP(player);
                    colDetected = true;
                }
                i++;
            }

            //kolizja wróg-pocisk
            n = playerProjectileHdlr.getSize();
            for(int j=0; j<n; j++)
            {
                GameObject tmpBullet = playerProjectileHdlr.get(j);
                colDetected = false;
                int m = enemyHdlr.getSize();
                i = 0;
                while((i<m) && !colDetected)
                {
                    GameObject tmpObj = enemyHdlr.get(i);
                    if(tmpBullet.getHitbox().intersects(tmpObj.getHitbox()))
                    {
                        tmpBullet.kill();
                        tmpObj.kill();
                        hud.incScore(((BasicEnemy)tmpObj).getValue());
                        colDetected = true;
                    }
                    i++;
                }
            }

            //czyszczenie list z "martwych" obiektów
            if(enemyHdlr.getSize() >0) 
            {
                crMan.collectSpecimens(enemyHdlr);
                displayer.SaveTheDead();
            }
            enemyHdlr.clearDead();
            enemyProjectileHdlr.clearDead();
            playerProjectileHdlr.clearDead();        
            //tu: obsługa śmierci "gracza"
            if(!player.isAlive()) gameState = GameState.DEAD;
            //pauza... na około...
            if(player.pauseRequested())
            {
                gameState = GameState.PAUSE;
                //player.requestPause();
            }
        }
        else
        if(gameState == GameState.PAUSE)
        {
            pauseMenu.tick();
            if(player.pauseRequested()) gameState = GameState.GAME;
        }
        else
        if(gameState == GameState.OPTIONS)
        {
            optMenu.tick();
        }
        else
        if(gameState == GameState.DEAD)
        {
            //tu coś się zadzieje, a potem...
            //System.out.println("Player dead");
            //gameState = GameState.MENU;
            endScr.prepScore();
        }
        else
        if(gameState == GameState.END)
        {
            displayer.DumpToLog();
            //sapis stanu rozgrywki?
            //zapis opcji
            System.exit(0);
        }
    }
    
    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics graphics = bs.getDrawGraphics();
        //tu napisz zawartość do wyświetlania!
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.setColor(Color.black);
        graphics.fillRect(WIDTH, 0, GlobalVars.extWidth, HEIGHT);
            
        if(gameState != null)
        switch (gameState) {
            case MENU:
                mainMenu.render(graphics);
                break;
            case GAME:
                player.render(graphics);
                enemyHdlr.render(graphics);
                enemyProjectileHdlr.render(graphics);
                playerProjectileHdlr.render(graphics);
                hud.render(graphics);
                displayer.render(graphics);
                break;
            case PAUSE:
                pauseMenu.render(graphics);
                displayer.render(graphics);
                break;
            case OPTIONS:
                optMenu.render(graphics);
                break;
            case DEAD:
                endScr.render(graphics);
                break;
            default:
                break;
        }
        
        graphics.dispose();
        bs.show();
    }
    
    public static void main(String args[])
    {
        new GameController();
    }
}
