/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import model.Dados;

/**
 * FXML Controller class
 *
 * @author usuario.local
 */
public class MainController implements Initializable {

    @FXML
    private Slider slPopulacao;
    @FXML
    private TextField txtPopulacao;
    @FXML
    private Slider slTaxaCruzamento;
    @FXML
    private TextField txtTaxaCruzamento;
    @FXML
    private Slider slTaxaMutacao;
    @FXML
    private TextField txtTaxaMutacao;
    @FXML
    private ComboBox<String> cboTipoObjetivo;
    @FXML
    private ComboBox<String> cboCampoObjetivo;
    @FXML
    private ComboBox<String> cboRestricao1;
    @FXML
    private ComboBox<String> cboTipoRestricao1;
    @FXML
    private TextField txtValorRestricao1;
    @FXML
    private ComboBox<String> cboRestricao2;
    @FXML
    private ComboBox<String> cboTipoRestricao2;
    @FXML
    private TextField txtValoreRestricao2;
    @FXML
    private TableView<Dados> tblDados;
    private ArrayList<Dados> dados;
    private ObservableList<Dados> dadosTabela;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        definirEventos();
        populate();
        configurarTabela();
        carregarDados();
    }    
    
    private void configurarTabela() {
        TableColumn<Dados, Integer> colPeso = new TableColumn("Peso");
        TableColumn<Dados, Integer> colVolume = new TableColumn("Volume");
        TableColumn<Dados, Integer> colValor = new TableColumn("Valor");
        
        colPeso.setCellValueFactory(new PropertyValueFactory<Dados, Integer>("peso"));
        colVolume.setCellValueFactory(new PropertyValueFactory<Dados, Integer>("volume"));
        colValor.setCellValueFactory(new PropertyValueFactory<Dados, Integer>("valor"));
        
        tblDados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colPeso.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        colVolume.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        colValor.setMaxWidth(1f * Integer.MAX_VALUE * 60);
        
        tblDados.getColumns().addAll(colPeso, colVolume, colValor);
    }
    
    private void carregarDados() {
        dadosTabela = FXCollections.observableArrayList(dados);
        tblDados.setItems(dadosTabela);
    }
    
    private void populate() {
        cboTipoObjetivo.getItems().addAll(
            "Máx.",
            "Min.");
        cboCampoObjetivo.getItems().addAll(
            "Valor",
            "Peso",
            "Volume");
        cboRestricao1.getItems().addAll(
            "Valor",
            "Peso",
            "Volume");
        cboRestricao2.getItems().addAll(
            "Valor",
            "Peso",
            "Volume");
        cboTipoRestricao1.getItems().addAll(
            "<",
            "<=",
            "=",
            "<>",
            ">",
            ">=");
        cboTipoRestricao2.getItems().addAll(
            "<",
            "<=",
            "=",
            "<>",
            ">",
            ">=");
    }
    
    private void definirEventos() {
        slPopulacao.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                txtPopulacao.setText(String.valueOf(newValue.intValue()));
            }
            
        });
        
        slTaxaCruzamento.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                txtTaxaCruzamento.setText(String.format("%.1f%%", newValue.doubleValue()));
            }
            
        });
        
        slTaxaMutacao.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                txtTaxaMutacao.setText(String.format("%.1f%%", newValue.doubleValue()));
            }
            
        });
    }
    
}
