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
    private ArrayList<boolean[]> cromossomos;
    private Object controle;
    private long geracao = 0;
    
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
        private ArrayList<boolean[]> cromossomos;
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
        geracoes = builder.geracoes;
    }
    
    public Gene(ArrayList<Object> lista) {
        this.lista = lista;
    }
    
    public boolean[] geraCromossomo() {
        Random r = new Random();
        ArrayList<Object> l = (ArrayList<Object>) this.lista;
        boolean[] b = new boolean[l.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = r.nextBoolean();
        }
        return b;
    }
    
    public ArrayList<DadosCromossomo> geraCromossomos() {
        ArrayList<DadosCromossomo> p = new ArrayList<>();
        
        for (int i = p.size(); i < this.populacao; i++) {
            p.add(new DadosCromossomo(geraCromossomo()));
        }
        
        return p;
    }
    
    public void geraCromossomos(ArrayList<DadosCromossomo> cromossomo) {
        for (int i = cromossomo.size(); i < this.populacao; i++) {
            cromossomo.add(new DadosCromossomo(geraCromossomo()));
        }
    }
    
    
    public ArrayList<DadosCromossomo> executa(ArrayList<DadosCromossomo> cromossomos) {
        if (geracao < geracoes) {
            //System.out.println("Geração: " + geracao);
            //System.out.println("Antes de Avaliar: ");
            //System.out.println(getCromossomosBits(cromossomos));
            validaCromossomos(cromossomos);
            //System.out.println("Depois de Avaliar: ");
            //System.out.println(getCromossomosBits(cromossomosValidos));
            //setObjectValue(getCromossomoBits(cromossomosValidos.get(0).cromossomo) + ": " + cromossomosValidos.get(0).valorObjetivo);
            geraCromossomos(cromossomos);
            //System.out.println("Após gerar novos cromossomos: ");
            //System.out.println(getCromossomosBits(cromossomosValidos));
            geracao += 1;
            executa(cromossomos);
        }
        
        return cromossomos;
    }
    
    private void validaCromossomos(ArrayList<DadosCromossomo> cromossomos) {
        ArrayList<DadosCromossomo> excluir = new ArrayList<>();
        
        cromossomos.forEach((dado) -> {
            double valor = 0;
            double restricao[] = new double[restricoesCampo.length];
            //System.out.println(getCromossomoBits(dado));
            for (int i = 0; i < dado.getCromossomo().length; i++) {
                if (!dado.isAvaliado()) {
                    if (dado.getCromossomo()[i]) {
                        valor += getCromossomoIndexValue(i);
                        for (int j = 0; j < restricao.length; j++) {
                            restricao[j] += getCromossomoIndexRestriction(i, j);
                        }
                    }
                }
            }
            
            
            if (existeRestricao(restricao)) {
                excluir.add(dado);
            } else {
                dado.setValorObjetivo(valor);
                dado.setValorRestricoes(restricao);
                dado.setAvaliado(true);
            }
            
        });   
        
        cromossomos.removeAll(excluir);
    }
    
    private boolean existeRestricao(double[] restricoes) {
        for (int i = 0; i < restricoes.length; i++) {
            switch (restricoesTipo[i]) {
                case "<":
                    if (!(restricoes[i] < restricoesValor[i])) {
                        return true;
                    }   break;
                case "<=":
                    if (!(restricoes[i] <= restricoesValor[i])) {
                        return true;
                    }   break;
                case ">":
                    if (!(restricoes[i] > restricoesValor[i])) {
                        return true;
                    }   break;
                case ">=":
                    if (!(restricoes[i] >= restricoesValor[i])) {
                        return true;
                    }   break;
                case "!=":
                    if (!(restricoes[i] != restricoesValor[i])) {
                        return true;
                    }   break;
                case "==":
                    if (!(restricoes[i] == restricoesValor[i])) {
                        return true;
                    }   break;
                default:
                    break;
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
            //System.out.println(index + ": " + valor);
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
    
    public String getCromossomosBits(ArrayList<DadosCromossomo> cromossomos) {
        String valor = "";
        
        for (DadosCromossomo c : cromossomos) {
            valor += getCromossomoBits(c.getCromossomo()) + "\n";
        }
        
        return valor;
    }
    
    public String getCromossomoBits(DadosCromossomo cromossomo) {
        return getCromossomoBits(cromossomo.getCromossomo());
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
                ArrayList<DadosCromossomo> d = g.geraCromossomos();
                
                if (!(controle == null)) {
                    g.setObjectValue("" + d.size());
                }
                
                //g.executa(d);
                System.out.println(getCromossomosBits(g.executa(d)));
                System.out.println("Geração: " + geracao);
            }
        }.start();
    }
    
    public class DadosCromossomo implements Comparable<DadosCromossomo> {
        private boolean[] cromossomo;
        private double valorObjetivo;
        private double[] valorRestricoes;
        private boolean avaliado;
        
        public DadosCromossomo(boolean[] cromossomo, double valorObjetivo, double[] valorRestricoes) {
            this(cromossomo);
            this.valorObjetivo = valorObjetivo;
            this.valorRestricoes = valorRestricoes;
            avaliado = true;
        }
        
        public DadosCromossomo(boolean[] cromossomo) {
            this.cromossomo = cromossomo;
        }

        public boolean[] getCromossomo() {
            return cromossomo;
        }

        public void setCromossomo(boolean[] cromossomo) {
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

        public boolean isAvaliado() {
            return avaliado;
        }

        public void setAvaliado(boolean avaliado) {
            this.avaliado = avaliado;
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
