/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.dados.AdicionaDadosController;
import util.Gene;

/**
 *
 * @author usuario.local
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ArrayList<byte[]> lista = Gene.geraCromossomos(20, 6);
        for (byte[] l : lista) {
            for (byte b : l) {
                System.out.print(b);
            }
            System.out.println("");
        }
        Locale.setDefault(new Locale("pt", "BR"));
        abrirTelaPrincipal(primaryStage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void abrirTelaPrincipal(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        stage.setTitle("Algoritmo Genético");
        stage.setMaximized(false);
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }
}
