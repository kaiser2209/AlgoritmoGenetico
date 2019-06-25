/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private double[] restricoesValor;
    private ArrayList<byte[]> cromossomos;
    private Object controle;
    
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
        private double[] restricoesValor;
        private ArrayList<byte[]> cromossomos;
        private Object controle;
        
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
        
        public Builder restricoes(String[] campos, String[] tipos, double[] valores) {
            restricoesCampo = campos;
            restricoesTipo = tipos;
            restricoesValor = valores;
            return this;
        }
        
        public Builder statusControle(Object controle) {
            this.controle = controle;
            return this;
        }
        
        public Object getLista() {
            return lista;
        }
    }
    
    private Gene(Builder builder) {
        populacao = builder.populacao;
        lista = builder.lista;
        controle = builder.controle;
        objetivoCampo = builder.objetivoCampo;
        restricoesCampo = builder.restricoesCampo;
        restricoesTipo = builder.restricoesTipo;
        restricoesValor = builder.restricoesValor;
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
    
    public void executa(ArrayList<byte[]> cromossomos) {
        ArrayList<DadosCromossomo> cromossomosValidos = validaCromossomos(cromossomos);
        setObjectValue(getCromossomoBits(cromossomosValidos.get(0).cromossomo) + ": " + cromossomosValidos.get(0).valorObjetivo);
        System.out.println(cromossomosValidos.size());
    }
    
    private ArrayList<DadosCromossomo> validaCromossomos(ArrayList<byte[]> cromossomos) {
        ArrayList<DadosCromossomo> dados = new ArrayList<>();
        
        cromossomos.forEach((b) -> {
            double valor = 0;
            double restricao[] = new double[restricoesCampo.length];
            System.out.println(getCromossomoBits(b));
            for (int i = 0; i < b.length; i++) {
                if (b[i] == 1) {
                    valor += getCromossomoIndexValue(i);
                    for (int j = 0; j < restricao.length; j++) {
                        restricao[j] += getCromossomoIndexRestriction(i, j);
                    }
                }
            }
            
            if (!existeRestricao(restricao)) {
                DadosCromossomo dado = new DadosCromossomo(b, valor, restricao);
                dados.add(dado);
            }
            
        });   
        
        return dados;
    }
    
    private boolean existeRestricao(double[] restricoes) {
        for (int i = 0; i < restricoes.length; i++) {
            if (restricoesTipo[i].equals("<")) {
                if (!(restricoes[i] < restricoesValor[i])) {
                    return true;
                }
            } else if (restricoesTipo[i].equals("<=")) {
                if (!(restricoes[i] <= restricoesValor[i])) {
                    return true;
                }
            } else if (restricoesTipo[i].equals(">")) {
                if (!(restricoes[i] > restricoesValor[i])) {
                    return true;
                }
            } else if (restricoesTipo[i].equals(">=")) {
                if (!(restricoes[i] >= restricoesValor[i])) {
                    return true;
                }
            } else if (restricoesTipo[i].equals("!=")) {
                if (!(restricoes[i] != restricoesValor[i])) {
                    return true;
                }
            } else if (restricoesTipo[i].equals("==")) {
                if (!(restricoes[i] == restricoesValor[i])) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private double getCromossomoIndexValue(int index) {
        double valor = 0;
        PropertyDescriptor pd;
        ArrayList<Object> listaDados = (ArrayList) this.lista;
        Object dados = listaDados.get(index);
        
        try {
            pd = new PropertyDescriptor(objetivoCampo, dados.getClass());
            valor = (double) pd.getReadMethod().invoke(dados);
            System.out.println(index + ": " + valor);
            //System.out.println(valor);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Gene.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return valor;
    }
    
    private double getCromossomoIndexRestriction(int index, int indexRestricao) {
        double valor = 0;
        PropertyDescriptor pd;
        ArrayList<Object> listaDados = (ArrayList) this.lista;
        Object dados = listaDados.get(index);
        
        try {
            pd = new PropertyDescriptor(restricoesCampo[indexRestricao], dados.getClass());
            valor = (double) pd.getReadMethod().invoke(dados);
            //System.out.println(valor);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Gene.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return valor;
    }
    
    public String getCromossomoBits(byte[] cromossomo) {
        String valor = "";
        
        for (byte b : cromossomo) {
            valor += String.valueOf(b);
        }
        
        return valor;
    }
    
    public void setObjectValue(Object value) {
        PropertyDescriptor pd;
        
        try {
            pd = new PropertyDescriptor("text", controle.getClass());
            pd.getWriteMethod().invoke(controle, value);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Gene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start() {
        new Thread() {
            Gene g = Gene.this;
            
            @Override
            public void run() {
                g.cromossomos = g.geraCromossomos();
                
                if (!(controle == null)) {
                    g.setObjectValue("" + g.cromossomos.size());
                }
                
                g.executa(g.cromossomos);
            }
        }.start();
    }
    
    public class DadosCromossomo implements Comparable<DadosCromossomo> {
        private byte[] cromossomo;
        private double valorObjetivo;
        private double[] valorRestricoes;
        
        public DadosCromossomo(byte[] cromossomo, double valorObjetivo, double[] valorRestricoes) {
            this.cromossomo = cromossomo;
            this.valorObjetivo = valorObjetivo;
            this.valorRestricoes = valorRestricoes;
        }

        public byte[] getCromossomo() {
            return cromossomo;
        }

        public void setCromossomo(byte[] cromossomo) {
            this.cromossomo = cromossomo;
        }

        public double getValorObjetivo() {
            return valorObjetivo;
        }

        public void setValorObjetivo(double valorObjetivo) {
            this.valorObjetivo = valorObjetivo;
        }

        public double[] getValorRestricoes() {
            return valorRestricoes;
        }

        public void setValorRestricoes(double[] valorRestricoes) {
            this.valorRestricoes = valorRestricoes;
        }
        
        @Override
        public int compareTo(DadosCromossomo o) {
            if (this.valorObjetivo > o.valorObjetivo) {
                return -1;
            }
            if (this.valorObjetivo < o.valorObjetivo) {
                return 1;
            }
            return 0;
        }
        
    }
}
