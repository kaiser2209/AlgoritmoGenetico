/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Dados;
import org.controlsfx.control.StatusBar;
import ui.dados.AdicionaDadosController;

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
    private TableView<Dados> tblDados;
    private ArrayList<Dados> dados = new ArrayList<Dados>();
    private ObservableList<Dados> dadosTabela;
    @FXML
    public TextField txtStatus;
    @FXML
    private TextField txtLimitePeso;
    @FXML
    private TextField txtLimiteVolume;
    @FXML
    private StatusBar sbStatus;
    @FXML
    private Label lblStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        definirEventos();
        valoresIniciais();
        configurarTabela();
        carregarDados();
    }    
    
    private void valoresIniciais() {
        slPopulacao.setValue(6);
        slTaxaCruzamento.setValue(90);
        slTaxaMutacao.setValue(5);
    }
    
    private void configurarTabela() {
        TableColumn<Dados, Integer> colPeso = new TableColumn("Peso");
        TableColumn<Dados, Integer> colVolume = new TableColumn("Volume");
        TableColumn<Dados, Integer> colValor = new TableColumn("Valor");
        
        colPeso.setCellValueFactory(new PropertyValueFactory<Dados, Integer>("peso"));
        colVolume.setCellValueFactory(new PropertyValueFactory<Dados, Integer>("volume"));
        colValor.setCellValueFactory(new PropertyValueFactory<Dados, Integer>("valor"));
        colPeso.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colVolume.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colValor.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
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
    
    public void addDados(Dados dados) {
        this.dados.add(dados);
        carregarDados();
    }
    
    public TextField getStatusText() {
        return this.txtStatus;
    }
    
    public StatusBar getStatusBar() {
        return this.sbStatus;
    }
    
    public Label getStatusLabel() {
        return this.lblStatus;
    }

    @FXML
    private void abrirTelaAdicionaDados(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/dados/AdicionaDados.fxml"));
        Parent root = loader.load();
        Scene cena = new Scene(root);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle("Incluir Dados");
        stage.setScene(cena);
        AdicionaDadosController controller = (AdicionaDadosController) loader.getController();
        controller.setMainController(this);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    
}
