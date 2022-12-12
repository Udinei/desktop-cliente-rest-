package conta.tela;

import conta.to.ContaTO;
import conta.to.NumeroTO;
import conta.to.TransferenciaTO;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

// Responsável por desenhar a tela de transferencia com a tecnologia javafx.
public class TransferenciaFrm {

    private TextField tfDebito;
    private TextField tfNomeDebito;
    private TextField tfCredito;
    private TextField tfNomeCredito;
    private TextField tfValor;

    // Novo ----------------
    // Url do sistema rest
    private static final String URL = "http://localhost:8080/";
    // Consumidor rest com json
    private RestTemplate rest;
    // --------------------

    public TransferenciaFrm() {
        // configuração do rest para processar json.
        rest = new RestTemplate();
        rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    private void limpar() {
        tfDebito.setText("");
        tfNomeDebito.setText("");
        tfCredito.setText("");
        tfNomeCredito.setText("");
        tfValor.setText("");
    }

    private Integer get(String valor) {
        try {
            return Integer.valueOf(valor);
        } catch (Exception e) {
            return null;
        }
    }

    private void mensagem(String texto) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transferência Bancária");
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.showAndWait();
    }

    private void get(TextField tfEntrada, TextField tfSaida) {
        try {
            // Novo get via rest ----------------
            var to = new NumeroTO(get(tfEntrada.getText()));
            var resp = rest.postForEntity(URL + "/transferencia/getconta", to, ContaTO.class);
            if (resp.getStatusCode() == OK) {
                ContaTO conta = resp.getBody();
                if (isNull(conta.getCorrentista())) {
                    tfSaida.setText("");
                } else {
                    tfSaida.setText(toIso(conta.getCorrentista()) + " - Saldo R$ " + conta.getSaldo());
                }
            }
            // --------------------
        } catch (Exception e) {
            tratarErroRest(e.getMessage());
        }
    }

    private BigDecimal get() {
        try {
            return new BigDecimal(tfValor.getText());
        } catch (Exception e) {
            return null;
        }
    }

    private FlowPane montarTela() {
        var pn = new FlowPane();
        pn.setHgap(10);
        pn.setVgap(10);

        pn.getChildren().add(new Label(" Conta Débito:"));
        tfDebito = new TextField();
        tfDebito.setPrefWidth(50);
        tfDebito.focusedProperty().addListener((o, v, n) -> {
            if (!n) get(tfDebito, tfNomeDebito);
        });

        pn.getChildren().add(tfDebito);

        tfNomeDebito = new TextField();
        tfNomeDebito.setPrefWidth(300);
        tfNomeDebito.setEditable(false);
        pn.getChildren().add(tfNomeDebito);

        pn.getChildren().add(new Label(" Conta Crédito:"));
        tfCredito = new TextField();
        tfCredito.setPrefWidth(50);
        tfCredito.focusedProperty().addListener((o, v, n) -> {
            if (!n) get(tfCredito, tfNomeCredito);
        });
        pn.getChildren().add(tfCredito);

        tfNomeCredito = new TextField();
        tfNomeCredito.setEditable(false);
        tfNomeCredito.setPrefWidth(300);
        pn.getChildren().add(tfNomeCredito);

        pn.getChildren().add(new Label(" Valor R$:"));
        tfValor = new TextField();
        tfValor.setPrefWidth(200);
        pn.getChildren().add(tfValor);

        var bt = new Button();
        bt.setOnAction((ev) -> {
            try {
                // Nova transfencia via rest ----------------
                var to = new TransferenciaTO(get(tfDebito.getText()), get(tfCredito.getText()), get());
                var req = new HttpEntity<TransferenciaTO>(to, new HttpHeaders());
                var resp = rest.exchange(new URI(URL + "/transferencia/transferir"), PUT, req, String.class);
                if (resp.getStatusCode() == NO_CONTENT) {
                    limpar();
                    mensagem("Transferência feita com sucesso!");
                }
            } catch (HttpClientErrorException e) {
                var erro = new ResponseEntity<>(e.getResponseBodyAsString(), BAD_REQUEST);
                if (e.getStatusCode().value() == 400) {
                    mensagem(toIso(erro.getBody()));
                } else {
                    tratarErroRest(toIso(erro.getBody()));
                }
            } catch (Exception e) {
                tratarErroRest(e.getMessage());
            }
            //-----------------------------------------------
        });
        bt.setText("Transferir");
        pn.getChildren().add(bt);
        return pn;
    }

    public void mostrar(Stage stage) {
        stage.setTitle("Adaptador JavaFX");
        var scene = new Scene(montarTela(), 500, 100);
        stage.setScene(scene);
        stage.show();
    }

    // Novo trata mensgem da comunicação http ----------------
    public void tratarErroRest(String erro) {
        if (erro.contains("Connection refused") || erro.contains("404") || erro.contains("405")) {
            mensagem("Sistema fora de ar, tenta mais tarde.");
        } else {
            mensagem("Erro não tratado:" + erro);
        }
    }
    // ---------------------------------------

    // Novo trata string utf8 ----------------
    public static String toIso(String str) {
        var utf8 = Charset.forName("UTF-8");
        var iso88591 = Charset.forName("ISO-8859-1");
        var ib = ByteBuffer.wrap(str.getBytes());
        var data = utf8.decode(ib);
        var outputBuffer = iso88591.encode(data);
        var outputData = outputBuffer.array();
        return new String(outputData);
    }
    // ---------------------------------------
}
