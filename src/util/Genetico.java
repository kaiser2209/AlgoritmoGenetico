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
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import model.DadosGen;
import org.controlsfx.control.StatusBar;

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
    private Object controle;
    private StringProperty mensagem;
    private Service<Void> servicoMensagem;
    
    public Genetico(Builder builder) {
        populacao = builder.populacao;
        taxaDeCruzamento = builder.taxaDeCruzamento;
        taxaDeMutacao = builder.taxaDeMutacao;
        geracoes = builder.geracoes;
        lista = builder.lista;
        limitePeso = builder.limitePeso;
        limiteVolume = builder.limiteVolume;
        controle = builder.controle;
        mensagem = builder.mensagem;
        servicoMensagem = builder.servicoMensagem;
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
            valor += getCromossomosDados(b) + "\n";
        }
        
        return valor;
    }
    
    public String getCromossomosDados(DadosCromossomo cromossomo) {
        String valor = "";
        
        valor += getCromossomoBits(cromossomo.getCromossomo()) + " - " +
                getRestricoesCromossomos(cromossomo);
        
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
    
    public void calculaDadosCromossomo(DadosCromossomo cromossomo) {
        if (!cromossomo.isCalculado()) {
            cromossomo.limpaValores();
            for (int i = 0; i < cromossomo.getCromossomo().length; i++) {
                if (cromossomo.getCromossomo()[i]) {
                    cromossomo.valor += lista.get(i).getValor();
                    cromossomo.peso += lista.get(i).getPeso();
                    cromossomo.volume += lista.get(i).getVolume();
                }
            }
            cromossomo.setCalculado(true);
        }
    }
    
    public void avaliaCromossomo(DadosCromossomo cromossomo) {
        while (cromossomo.getPeso() > limitePeso || cromossomo.getVolume() > limiteVolume) {
            cromossomo.setCalculado(false);
            reparaCromossomo(cromossomo);
        }
    }
    
    public void reparaCromossomo(DadosCromossomo cromossomo) {
        int bitsAtivos = 0;
        int posicao;
        
        for (boolean b : cromossomo.getCromossomo()) {
            if (b) {
                bitsAtivos++;
            }
        }
        
        Random r = new Random();
        posicao = r.nextInt(bitsAtivos);
        
        for (int i = 0; i < cromossomo.getCromossomo().length; i++) {
            if (cromossomo.getCromossomo()[i]) {
                if (posicao == 0) {
                    cromossomo.getCromossomo()[i] = false;
                }
                posicao--;
            }
        }
        calculaDadosCromossomo(cromossomo);
    }
    
    public void avaliaCromossomos(ArrayList<DadosCromossomo> cromossomos) {
        for (DadosCromossomo dados : cromossomos) {
            calculaDadosCromossomo(dados);
            avaliaCromossomo(dados);
        }
        Collections.sort(cromossomos);
    }
    
    public ArrayList<DadosCromossomo> cruzaCromossomos(DadosCromossomo pai1, DadosCromossomo pai2) {
        ArrayList<DadosCromossomo> filhos = new ArrayList<>();
        float aleatorio;
        
        Random r = new Random();
        aleatorio = r.nextFloat();
        
        boolean[][] filho = new boolean[2][lista.size()];
        if (aleatorio <= taxaDeCruzamento) {
            int pos = r.nextInt(lista.size() - 1) + 1 ;
            
            for (int i = 0; i < lista.size(); i++) {
                if (pos > i) {
                    filho[0][i] = pai1.getCromossomo()[i];
                    filho[1][i] = pai2.getCromossomo()[i];
                } else {
                    filho[0][i] = pai2.getCromossomo()[i];
                    filho[1][i] = pai1.getCromossomo()[i];
                }
            }
        } else {
            for (int i = 0; i < lista.size(); i++) {
                filho[0][i] = pai1.getCromossomo()[i];
                filho[1][i] = pai2.getCromossomo()[i];
            }
        }
        filhos.add(new DadosCromossomo(filho[0]));
        filhos.add(new DadosCromossomo(filho[1]));
        
        return filhos;
    }
    
    public void realizaCruzamento() {
        int[] valoresAcumulados = calculaAcumulado(cromossomos);
        ArrayList<DadosCromossomo> melhoresAnterior = new ArrayList<>();
        ArrayList<DadosCromossomo> paisSelecionados = new ArrayList<>();
        ArrayList<DadosCromossomo> novaPopulacao = new ArrayList<>();
        while (paisSelecionados.size() < (populacao - 2)) {
            paisSelecionados.add(selecionaCromossomo(valoresAcumulados, cromossomos));
        }
        for (int i = 0; i < paisSelecionados.size(); i += 2 ) {
            novaPopulacao.addAll(cruzaCromossomos(paisSelecionados.get(i), paisSelecionados.get(i + 1)));
        }
        
        melhoresAnterior.add(cromossomos.get(0));
        melhoresAnterior.add(cromossomos.get(1));
        //System.out.println("Pais Selecionados: ");
        //System.out.println(getCromossomosDados(paisSelecionados));
        //System.out.println("Geração: " + geracao);
        //System.out.println("Antes de substituir: ");
        //System.out.println(getCromossomosDados(cromossomos));
        realizaMutacao(novaPopulacao);
        //System.out.println("Maiores");
        //System.out.println(getCromossomosDados(cromossomos.get(0)));
        //System.out.println(getCromossomosDados(cromossomos.get(1)));
        //System.out.println("=================================================");
        
        //System.out.println("Nova População: ");
        //System.out.println(getCromossomosDados(novaPopulacao));
        novaPopulacao.addAll(melhoresAnterior);
        Collections.sort(novaPopulacao);
        cromossomos = novaPopulacao;
        //System.out.println("Depois de substituir: ");
        //System.out.println(getCromossomosDados(cromossomos));
        
        //System.out.println("Nova População Ordenada: ");
        //System.out.println(getCromossomosDados(novaPopulacao));

    }
    
    public void realizaMutacao(ArrayList<DadosCromossomo> cromossomos) {
        Random r = new Random();
        float resultado;
        for (DadosCromossomo d : cromossomos) {
            for (int i = 0; i < d.getCromossomo().length; i++) {
                resultado = r.nextFloat();
                if (resultado < taxaDeMutacao) {
                    d.getCromossomo()[i] = !d.getCromossomo()[i];
                    d.setCalculado(false);
                    //System.out.println("Mutou");
                }
            }
        }
        avaliaCromossomos(cromossomos);
    }
    
    public DadosCromossomo selecionaCromossomo(int[] valores, ArrayList<DadosCromossomo> cromossomos) {
        Random r = new Random();
        int selecao = r.nextInt(valores[0]);
        for (int i = valores.length - 1; i >= 0; i--) {
            if (selecao < valores[i]) {
                return cromossomos.get(i);
            }
        }
        
        return null;
    }
    
    private int[] calculaAcumulado(ArrayList<DadosCromossomo> cromossomos) {
        int[] resultado = new int[cromossomos.size()];
            for (int i = cromossomos.size(); i > 0; i--) {
                resultado[i - 1] = cromossomos.get(i - 1).getValor();
                if (i < cromossomos.size()) {
                    resultado[i - 1] += resultado[i];
                }
            }
        return resultado;
    }
    
    public void setObjectValue(Object value) {
        if (!(controle == null)) {
            PropertyDescriptor pd;

            try {
                pd = new PropertyDescriptor("text", controle.getClass());
                pd.getWriteMethod().invoke(controle, value);
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Gene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setObjectValue(String value, double barra) {
        mensagem.set(value);
        StatusBar sb = (StatusBar) controle;
        sb.progressProperty().set(barra);
    }
    
    public void executa() {
        
        
        
        new Thread() {
            Genetico g = Genetico.this;
            long tempoInicial, tempoAtual, tempoFinal;
            
            @Override
            public void run() {
                tempoInicial = System.currentTimeMillis();
                tempoFinal = tempoInicial + 20;
                geraCromossomos();
                do {
                    avaliaCromossomos(cromossomos);
                    realizaCruzamento();
                    //System.out.println("Antes da Mutação: ");
                    //System.out.println(getCromossomosDados(cromossomos));
                    //realizaMutacao(cromossomos);
                    //System.out.println("Após a Mutação: ");
                    //System.out.println(getCromossomosDados(cromossomos));
                    geracao++;
                    //System.out.println(g.getCromossomosBits(cromossomos));
                    if (System.currentTimeMillis() >= tempoFinal) {
                        setObjectValue("Geração: " + String.format("%8d", geracao) + ": " + getCromossomosDados(cromossomos.get(0)), (1.0 * geracao / geracoes));
                        tempoFinal = System.currentTimeMillis() + 50;
                    }
                    /*
                    try {
                        sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Genetico.class.getName()).log(Level.SEVERE, null, ex);
                    } */
                } while (geracao < geracoes);
                setObjectValue("Geração: " + String.format("%8d", geracao) + ": " + getCromossomosDados(cromossomos.get(0)), (1.0 * geracao / geracoes));
                System.out.println(geracao);
                System.out.println("Melhor Resultado: ");
                System.out.println(getCromossomosDados(cromossomos));
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
        private Object controle = null;
        private StringProperty mensagem = null;
        private Service<Void> servicoMensagem = null;
        
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
        
        public Builder controleStatus(Object controle) {
            this.controle = controle;
            return this;
        }
        
        public Builder mensagemStatus(StringProperty mensagem) {
            this.mensagem = mensagem;
            return this;
        }
        
        public Builder servicoMensagem(Service<Void> servico) {
            servicoMensagem = servico;
            return this;
        }
    }
    
    public class DadosCromossomo implements Comparable<DadosCromossomo> {
        private boolean[] cromossomo;
        private int valor;
        private int peso;
        private int volume;
        private boolean calculado;
        
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

        public boolean isCalculado() {
            return calculado;
        }

        public void setCalculado(boolean calculado) {
            this.calculado = calculado;
        }
        
        public void limpaValores() {
            this.valor = 0;
            this.peso = 0;
            this.volume = 0;
        }

        @Override
        public int compareTo(DadosCromossomo o) {
            if (this.getValor() > o.getValor()) {
                return -1;
            }
            if (this.getValor() < o.getValor()) {
                return 1;
            }
            return 0;
        }
    }
}
