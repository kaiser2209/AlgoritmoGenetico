/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.grafico;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import util.Genetico;

/**
 * FXML Controller class
 *
 * @author guard
 */
public class GraficoController implements Initializable {

    @FXML
    private LineChart<Integer, Integer> graficoEvolucao;
    
    private ArrayList<Genetico.DadosCromossomo> melhores;
    private int geracoes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setInformacoes(ArrayList<Genetico.DadosCromossomo> melhores, int geracoes) {
        this.melhores = melhores;
        this.geracoes = geracoes;

        int[] dadosGeracao = new int[melhores.size()];
        XYChart.Data[] linhaValor = new XYChart.Data[melhores.size()];
       
        
        XYChart.Series<Integer, Integer> valor = new XYChart.Series<>();
        valor.setName("Valor");
        XYChart.Series<Integer, Integer> peso = new XYChart.Series<>();
        peso.setName("Peso");
        XYChart.Series<Integer, Integer> volume = new XYChart.Series<>();
        volume.setName("Volume");
        
        
        
        for (int i = 0; i < melhores.size(); i++) {
            valor.getData().add(new XYChart.Data<>(i, melhores.get(i).getValor()));
            peso.getData().add(new XYChart.Data<>(i, melhores.get(i).getPeso()));
            volume.getData().add(new XYChart.Data<>(i, melhores.get(i).getVolume()));
        }
        
        graficoEvolucao.getData().addAll(valor);
    }
    
}
