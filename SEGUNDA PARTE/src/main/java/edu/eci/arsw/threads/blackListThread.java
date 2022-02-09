/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.threads;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import edu.eci.arsw.blacklistvalidator.*;

/**
 *
 * @author diego
 */
public class blackListThread extends Thread{
    private int inferior;
    private int superior;
    private String host;
    private HostBlacklistsDataSourceFacade skds;
    private static int countBlackList;
    private static Integer contBlack = 0;
    private int listCount;
    private LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    
    /**
     * Constructor del hilo de acuerdo a que se le envia la direccion ip a revisar, el intervalo de
     * revision y la lista a revisar
     * @param a
     * @param b
     * @param ip
     * @param blackList 
     */
    public blackListThread(int a, int b, String ip, HostBlacklistsDataSourceFacade blackList){
        this.inferior = a;
        this.superior = b;
        this.host = ip;
        this.skds = blackList;
    }
    
    /**
     * Funcion generada para revisar en un segmento de la lista de servidores si 
     * la direccion ip esta dentro de esas listas. En caso que esto sea cierto
     * se le suma al contador de listas negras donde aparece la ip y la posicion
     * de la lista se guarda en una LinkedList
     */
    public void run(){
        synchronized(blackListOcurrences){
            synchronized(contBlack){
                for(int num = this.inferior;num<=this.superior;num++){
                    this.listCount ++;
                    if(this.skds.isInBlackListServer(num,this.host)){
                        this.blackListOcurrences.add(num);
                        this.countBlackList ++;
                        contBlack =+ 1;
                    }
                }
                    try{
                        if(contBlack == HostBlackListsValidator.getBlackListAlarm()){
                            blackListOcurrences.wait();
                        }
                    }
                    catch(InterruptedException e){
                       e.printStackTrace();
                    }   
            }
        }
    }
    
    /**
     * Funcion generada para retornar la cantidad de veces que el host ha sido 
     * encontrado en listas negras
     * @return 
     */
    public int malwareFounded(){
        return this.countBlackList;
    }
    
    /**
     * Retorna la lista de posiciones de las listas negras donde apareció la direccion
     * @return -> linkedList<Integer>
     */
    public LinkedList<Integer> getBlackListOcurrence(){
        return this.blackListOcurrences;
    }
    
    /**
     * Retorna la cantidad de listas que fueron revisadas
     * @return -> cantidad de listas (int)
     */
    public int getListCount(){
        return this.listCount;
    }
    
}
