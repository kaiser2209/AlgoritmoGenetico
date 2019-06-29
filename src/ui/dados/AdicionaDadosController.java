/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dados;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DadosGen;
import ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author usuario.local
 */
public class AdicionaDadosController implements Initializable {

    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtValor;
    @FXML
    private TextField txtVolume;
    private MainController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void limparDados() {
        txtPeso.clear();
        txtValor.clear();
        txtVolume.clear();
    }

    @FXML
    private void salvarDados(ActionEvent event) {
        DadosGen d = new DadosGen(
            Integer.parseInt(txtValor.getText()),
            Integer.parseInt(txtPeso.getText()),
            Integer.parseInt(txtVolume.getText()));
        mainController.addDados(d);
        limparDados();
    }

    @FXML
    private void limparDados(ActionEvent event) {
        limparDados();
    }

    @FXML
    private void sair(ActionEvent event) {
        Button b = (Button) event.getSource();
        Stage stage = (Stage) b.getScene().getWindow();
        stage.close();
    }
    
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
}
