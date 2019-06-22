/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author usuario.local
 */
public class Gene {
    private int populacao;
    private Object lista;
    private String objetivoCampo;
    private boolean objetivoMax;
    
    public static class Builder {
        private int populacao;
        private Object lista;
        private String objetivoCampo;
        private boolean objetivoMax = true;
        
        public Builder(int populacao, Object lista) {
            this.populacao = populacao;
            this.lista = lista;
        }
        
        public Gene build() {
            return new Gene(this);
        }
        
        public Builder objetivo(String campo, boolean max) {
            objetivoCampo = campo;
            objetivoMax = max;
            return this;
        }
    }
    
    private Gene(Builder builder) {
        populacao = builder.populacao;
        lista = builder.lista;
    }
    
    public Gene(ArrayList<Object> lista) {
        this.lista = lista;
    }
    
    public byte[] geraCromossomo() {
        Random r = new Random();
        ArrayList<Object> lista = (ArrayList<Object>) this.lista;
        byte[] b = new byte[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            b[i] = (byte) r.nextInt(2);
        }
        return b;
    }
    
    public ArrayList<byte[]> geraCromossomos() {
        ArrayList<byte[]> p = new ArrayList<>();
        
        for (int i = 0; i < this.populacao; i++) {
            p.add(geraCromossomo());
        }
        
        return p;
    }
}
