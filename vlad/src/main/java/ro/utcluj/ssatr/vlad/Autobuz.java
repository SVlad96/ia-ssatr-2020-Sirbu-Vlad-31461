/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.utcluj.ssatr.vlad;

/**
 *
 * @author Vlad
 */
public class Autobuz {
    
    private int number;
    private int stopTime;

    public Autobuz( int number) {
        this.number = number;
    }

    public Autobuz(int number, int stopTime) {
        this.number = number;
        this.stopTime = stopTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

}
