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
    private float taxaDeCruzamento;
    private float taxaDeMutacao;
    private float intervaloDeGeracao;
    private long geracoes;
    private String[] restricoesCampo;
    private String[] restricoesTipo;
    private ArrayList<byte[]> cromossomos;
    
    public static class Builder {
        private int populacao = 6;
        private Object lista;
        private String objetivoCampo;
        private boolean objetivoMax = true;
        private float taxaDeCruzamento = 0.9f;
        private float taxaDeMutacao = 0.05f;
        private float intervaloDeGeracao = 0.5f;
        private long geracoes = 200000l;
        private String[] restricoesCampo;
        private String[] restricoesTipo;
        private ArrayList<byte[]> cromossomos;
        
        public Builder(ArrayList lista) {
            this.lista = lista;
        }
        
        public Gene build() {
            return new Gene(this);
        }
        
        public Builder tamanhoPopulacao(int populacao) {
            this.populacao = populacao;
            return this;
        }
        
        public Builder objetivo(String campo, boolean max) {
            objetivoCampo = campo;
            objetivoMax = max;
            return this;
        }
        
        public Builder taxaDeCruzamento(float taxaDeCruzamento) {
            this.taxaDeCruzamento = taxaDeCruzamento;
            return this;
        }
        
        public Builder taxaDeMutacao(float taxaDeMutacao) {
            this.taxaDeMutacao = taxaDeMutacao;
            return this;
        }
        
        public Builder intervaloDeGeracao(float intervaloDeGeracao) {
            this.intervaloDeGeracao = intervaloDeGeracao;
            return this;
        }
        
        public Builder geracoesDesejadas(long geracoes) {
            this.geracoes = geracoes;
            return this;
        }
        
        public Builder restricoes(String[] campos, String[] tipos) {
            restricoesCampo = campos;
            restricoesTipo = tipos;
            return this;
        }
        
        public Object getLista() {
            return lista;
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
        ArrayList<Object> l = (ArrayList<Object>) this.lista;
        byte[] b = new byte[l.size()];
        for (int i = 0; i < b.length; i++) {
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
    
    public void start() {
        new Thread() {
            Gene g = Gene.this;
            
            @Override
            public void run() {
                g.cromossomos = g.geraCromossomos();
                
                System.out.println("Tamanho: " + g.cromossomos.size());
            }
        }.start();
    }
}
