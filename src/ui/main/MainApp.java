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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DadosGen;
import util.Genetico;

/**
 *
 * @author usuario.local
 */
public class MainApp extends Application {
    private MainController controller;
    private Service<Void> mensagem;
    private StringProperty mensagemStatus = new SimpleStringProperty();

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        Object d = new Dados(1, 2, 3);
        Class c = d.getClass();
        String campo = "peso";
        Field valor = c.getDeclaredField(campo);
        valor.setAccessible(true);
        //System.out.println(valor.get(d));
*/
        Locale.setDefault(new Locale("pt", "BR"));
        abrirTelaPrincipal(primaryStage);
        
        ArrayList<DadosGen> dados = new ArrayList<>();
        dados.add(new DadosGen(2, 4, 8));
        dados.add(new DadosGen(3, 6, 9));
        dados.add(new DadosGen(7, 8, 2));
        dados.add(new DadosGen(2, 6, 4));
        dados.add(new DadosGen(3, 1, 1));
        dados.add(new DadosGen(3, 5, 7));
        dados.add(new DadosGen(3, 4, 6));
        dados.add(new DadosGen(3, 3, 3));
        dados.add(new DadosGen(8, 6, 1));
        dados.add(new DadosGen(4, 1, 1));
        dados.add(new DadosGen(4, 3, 2));
        dados.add(new DadosGen(6, 3, 7));
        dados.add(new DadosGen(9, 9, 4));
        
        Genetico gene = new Genetico.Builder(dados)
                .populacao(20)
                .taxaDeCruzamento(0.9f)
                .taxaDeMutacao(0.05f)
                .geracoesDesejadas(2000000l)
                .limiteDePeso(30)
                .limiteDeVolume(25)
                .controleStatus(controller.getStatusBar())
                .mensagemStatus(mensagemStatus)
                .build();
        
        gene.executa();
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void abrirTelaPrincipal(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        controller = (MainController) loader.getController();
        criarTarefa();
        controller.getStatusBar().textProperty().bind(mensagem.messageProperty());
        controller.getStatusLabel().textProperty().bind(mensagem.messageProperty());
        mensagem.start();
        stage.setTitle("Algoritmo Genético");
        stage.setMaximized(false);
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }
    
    private void criarTarefa() {
        mensagem = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            updateMessage(mensagemStatus.get());
                            Thread.sleep(100);
                        }
                    }
                    
                };
            }
            
        };
    }
}
