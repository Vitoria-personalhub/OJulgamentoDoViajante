import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Jogo "O Julgamento do Viajante"
 game bar*
 * - Simula uma aventura com escolhas que alteram atributos:
 *   Saúde, Honra e Dinheiro.
 * - Contém missões, sub-missões e cenários.
 * - Implementado em JavaFX com máquina de estados.
 */
public class MainFX extends Application {

    // ------------------------------
    // ATRIBUTOS DO JOGADOR
    // ------------------------------
    private double saude = 50;    // Saúde do jogador (0-100)
    private double honra = 50;    // Honra do jogador (0-100)
    private double dinheiro = 50; // Dinheiro do jogador (0-100)

    // ------------------------------
    // ELEMENTOS DE UI - LADO ESQUERDO
    // ------------------------------
    private Label missaoLabel;        // Mostra a missão atual
    private Label resultadoLabel;     // Mostra resultados das ações
    private Button opcao1 = new Button(); // Botão da primeira opção
    private Button opcao2 = new Button(); // Botão da segunda opção
    private Button opcao3 = new Button(); // Botão da terceira opção
    private Button reiniciarBtn = new Button("Reiniciar"); // Botão para reiniciar jogo

    // ------------------------------
    // ELEMENTOS DE UI - LADO DIREITO (ATRIBUTOS)
    // ------------------------------
    private ProgressBar saudeBar = new ProgressBar(0.5);
    private ProgressBar honraBar = new ProgressBar(0.5);
    private ProgressBar dinheiroBar = new ProgressBar(0.5);
    private Label saudeValor = new Label("50");
    private Label honraValor = new Label("50");
    private Label dinheiroValor = new Label("50");

    // ------------------------------
    // MÁQUINA DE ESTADOS DAS MISSÕES
    // ------------------------------
    private int estado = 0; // Estado inicial do jogo (Missão 1)
    private static final double PAUSA_SEGUNDOS = 1.0; // Pausa antes de Game Over ou transição

    @Override
    public void start(Stage stage) {

        // ------------------------------
        // BACKGROUND
        // ------------------------------
        BackgroundImage backgroundImage = null;
        try {
            // Tenta carregar imagem de fundo
            Image img = new Image("file:imagens/fundo.jpg", 1200, 800, false, true);
            backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        } catch (Exception e) {
            System.out.println("Fundo não encontrado; usando fundo escuro.");
        }

        BorderPane root = new BorderPane(); // Layout principal
        if (backgroundImage != null) root.setBackground(new Background(backgroundImage));
        else root.setStyle("-fx-background-color: linear-gradient(#0b0b0b, #1b1b1b);");

        // ------------------------------
        // LADO ESQUERDO - Texto e Botões
        // ------------------------------
        VBox textoBox = new VBox(16);
        textoBox.setPadding(new Insets(22));
        textoBox.setAlignment(Pos.TOP_LEFT);
        textoBox.setMaxWidth(520);
        textoBox.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.55),
                new CornerRadii(10), Insets.EMPTY)));

        // Label da missão
        missaoLabel = new Label();
        missaoLabel.setFont(Font.font(20));
        missaoLabel.setWrapText(true);
        missaoLabel.setTextFill(Color.WHITE);

        // Label de resultados da ação
        resultadoLabel = new Label();
        resultadoLabel.setFont(Font.font(16));
        resultadoLabel.setWrapText(true);
        resultadoLabel.setTextFill(Color.LIGHTGRAY);

        // Configura botões de escolha
        opcao1.setPrefWidth(480); opcao2.setPrefWidth(480); opcao3.setPrefWidth(480);
        opcao1.setPrefHeight(50); opcao2.setPrefHeight(50); opcao3.setPrefHeight(50);
        estiloBotao(opcao1); estiloBotao(opcao2); estiloBotao(opcao3);

        // Botão de reiniciar (inicialmente invisível)
        reiniciarBtn.setPrefWidth(480);
        reiniciarBtn.setPrefHeight(50);
        estiloBotao(reiniciarBtn);
        reiniciarBtn.setVisible(false); // só aparece após Game Over ou final
        reiniciarBtn.setOnAction(ev -> reiniciarJogo());

        // Adiciona elementos na VBox
        textoBox.getChildren().addAll(missaoLabel, opcao1, opcao2, opcao3, reiniciarBtn, resultadoLabel);

        // ------------------------------
        // LADO DIREITO - BARRAS DE ATRIBUTOS
        // ------------------------------
        VBox atributosBox = new VBox(18);
        atributosBox.setPadding(new Insets(24));
        atributosBox.setAlignment(Pos.TOP_CENTER);

        // Configura cores das barras
        configurarBarra(saudeBar, Color.CADETBLUE);
        configurarBarra(honraBar, Color.DARKRED);
        configurarBarra(dinheiroBar, Color.web("#DAA520"));

        // Labels das barras
        Label lblSaude = criarLabel("Saúde", 16);
        Label lblHonra = criarLabel("Honra", 16);
        Label lblDinheiro = criarLabel("Dinheiro", 16);

        // Configura valores
        saudeValor.setFont(Font.font(16)); saudeValor.setTextFill(Color.WHITE);
        honraValor.setFont(Font.font(16)); honraValor.setTextFill(Color.WHITE);
        dinheiroValor.setFont(Font.font(16)); dinheiroValor.setTextFill(Color.WHITE);

        // Agrupa cada barra com seu label
        VBox saudeBox = new VBox(6, lblSaude, saudeBar, saudeValor);
        VBox honraBox = new VBox(6, lblHonra, honraBar, honraValor);
        VBox dinheiroBox = new VBox(6, lblDinheiro, dinheiroBar, dinheiroValor);

        atributosBox.getChildren().addAll(saudeBox, honraBox, dinheiroBox);

        // ------------------------------
        // Montagem principal
        // ------------------------------
        root.setLeft(textoBox);
        root.setRight(atributosBox);

        // ------------------------------
        // Eventos dos botões de escolha
        // ------------------------------
        opcao1.setOnAction(e -> escolherAcao(1));
        opcao2.setOnAction(e -> escolherAcao(2));
        opcao3.setOnAction(e -> escolherAcao(3));

        // Inicializa a tela
        atualizarTela();
        atualizarBarras();

        Scene scene = new Scene(root, 1100, 760);
        stage.setScene(scene);
        stage.setTitle("O julgamento do viajante");
        stage.show();
    }

    // ==============================
    // MÉTODOS DE UTILITÁRIOS VISUAIS
    // ==============================
    private void estiloBotao(Button b) {
        // Define estilo padrão e hover
        b.setStyle("-fx-background-color: rgba(80,80,80,0.9); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 8;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: rgba(110,110,110,0.95); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 8;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: rgba(80,80,80,0.9); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 8;"));
    }

    private Label criarLabel(String texto, int tamanho) {
        Label l = new Label(texto);
        l.setFont(Font.font(tamanho));
        l.setTextFill(Color.WHITE);
        return l;
    }

    private void configurarBarra(ProgressBar barra, Color cor) {
        barra.setPrefWidth(220);
        barra.setProgress(0.5); // Valor inicial
        barra.setStyle("-fx-accent: " + toRgbString(cor) + ";");
    }

    // ==============================
    // LÓGICA DE JOGO
    // ==============================
    private void escolherAcao(int escolha) {
        if (isGameOverOrFinal()) return; // Bloqueia ações após Game Over ou Final

        String mensagem = "";
        int proximoEstado = estado;
        boolean avancar = true;

        switch (estado) {
            case 0 -> { // Missão 1
                if (escolha == 1) { alterarAtributos(0, +5, -20); mensagem = "Você ajuda financeiramente: -20 Dinheiro, +5 Honra."; proximoEstado = 7; }
                else if (escolha == 2) { alterarAtributos(0, -10, 0); mensagem = "Você ignora a moça: -10 Honra."; proximoEstado = 7; }
                else { mensagem = "Você parte em busca de alimento e encontra um bosque..."; proximoEstado = 2; }
            }
            case 2 -> { // Bosque
                if (escolha == 1) { mensagem = "Ao adentrar o bosque algo dá errado... 💀"; gameOverDelay(mensagem); return; }
                else if (escolha == 2) { mensagem = "Você evita o bosque e segue por outro caminho."; proximoEstado = 3; }
                else { mensagem = "Você procura alternativa e acaba perto do urso."; proximoEstado = 3; }
            }
            case 3 -> { // Cenário do Urso
                if (escolha == 1) { mensagem = "Você tenta enfrentar o urso — fatal. 💀"; gameOverDelay(mensagem); return; }
                else if (escolha == 2) { mensagem = "Você espera; o urso se volta para você!"; proximoEstado = 4; }
                else { mensagem = "Você segue e encontra uma residência."; proximoEstado = 5; }
            }
            case 4 -> { // Segunda escolha do Urso
                if (escolha == 1) { mensagem = "Você tenta lutar com o urso — fatídico. 💀"; gameOverDelay(mensagem); return; }
                else if (escolha == 2) { alterarAtributos(0, -10, 0); mensagem = "Você foge: -10 Honra."; proximoEstado = 5; }
                else { mensagem = "Você espera; o urso parte e você segue."; proximoEstado = 5; }
            }
            case 5 -> { // Residência
                if (escolha == 1) { alterarAtributos(0, -5, 0); mensagem = "Você invade a casa (-5 Honra). O dono aparece!"; proximoEstado = 6; }
                else if (escolha == 2) { alterarAtributos(0, -8, 0); mensagem = "Você pega da plantação (-8 Honra). Missão concluída!"; proximoEstado = 7; }
                else { mensagem = "Você continua procurando..."; avancar = false; proximoEstado = 5; }
            }
            case 6 -> { // Segunda escolha Residência
                if (escolha == 1) { alterarAtributos(0, -10, 0); mensagem = "Você foge com os itens (-10 Honra). Missão falhou."; proximoEstado = 7; }
                else if (escolha == 2) { alterarAtributos(0, -15, +20); mensagem = "Você luta com o dono (-15 Honra, +20 Dinheiro). Missão concluída!"; proximoEstado = 7; }
                else { mensagem = "Você hesita..."; avancar = false; proximoEstado = 6; }
            }
            default -> { mensagem = "Nada a fazer."; avancar = false; }
        }

        // Atualiza resultado e barras
        resultadoLabel.setText(mensagem);
        atualizarBarras();

        if (isGameOver()) { gameOverDelay("💀 GAME OVER 💀\nUm de seus atributos chegou a zero. Reinicie para tentar novamente."); return; }
        if (checkAndHandleFinals()) return;

        // Pausa antes de avançar
        if (avancar) {
            final int destino = proximoEstado;
            disableAllButtons();
            PauseTransition pause = new PauseTransition(Duration.seconds(PAUSA_SEGUNDOS));
            pause.setOnFinished(evt -> {
                estado = destino;
                resultadoLabel.setText("");
                atualizarTela();
                enableAllButtons();
            });
            pause.play();
        } else {
            atualizarTela();
        }
    }

    // ==============================
    // Atualiza textos da tela conforme estado
    // ==============================
    private void atualizarTela() {
        if (isGameOverOrFinal()) return;

        switch (estado) {
            case 0 -> {
                missaoLabel.setText("Missão 1 — Ajuda à Moça e às Crianças\nUma mulher e 5 crianças pedem ajuda. O que você faz?");
                opcao1.setText("Ajudar financeiramente"); opcao2.setText("Ignorar"); opcao3.setText("Buscar alimento");
            }
            case 2 -> {
                missaoLabel.setText("Cenário — Bosque\nVocê encontra um bosque no caminho. Qual ação?");
                opcao1.setText("Adentrar o bosque"); opcao2.setText("Seguir por outro local"); opcao3.setText("Buscar alternativa");
            }
            case 3 -> {
                missaoLabel.setText("Cenário — Urso\nVocê vê um urso devorando viajantes e protegendo cargas.");
                opcao1.setText("Tentar a sorte contra o urso"); opcao2.setText("Esperar o urso e saquear"); opcao3.setText("Procurar outro lugar");
            }
            case 4 -> {
                missaoLabel.setText("O urso se volta para você. Próxima ação:");
                opcao1.setText("Lutar contra o urso"); opcao2.setText("Correr por sua vida"); opcao3.setText("Esperar o urso");
            }
            case 5 -> {
                missaoLabel.setText("Cenário — Residência\nVocê encontra uma casa de madeira com uma plantação.");
                opcao1.setText("Invadir a casa e roubar tudo"); opcao2.setText("Pegar um pouco da plantação"); opcao3.setText("Continuar procurando");
            }
            case 6 -> {
                missaoLabel.setText("O dono chega! Próxima ação:");
                opcao1.setText("Deixar os itens e fugir"); opcao2.setText("Lutar contra o dono"); opcao3.setText("Caminho indefinido");
            }
            case 7 -> {
                missaoLabel.setText("Missão concluída! Reinicie para nova jornada.");
                opcao1.setText("-"); opcao2.setText("-"); opcao3.setText("-");
                disableAllButtons();
                reiniciarBtn.setVisible(true); // Botão aparece no final
            }
            default -> {
                missaoLabel.setText("");
                disableAllButtons();
            }
        }
    }

    // ==============================
    // Métodos auxiliares
    // ==============================
    private void alterarAtributos(double deltaSaude, double deltaHonra, double deltaDinheiro) {
        saude = clamp(saude + deltaSaude, 0, 100);
        honra = clamp(honra + deltaHonra, 0, 100);
        dinheiro = clamp(dinheiro + deltaDinheiro, 0, 100);
    }

    private void atualizarBarras() {
        saudeBar.setProgress(saude / 100.0);
        honraBar.setProgress(honra / 100.0);
        dinheiroBar.setProgress(dinheiro / 100.0);

        saudeValor.setText(String.valueOf((int) saude));
        honraValor.setText(String.valueOf((int) honra));
        dinheiroValor.setText(String.valueOf((int) dinheiro));
    }

    private boolean checkAndHandleFinals() {
        if (saude >= 100) { showFinal("🌿 FINAL DA VITALIDADE: você atingiu saúde máxima."); return true; }
        if (honra >= 100) { showFinal("⚔️ FINAL DO CAVALEIRO: sua honra é absoluta."); return true; }
        if (dinheiro >= 100) { showFinal("💰 FINAL DO MAGNATA: você alcançou riquezas infinitas."); return true; }
        return false;
    }

    private void showFinal(String texto) {
        missaoLabel.setText("🌟 FIM ESPECIAL 🌟");
        resultadoLabel.setText(texto + "\nReinicie para tentar outro caminho.");
        disableAllButtons();
        reiniciarBtn.setVisible(true);
    }

    private void gameOverDelay(String mensagem) {
        resultadoLabel.setText(mensagem);
        disableAllButtons();
        PauseTransition p = new PauseTransition(Duration.seconds(PAUSA_SEGUNDOS));
        p.setOnFinished(evt -> gameOver());
        p.play();
    }

    private void gameOver() {
        saude = honra = dinheiro = 0;
        atualizarBarras();
        missaoLabel.setText("💀 GAME OVER 💀");
        resultadoLabel.setText("Você não sobreviveu. Reinicie para tentar novamente.");
        disableAllButtons();
        reiniciarBtn.setVisible(true);
    }

    private void reiniciarJogo() {
        saude = 50; honra = 50; dinheiro = 50;
        estado = 0;
        reiniciarBtn.setVisible(false);
        resultadoLabel.setText("");
        atualizarBarras();
        atualizarTela();
        enableAllButtons();
    }

    private boolean isGameOver() { return saude <= 0 || honra <= 0 || dinheiro <= 0; }
    private boolean isFinal() { return saude >= 100 || honra >= 100 || dinheiro >= 100; }
    private boolean isGameOverOrFinal() { return isGameOver() || isFinal(); }

    private void disableAllButtons() { opcao1.setDisable(true); opcao2.setDisable(true); opcao3.setDisable(true); }
    private void enableAllButtons() { opcao1.setDisable(false); opcao2.setDisable(false); opcao3.setDisable(false); }

    private double clamp(double v, double min, double max) { return Math.max(min, Math.min(max, v)); }

    private String toRgbString(Color c) {
        return String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
