package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;

    //Se cambia el int por un AtomicInteger para que este tenga un orden de acuerdo a como
    //va a ser modificado. No se deja usar mientras alguien más lo esté usando
    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private boolean paused;
    private final Random r = new Random(System.currentTimeMillis());


    public Immortal(String name, List<Immortal> immortalsPopulation, AtomicInteger health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (true) {
            //if(health.get() <= 0){
              //  break;
            //}
            Immortal im;
            synchronized(immortalsPopulation){
                try{
                    if(paused){
                        immortalsPopulation.wait();
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            


            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());


            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) {
        synchronized(immortalsPopulation){
        synchronized(i2.getHealth()){
            if (i2.getHealth().get() > 0) {
                i2.changeHealth(i2.getHealth().get() - defaultDamageValue);
                this.health.getAndAdd(defaultDamageValue);
                updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
            } else {
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            }
        }}

    }

    public void pause(boolean bool){
        this.paused = bool;
    }

    public void changeHealth(int v) {
        health.getAndSet(v);}

    public AtomicInteger getHealth() {
        return health;
    }
    
    public void pause(){
        this.paused = true;
    }
    
    public void resumed(){
        this.paused = false;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
