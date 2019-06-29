/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.resultado;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DadosGen;
import ui.main.MainController;
import util.Genetico;

/**
 * FXML Controller class
 *
 * @author guard
 */
public class ResultadoController implements Initializable {
    private Genetico.DadosCromossomo melhor;
    private int melhorGeracao;
    private ArrayList<Dados> dados = new ArrayList<>();
    private ObservableList<Dados> dadosTabela;
    @FXML
    private TableView<Dados> tblResultado;
    @FXML
    private TextField txtGeracao;
    @FXML
    private TextField txtValor;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtVolume;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        melhor = Genetico.getMelhorIndividuo();
        melhorGeracao = Genetico.getMelhorGeracao();
        configuracaoTabela();
        defDados();
        carregarDados();
    }  
    
    private void defDados() {
        for (int i = 0; i < melhor.getCromossomo().length; i++) {
            if (melhor.getCromossomo()[i]) {
                dados.add(new Dados(i + 1, MainController.getDados().get(i)));
            }
        }
    }
    
    private void configuracaoTabela() {
        TableColumn<Dados, Integer> colId = new TableColumn("Id");
        TableColumn<Dados, Integer> colPeso = new TableColumn("Peso");
        TableColumn<Dados, Integer> colVolume = new TableColumn("Volume");
        TableColumn<Dados, Integer> colValor = new TableColumn("Valor");
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colId.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colPeso.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colVolume.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colValor.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        tblResultado.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colId.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        colPeso.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        colVolume.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        colValor.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        
        tblResultado.getColumns().addAll(colId, colPeso, colVolume, colValor);
    }
    
    private void carregarDados() {
        dadosTabela = FXCollections.observableArrayList(dados);
        tblResultado.setItems(dadosTabela);
        
        txtGeracao.setText(String.valueOf(melhorGeracao));
        txtValor.setText(String.valueOf(melhor.getValor()));
        txtPeso.setText(String.valueOf(melhor.getPeso()));
        txtVolume.setText(String.valueOf(melhor.getVolume()));
    }
    
    public class Dados {
        private int id;
        private DadosGen dado;
        
        public Dados(int id, DadosGen dado) {
            this.id = id;
            this.dado = dado;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public DadosGen getDado() {
            return dado;
        }

        public void setDado(DadosGen dado) {
            this.dado = dado;
        }
        
        public int getPeso() {
            return this.dado.getPeso();
        }
        
        public int getVolume() {
            return this.dado.getVolume();
        }
        
        public int getValor() {
            return this.dado.getValor();
        }
    } 
    
}
