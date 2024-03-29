/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.main;

import excecoes.ArquivoNoFormatoInvalidoException;
import excecoes.DadosInsuficientesException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DadosGen;
import org.controlsfx.control.StatusBar;
import ui.dados.AdicionaDadosController;
import ui.grafico.GraficoController;
import ui.resultado.ResultadoController;
import util.AlertDialog;
import util.Genetico;

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
    private TableView<DadosGen> tblDados;
    private static ArrayList<DadosGen> dados = new ArrayList<>();
    private ObservableList<DadosGen> dadosTabela;
    public TextField txtStatus;
    @FXML
    private TextField txtLimitePeso;
    @FXML
    private TextField txtLimiteVolume;
    @FXML
    private StatusBar sbStatus;
    private Label lblStatus;
    @FXML
    private RadioButton chkParadaPorGeracao;
    @FXML
    private TextField txtNumeroGeracoes;
    @FXML
    private RadioButton chkParadaPorConvergencia;
    private Genetico gene;
    private Service<Void> mensagem;
    private StringProperty mensagemStatus = new SimpleStringProperty();
    private ToggleGroup grMetodoDeParada = new ToggleGroup();
    @FXML
    private Button btnAdiciona;
    @FXML
    private Button btnApaga;
    @FXML
    private Button btnLimpaTabela;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnCarregar;
    @FXML
    private BorderPane principal;
    @FXML
    private Button btnResultado;
    @FXML
    private Button btnGrafico;
    @FXML
    private Button btnExecutar;
    @FXML
    private Button btnParar;

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
        estadoBotoes();
    }    
    
    private void valoresIniciais() {
        slPopulacao.setValue(6);
        slTaxaCruzamento.setValue(90);
        slTaxaMutacao.setValue(5);
        chkParadaPorGeracao.setToggleGroup(grMetodoDeParada);
        chkParadaPorConvergencia.setToggleGroup(grMetodoDeParada);
        
    }
    
    private void configurarTabela() {
        TableColumn<DadosGen, Integer> colPeso = new TableColumn("Peso");
        TableColumn<DadosGen, Integer> colVolume = new TableColumn("Volume");
        TableColumn<DadosGen, Integer> colValor = new TableColumn("Valor");
        
        colPeso.setCellValueFactory(new PropertyValueFactory<DadosGen, Integer>("peso"));
        colVolume.setCellValueFactory(new PropertyValueFactory<DadosGen, Integer>("volume"));
        colValor.setCellValueFactory(new PropertyValueFactory<DadosGen, Integer>("valor"));
        colPeso.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colVolume.setStyle( "-fx-alignment: CENTER-RIGHT;");
        colValor.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        tblDados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colPeso.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        colVolume.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        colValor.setMaxWidth(1f * Integer.MAX_VALUE * 40);
        
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
        
        tblDados.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<DadosGen>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends DadosGen> c) {
                estadoBotoes();
            }
            
        });
    }
    
    public void addDados(DadosGen dados) {
        this.dados.add(dados);
        carregarDados();
        estadoBotoes();
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

    @FXML
    private void salvarDados(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showSaveDialog((Stage) principal.getScene().getWindow());
        
        if (file != null) {
            salvarCSV(file);
        }
    }
    
    private void salvarCSV(File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (DadosGen d : dados) {
                writer.print(d.getValor());
                writer.print(",");
                writer.print(d.getPeso());
                writer.print(",");
                writer.println(d.getVolume());
            }
        } catch (IOException e) {

        }
        
    }

    @FXML
    private void executarAlgoritmo(ActionEvent event) { 
        btnParar.setDisable(false);
        btnExecutar.setDisable(true);
        int populacao = (int) slPopulacao.getValue();
        float taxaDeCruzamento = (float) slTaxaCruzamento.getValue();
        float taxaDeMutacao = (float) slTaxaMutacao.getValue();
        long geracoesDesejadas = Long.parseLong(txtNumeroGeracoes.getText());
        int limiteDePeso = Integer.parseInt(txtLimitePeso.getText());
        int limiteDeVolume = Integer.parseInt(txtLimiteVolume.getText());
        try {
            criarTarefa();
            getStatusBar().textProperty().bind(mensagem.messageProperty());
            mensagem.start();
            Genetico.melhores.clear();
            gene = new Genetico.Builder(dados)
                    .populacao(populacao)
                    .taxaDeCruzamento(taxaDeCruzamento)
                    .taxaDeMutacao(taxaDeMutacao)
                    .geracoesDesejadas(geracoesDesejadas)
                    .limiteDePeso(limiteDePeso)
                    .limiteDeVolume(limiteDeVolume)
                    .controleStatus(getStatusBar())
                    .mensagemStatus(mensagemStatus)
                    .usarParadaPorConvergencia(chkParadaPorConvergencia.isSelected())
                    .setMainController(this)
                    .build();
        

            gene.executa();
        } catch (DadosInsuficientesException ex) {
            AlertDialog.showMessage("Erro", ex.getMessage(), Alert.AlertType.ERROR);
        }
        
    }

    @FXML
    private void pararAlgoritmo(ActionEvent event) {
        btnParar.setDisable(true);
        btnExecutar.setDisable(false);
        gene.para();
        gene = null;
    }

    @FXML
    private void mostraResultado(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/resultado/Resultado.fxml"));
        Parent root = loader.load();
        Scene cena = new Scene(root);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle("Resultado");
        stage.setScene(cena);
        ResultadoController controller = (ResultadoController) loader.getController();
        //controller.setInformacoes(Genetico.melhores, Genetico.totalGeracoes);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void mostraGrafico(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/grafico/Grafico.fxml"));
        Parent root = loader.load();
        Scene cena = new Scene(root);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle("Gráfico de Evolução");
        stage.setScene(cena);
        GraficoController controller = (GraficoController) loader.getController();
        controller.setInformacoes(Genetico.melhores, Genetico.totalGeracoes);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void apagarDadoSelecionado(ActionEvent event) {
        Optional<ButtonType> confirmacao = AlertDialog.showConfirmMessage("Confirmação", "Deseja excluir o dados selecionado?", Alert.AlertType.CONFIRMATION);
        if (confirmacao.get() == ButtonType.OK) {
            dados.remove(tblDados.getSelectionModel().getSelectedItem());
        }
        
        carregarDados();
        estadoBotoes();
    }

    @FXML
    private void limparDadosTabela(ActionEvent event) {
        Optional<ButtonType> confirmacao = AlertDialog.showConfirmMessage("Confirmação", "Deseja excluir todos os dados da tabela?", Alert.AlertType.CONFIRMATION);
        if (confirmacao.get() == ButtonType.OK) {
            dados.clear();
            carregarDados();
            estadoBotoes();
        }
    }
    
    public void estadoBotoes() {
        //System.out.println(tblDados.getSelectionModel().getSelectedIndex());
        btnApaga.setDisable(dados.isEmpty() || tblDados.getSelectionModel().getSelectedIndex() < 0);
        btnLimpaTabela.setDisable(dados.isEmpty());
        btnResultado.setDisable(Genetico.melhores.size() == 0);
        btnGrafico.setDisable(Genetico.melhores.size() == 0);
        btnExecutar.setDisable(false);
        btnParar.setDisable(true);
    }

    @FXML
    private void abrirDados(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showOpenDialog((Stage) principal.getScene().getWindow());
        
        if (file == null) {
            return;
        }
        
        String linha = null;
        BufferedReader stream = null;
        
        try {
            ArrayList<DadosGen> lista = new ArrayList<>();
            stream = new BufferedReader(new FileReader(file));
            while (((linha = stream.readLine()) != null)) {
                String dadosLidos[] = linha.split(",");
                if (dadosLidos.length != 3) {
                    throw new ArquivoNoFormatoInvalidoException("O arquivo informado não pode ser aberto.");
                }
                lista.add(new DadosGen(Integer.valueOf(dadosLidos[0]),
                        Integer.valueOf(dadosLidos[1]),
                        Integer.valueOf(dadosLidos[2])));
            }
            
            dados = lista;
            carregarDados();
            estadoBotoes();
        } catch (FileNotFoundException e) {
            AlertDialog.showMessage("Erro", e.getMessage(), Alert.AlertType.ERROR);
        } catch (ArquivoNoFormatoInvalidoException e) {
            AlertDialog.showConfirmMessage("Erro", e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            AlertDialog.showConfirmMessage("Erro", e.getMessage(), Alert.AlertType.ERROR);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    AlertDialog.showConfirmMessage("Erro", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
        
    }
    
    public static ArrayList<DadosGen> getDados() {
        return MainController.dados;
    }
    
}
