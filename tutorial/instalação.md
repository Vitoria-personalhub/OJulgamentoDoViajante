# ☕ Guia Completo para Configurar JavaFX no IntelliJ IDEA (2025)
Este guia explica **como instalar, configurar e rodar um projeto JavaFX** no IntelliJ IDEA, passo a passo, usando o **JDK 25** e o **JavaFX 25**.
Exemplo baseado no jogo *"Julgamento do Viajante"*.

## 🧩 1. Instalar o Java JDK
### 🔹 Passo 1: Baixar o JDK
Baixe o **Oracle OpenJDK 25** no site oficial:
👉 [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
Escolha a versão:
**Windows x64 Installer**

### 🔹 Passo 2: Instalar e testar
Após instalar, abra o **Prompt de Comando** e verifique:
```bash
java -version
```
Deve aparecer algo como:
```
java version "25.0.1"  OpenJDK Runtime Environment
```

## 🎭 2. Baixar o JavaFX SDK
### 🔹 Passo 1: Download
Acesse o site do JavaFX:
👉 [https://openjfx.io/](https://openjfx.io/)
Baixe o arquivo:
```
openjfx-25.0.1_windows-x64_bin-sdk.zip
```

### 🔹 Passo 2: Extração
Extraia em um local de fácil acesso, por exemplo:
```
C:\Users\SeuNome\Documents\JavaFX\javafx-sdk-25.0.1
```

## 💡 3. Instalar o IntelliJ IDEA
Baixe a versão Community (gratuita) em:
👉 [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
Após baixar:
- Descompacte o arquivo `.zip`
- Acesse a pasta `bin`
- O aplicativo executável é o `idea64.exe`

## 🚀 4. Criar o Projeto JavaFX
Abra o IntelliJ → **File > New > Project**
Escolha **Java** (ou **JavaFX**, se aparecer)
Configure:
- Project SDK: selecione o **JDK 25**
- Project Name: `JulgamentoDoViajante`
- Location: escolha a pasta de destino
Clique em **Create**

## 🧱 5. Adicionar o JavaFX ao Projeto
Vá até:
```
File > Project Structure > Modules > Dependencies
```
Clique no botão **+ → JARs or Directories**
Selecione:
```
C:\Users\SeuNome\Documents\JavaFX\javafx-sdk-25.0.1\lib
```
Clique em **OK → Compile → Apply → OK**

## ⚙️ 6. Configurar a Execução (Run Configuration)
No topo do IntelliJ:
```
Run → Edit Configurations...
```
No campo **VM Options**, adicione (ajuste o caminho conforme seu PC):
```
--module-path "C:\Users\SeuNome\Documents\JavaFX\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```
⚠️ Dica: mantenha as aspas se houver espaços no caminho.
Clique em **Apply → OK**

## 🖼️ 7. Adicionar Imagens e Código
Estrutura do projeto:
```
JulgamentoDoViajante/
│
├── src/
│   ├── MainFX.java
│
└── imagens/
    └── fundo.jpg
```
(a imagem de fundo deve estar na pasta `imagens/`)

## 🧠 8. Rodar o Projeto
Abra `MainFX.java`
Clique no botão ▶️ ao lado do método `main`
O jogo deve iniciar mostrando:
- Fundo com imagem personalizada
- Caixas e barras de atributos coloridas
- Missões automáticas e interativas

## 🧰 9. Solução de Problemas
| Erro | Solução |
|------|----------|
| **Error: Could not find or load main class** | Verifique o caminho em `--module-path` e as aspas |
| **Warning: Error loading image** | A imagem deve estar dentro da pasta `imagens/` |
| **Tela branca / sem barras** | Verifique se o CSS (cores) não foi sobrescrito |
| **JavaFX not found** | Confira se os JARs da pasta `lib` foram adicionados em Dependencies |

## 🧩 10. Teste rápido de funcionamento
Antes de rodar o jogo completo, teste o JavaFX com este código simples:
```java
import javafx.application.Application;
import javafx.stage.Stage;

public class TesteFX extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Teste JavaFX");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
```
Se a janela aparecer, o JavaFX está configurado corretamente! ✅

## 🎮 Conclusão
Após seguir todos os passos:
✅ Java e JavaFX instalados
✅ IntelliJ configurado
✅ Projeto funcional com imagem e atributos

Agora é só rodar, jogar e evoluir o **Julgamento do Viajante**! ⚔️🔥

**Autor:** Milene Vitória
**Projeto:** Julgamento do Viajante
**Tecnologias:** Java 25 • JavaFX 25 • IntelliJ IDEA Community
