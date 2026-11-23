# CONSULTA RÁPIDA --- TESTES UNITÁRIOS JAVA + JUNIT 5

> **Use este arquivo durante a prova.** Não leia tudo de uma vez.
> Encontre o que o enunciado pede na tabela abaixo, vá até a seção
> indicada e adapte o exemplo.

## 1. Ache rápido o que você precisa

  -----------------------------------------------------------------------
  Se o enunciado pedir...             Vá para...
  ----------------------------------- -----------------------------------
  Conferir um cálculo ou retorno      **2. Testar resultado**

  Conferir se uma exceção acontece    **3. Testar exceção**

  Verificar alteração de              **4. Testar estado**
  status/atributo                     

  Preparar objeto para cada teste     **5. Preparação**

  Testar regra de limite (maior que,  **6. Testar limites**
  menor que)                          

  Testar classe filha, abstrata,      **7. Conceitos do projeto**
  interface ou enum                   

  Criar uma classe de teste do zero   **8. Modelo pronto**

  Compilar ou executar no PowerShell  **9. Comandos**

  Resolver falha de teste             **10. Erros comuns**
  -----------------------------------------------------------------------

## 2. Testar resultado de um método

**Quando usar:** o método retorna um valor, como preço final, média,
total, frete ou quantidade.

``` java
@Test
void deveCalcularTotal() {
    // Preparar
    Produto produto = new Produto(100.0);

    // Executar
    double resultado = produto.calcularTotal();

    // Conferir: esperado, obtido, tolerância
    assertEquals(110.0, resultado, 0.001);
}
```

`assertEquals(esperado, obtido)` compara o resultado esperado com o que
o método retornou.

-   Para `int`, `String`, enum etc.: `assertEquals(esperado, obtido)`.
-   Para `double` ou `float`: use também o delta, por exemplo `0.001`.
-   O valor esperado deve vir da regra do enunciado. Não copie o cálculo
    do próprio método para montar a expectativa.

**Adapte:** troque `Produto`, os argumentos do construtor,
`calcularTotal()` e `110.0`.

## 3. Testar se uma exceção é lançada

**Quando usar:** o enunciado diz "deve lançar", "não permitir",
"rejeitar", "valor inválido" ou "não aceitar".

``` java
@Test
void naoDeveAceitarValorNegativo() {
    assertThrows(ValorInvalidoException.class, () -> {
        new Produto(-10.0);
    });
}
```

-   `ValorInvalidoException.class`: tipo de exceção esperado.
-   `() -> { ... }`: ação que o JUnit executa.
-   Coloque dentro das chaves exatamente a operação que deve lançar a
    exceção.

Para exceção padrão do Java, use o tipo correto:

``` java
assertThrows(NullPointerException.class, () -> {
    produto.definirCategoria(null);
});
```

**Atenção:** use o nome da exceção que o código realmente lança.
`assertThrows` verifica a exceção; `throws` na assinatura apenas declara
que o método pode propagá-la.

## 4. Testar mudança de estado

**Quando usar:** o método altera status, situação, disponibilidade,
saldo ou outro atributo.

``` java
@Test
void deveAlterarStatus() {
    Pedido pedido = new Pedido(/* argumentos */);

    pedido.processarPedido();

    assertEquals(StatusPedido.PROCESSANDO, pedido.getStatus());
}
```

**Regra que proíbe uma alteração:**

``` java
@Test
void naoDeveAlterarPedidoEntregue() {
    pedido.alterarStatus(StatusPedido.ENTREGUE);

    assertThrows(PedidoInvalidoException.class, () -> {
        pedido.alterarStatus(StatusPedido.PROCESSANDO);
    });

    // Confere que o status não mudou
    assertEquals(StatusPedido.ENTREGUE, pedido.getStatus());
}
```

Adapte os métodos, status e exceção aos nomes do seu projeto.

## 5. Preparar objetos antes de cada teste

Use `@BeforeEach` quando vários testes precisarem de um objeto
inicializado.

``` java
class ProdutoTest {
    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto(100.0);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Teste finalizado");
    }
}
```

  Anotação        Quando executa
  --------------- ----------------------
  `@Test`         identifica um teste
  `@BeforeEach`   antes de cada teste
  `@AfterEach`    depois de cada teste

**Por que objeto novo?** Um teste pode modificar o objeto. Preparar
outro antes do próximo evita que um teste dependa do resultado de outro.

Se o teste só usa um objeto uma vez, pode criá-lo diretamente dentro do
próprio método de teste.

## 6. Testar limites de uma regra

**Quando usar:** o enunciado usa expressões como "maior que 100", "a
partir de 18", "menor ou igual a 50".

Se a regra for **maior que 100** (`> 100`), teste:

    Entrada Resultado esperado
  --------- ---------------------
      99,99 condição não atende
     100,00 condição não atende
     100,01 condição atende

Tradução das palavras:

  Enunciado          Operador
  ------------------ ----------
  maior que          `>`
  maior ou igual a   `>=`
  menor que          `<`
  menor ou igual a   `<=`
  igual a            `==`
  diferente de       `!=`

Não confunda "maior que 100" com "maior ou igual a 100": no segundo
caso, 100 também atende.

## 7. Conceitos do projeto: o que muda nos testes?

-   **Herança:** teste as regras comuns na classe base e as regras
    próprias em cada classe filha.
-   **Classe abstrata:** não pode ser instanciada diretamente. Use uma
    classe concreta que herde dela.
-   **Interface:** teste o comportamento usando uma classe concreta que
    implementa a interface.
-   **Enum:** compare com o valor do enum, por exemplo
    `assertEquals(Status.ATIVO, objeto.getStatus())`.
-   **Exceção personalizada:** use o nome da classe de exceção definida
    no projeto.

Exemplo de classe abstrata `Pedido`:

``` java
Pedido pedido = new PedidoEletronico(/* argumentos */);
```

Não tente fazer `new Pedido(...)` se `Pedido` for abstrata.

## 8. Modelo pronto: classe de teste

Copie e adapte este esqueleto. Apague os métodos de teste que não
fizerem sentido para o enunciado.

``` java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinhaClasseTest {
    private MinhaClasse objeto;

    @BeforeEach
    void setUp() {
        objeto = new MinhaClasse(/* dados válidos */);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Teste finalizado");
    }

    @Test
    void deveRetornarResultadoEsperado() {
        var resultado = objeto.metodo();

        assertEquals(/* esperado */, resultado);
    }

    @Test
    void deveLancarExcecaoParaDadoInvalido() {
        assertThrows(MinhaExcecao.class, () -> {
            objeto.metodo(/* dado inválido */);
        });
    }
}
```

**Antes de executar:** substitua todos os nomes de exemplo pelos nomes
que existem no código. Se o retorno for `double`, acrescente o delta no
`assertEquals`.

## 9. Comandos para este projeto (PowerShell, sem Maven)

Estes comandos consideram a estrutura usada no projeto de estudo:

-   produção: `src/main/java`
-   testes: `src/test/java`
-   JUnit: `lib/junit-platform-console-standalone-1.11.4.jar`
-   saída: `out`

**1. Compilar as classes principais:**

``` powershell
$arquivos = Get-ChildItem .\src\main\java -Filter *.java
javac -cp "lib\junit-platform-console-standalone-1.11.4.jar" -d out $arquivos.FullName
```

**2. Compilar os testes:**

``` powershell
$testes = Get-ChildItem .\src\test\java -Filter *.java
javac -cp "out;lib\junit-platform-console-standalone-1.11.4.jar" -d out $testes.FullName
```

**3. Executar todos os testes:**

``` powershell
java -jar lib\junit-platform-console-standalone-1.11.4.jar execute --class-path out --scan-class-path
```

Se a prova tiver outra estrutura de pastas, outro nome de JAR ou usar
Maven, os comandos precisam ser ajustados. Não use estes caminhos sem
conferir os arquivos do projeto da prova.

## 10. Erros comuns: confira primeiro aqui

  ------------------------------------------------------------------------------------
  Mensagem/situação                                O que verificar
  ------------------------------------------------ -----------------------------------
  `cannot find symbol`                             nome de classe, método ou variável;
                                                   se a classe foi compilada

  `package org.junit.jupiter.api does not exist`   JUnit não está no classpath

  `No tests found`                                 `@Test`, nome/pacote da classe e
                                                   comando de execução

  `expected ... but was ...`                       valor esperado, regra e
                                                   implementação

  `NullPointerException` inesperada                objeto ou atributo não inicializado

  teste passa sozinho e falha junto                estado compartilhado; prepare
                                                   objeto em `@BeforeEach`

  `assertEquals` falha com decimais                use delta e confira arredondamento

  exceção esperada não acontece                    confira se chamou o método correto
                                                   e se a regra está implementada

  exceção diferente                                confira o tipo realmente lançado
  ------------------------------------------------------------------------------------

Um teste que falha pode indicar erro no programa, e não no teste.
Compare com o enunciado antes de mudar a expectativa.

## 11. Checklist de entrega

-   [ ] Criei as classes de teste solicitadas.
-   [ ] Testei cada regra de negócio importante.
-   [ ] Testei os cálculos com valores esperados corretos.
-   [ ] Testei limites quando há condições (`>`, `>=`, `<`, `<=`).
-   [ ] Testei entradas inválidas e exceções.
-   [ ] Testei mudanças de estado e transições proibidas.
-   [ ] Usei `@BeforeEach` quando precisei reinicializar objetos.
-   [ ] Executei todos os testes e conferi o resultado.

------------------------------------------------------------------------

## Regra de ouro

**Enunciado → regra → método → dados de teste → chamada → assert.**

1.  Encontre uma regra no enunciado.
2.  Localize no código o método responsável.
3.  Prepare dados que representem o cenário.
4.  Chame o método.
5.  Verifique o retorno, o estado ou a exceção.

Não coloque a lógica de negócio dentro do teste ou do `main`: o teste
verifica o comportamento das classes do sistema.
