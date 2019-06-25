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
public class DadosGen {
    private int valor;
    private int peso;
    private int volume;

    public DadosGen(int valor, int peso, int volume) {
        this.valor = valor;
        this.peso = peso;
        this.volume = volume;
    }
    
    public DadosGen() {
        
    }
    
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    
}
