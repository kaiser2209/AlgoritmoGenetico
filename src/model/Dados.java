/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author usuario.local
 */
public class Dados {
    private double valor;
    private double peso;
    private double volume;

    public Dados(int valor, int peso, int volume) {
        this.valor = valor;
        this.peso = peso;
        this.volume = volume;
    }
    
    public Dados() {
        
    }
    
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
    
}
