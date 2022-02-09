/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread{
    private int inferior;
    private int superior;
    
    /**
     * Creacion de las variables donde se van a guardar los limites 
     * @param a
     * @param b 
     */
    public CountThread(int a, int b){
        this.inferior = a;
        this.superior = b;
    }
    
    /**
     * Funcion generada para revisar que si en caso que nuestra variable num este dentro del rango
     * esta se imprima
     */
    public void run(){
        for(int num = this.inferior; num<=this.superior;num++){
            System.out.println(num);
        }
    }
}
