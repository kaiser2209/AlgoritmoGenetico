/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Random;
import model.DadosGen;

/**
 *
 * @author usuario.local
 */
public class Genetico {
    private ArrayList<DadosGen> lista;
    private int populacao;
    private float taxaDeCruzamento;
    private float taxaDeMutacao;
    private long geracoes;
    private final ArrayList<boolean[]> cromossomos;
    private long geracao;
    
    public Genetico(Builder builder) {
        populacao = builder.populacao;
        taxaDeCruzamento = builder.taxaDeCruzamento;
        taxaDeMutacao = builder.taxaDeMutacao;
        geracoes = builder.geracoes;
        cromossomos = builder.cromossomos;
        lista = builder.lista;
    }
    
    public void geraCromossomos() {
        for (int i = cromossomos.size(); i < populacao; i++) {
            cromossomos.add(geraCromossomo());
        }
    }
    
    public boolean[] geraCromossomo() {
        boolean[] b = new boolean[lista.size()];
        Random r = new Random();
        for (int i = 0; i < b.length; i++) {
            b[i] = r.nextBoolean();
        }
        
        return b;
    }
    
    public String getCromossomosBits(ArrayList<boolean[]> cromossomos) {
        String valor = "";
        
        for (boolean[] b : cromossomos) {
            valor += getCromossomoBits(b) + "\n";
        }
        
        return valor;
    }
    
    public String getCromossomoBits(boolean[] cromossomo) {
        String valor = "";
        for (boolean b : cromossomo) {
            if (b) {
                valor += "1";
            } else {
                valor += "0";
            }
        }
        
        return valor;
    }
    
    public void executa() {
        new Thread() {
            Genetico g = Genetico.this;
            
            @Override
            public void run() {
                do {
                    geraCromossomos();
                    geracao++;
                    System.out.println(g.getCromossomosBits(cromossomos));
                } while (geracao < geracoes);
            }
        }.start();
    }
    
    public static class Builder {
        private ArrayList<DadosGen> lista;
        private int populacao = 6;
        private float taxaDeCruzamento = 0.9f;
        private float taxaDeMutacao = 0.05f;
        private long geracoes = 10000l;
        private ArrayList<boolean[]> cromossomos = new ArrayList<>();
        
        public Genetico build() {
            return new Genetico(this);
        }
        
        public Builder(ArrayList<DadosGen> lista) {
            this.lista = lista;
        }
        
        public Builder populacao(int populacao) {
            this.populacao = populacao;
            return this;
        }
        
        public Builder taxaDeCruzamento(float taxa) {
            taxaDeCruzamento = taxa;
            return this;
        }
        
        public Builder taxaDeMutacao(float taxa) {
            taxaDeMutacao = taxa;
            return this;
        }
        
        public Builder geracoesDesejadas(long geracoes) {
            this.geracoes = geracoes;
            return this;
        }
        
        public Builder populacaoInicial(ArrayList<boolean[]> cromossomos) {
            this.cromossomos = cromossomos;
            return this;
        }
    }
}
