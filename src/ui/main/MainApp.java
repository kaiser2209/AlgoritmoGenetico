/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Dados;
import ui.dados.AdicionaDadosController;
import util.Gene;

/**
 *
 * @author usuario.local
 */
public class MainApp extends Application {
    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        Gene g = new Gene();
        ArrayList<byte[]> lista = g.geraCromossomos();
        for (byte[] l : lista) {
            for (byte b : l) {
                System.out.print(b);
            }
            System.out.println("");
        }
        
*/
        

/*        
        for (byte[] bytes : g.geraCromossomos()) {
            for (byte b : bytes) {
                System.out.print(b);
            }
            System.out.println("");
        }
*/        
        Object d = new Dados(1, 2, 3);
        Class c = d.getClass();
        String campo = "peso";
        Field valor = c.getDeclaredField(campo);
        valor.setAccessible(true);
        //System.out.println(valor.get(d));
        Locale.setDefault(new Locale("pt", "BR"));
        abrirTelaPrincipal(primaryStage);
        
        ArrayList<Dados> dados = new ArrayList<>();
        dados.add(new Dados(2, 4, 8));
        dados.add(new Dados(3, 6, 9));
        dados.add(new Dados(7, 8, 2));
        
        Gene g = new Gene.Builder(dados)
                .objetivo("valor", true)
                .restricoes(new String[]{"peso", "volume"}, new String[]{"<=", "<="}, new double[]{10, 15})
                .taxaDeCruzamento(0.9f)
                .taxaDeMutacao(0.05f)
                .geracoesDesejadas(2000000)
                .statusControle(controller.txtStatus)
                .build();
        
        g.start();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void abrirTelaPrincipal(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        controller = (MainController) loader.getController();
        stage.setTitle("Algoritmo Genético");
        stage.setMaximized(false);
        Scene cena = new Scene(root);
        stage.setScene(cena);
        stage.show();
    }
}
