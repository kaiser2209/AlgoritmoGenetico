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
    private int limitePeso;
    private int limiteVolume;
    private float taxaDeCruzamento;
    private float taxaDeMutacao;
    private long geracoes;
    private ArrayList<DadosCromossomo> cromossomos = new ArrayList<>();
    private long geracao;
    
    public Genetico(Builder builder) {
        populacao = builder.populacao;
        taxaDeCruzamento = builder.taxaDeCruzamento;
        taxaDeMutacao = builder.taxaDeMutacao;
        geracoes = builder.geracoes;
        lista = builder.lista;
        limitePeso = builder.limitePeso;
        limiteVolume = builder.limiteVolume;
    }
    
    public void geraCromossomos() {
        for (int i = cromossomos.size(); i < populacao; i++) {
            cromossomos.add(new DadosCromossomo(geraCromossomo()));
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
    
    public String getRestricoesCromossomos(DadosCromossomo cromossomo) {
        return "Peso: " + String.format("%4d", cromossomo.getPeso()) + 
               " Volume: " + String.format("%4d", cromossomo.getVolume()) +
               " Valor: " + String.format("%4d", cromossomo.getValor());
    }
    
    public String getCromossomosDados(ArrayList<DadosCromossomo> cromossomos) {
        String valor = "";
        
        for (DadosCromossomo b : cromossomos) {
            valor += getCromossomoBits(b.getCromossomo()) + " - " + 
                    getRestricoesCromossomos(b) + "\n";
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
    
    public int[] avaliaCromossomo(boolean[] cromossomo) {
        int[] valor = new int[3];
        
        for (int i = 0; i < cromossomo.length; i++) {
            if (cromossomo[i]) {
                valor[0] += lista.get(i).getValor();
                valor[1] += lista.get(i).getPeso();
                valor[2] += lista.get(i).getVolume();
            }
        }
        
        return valor;
    }
    
    public void avaliaCromossomos() {
        ArrayList<DadosCromossomo> listaExclusao = new ArrayList<>();
        for (DadosCromossomo dados : cromossomos) {
            int valor = 0, peso = 0, volume = 0;
            int valores[] = avaliaCromossomo(dados.getCromossomo());
            valor = valores[0];
            peso = valores[1];
            volume = valores[2];
            if (peso > limitePeso || volume > limiteVolume) {
                listaExclusao.add(dados);
            } else {
                dados.valor = valor;
                dados.peso = peso;
                dados.volume = volume;
            }
        }
        System.out.println("Antes da Avaliação: ");
        System.out.println(getCromossomosDados(cromossomos));
        cromossomos.removeAll(listaExclusao);
        System.out.println("Depois da Avaliação: ");
        System.out.println(getCromossomosDados(cromossomos));
    }
    
    public void realizaCruzamento() {
        
    }
    
    public void executa() {
        new Thread() {
            Genetico g = Genetico.this;
            
            @Override
            public void run() {
                do {
                    geraCromossomos();
                    avaliaCromossomos();
                    realizaCruzamento();
                    geracao++;
                    //System.out.println(g.getCromossomosBits(cromossomos));
                } while (geracao < geracoes);
                System.out.println(geracao);
            }
        }.start();
    }
    
    public static class Builder {
        private ArrayList<DadosGen> lista;
        private int populacao = 6;
        private int limitePeso = 6000;
        private int limiteVolume = 350;
        private float taxaDeCruzamento = 0.9f;
        private float taxaDeMutacao = 0.05f;
        private long geracoes = 10000l;
        
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
        
        public Builder limiteDePeso(int peso) {
            this.limitePeso = peso;
            return this;
        }
        
        public Builder limiteDeVolume(int volume) {
            this.limiteVolume = volume;
            return this;
        }
    }
    
    public class DadosCromossomo {
        private boolean[] cromossomo;
        private int valor;
        private int peso;
        private int volume;
        private boolean avaliado;
        
        public DadosCromossomo(boolean[] cromossomo) {
            this.cromossomo = cromossomo;
        }
        
        public DadosCromossomo(boolean[] cromossomo, int valor, int peso, int volume) {
            this(cromossomo);
            this.valor = valor;
            this.peso = peso;
            this.volume = volume;
        }

        public boolean[] getCromossomo() {
            return cromossomo;
        }

        public void setCromossomo(boolean[] cromossomo) {
            this.cromossomo = cromossomo;
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

        public boolean isAvaliado() {
            return avaliado;
        }

        public void setAvaliado(boolean avaliado) {
            this.avaliado = avaliado;
        }
    }
}
