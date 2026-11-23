# Guia de consulta --- Testes Unitários em Java (JUnit 5)

> Material de apoio para prova prática com consulta. Use os modelos como
> ponto de partida e adapte nomes, construtores, métodos, regras e
> exceções ao enunciado.

## 1. O que é um teste unitário?

Um teste unitário executa uma parte pequena do sistema (normalmente um
método) e verifica se o resultado ou comportamento corresponde ao
esperado.

**Importante:** o teste não faz o cálculo no lugar da classe da
aplicação. Ele chama o método da aplicação e confere o resultado.

Exemplo: a classe calcula o total de uma compra; o teste chama
`calcularTotal()` e compara o retorno com o valor esperado.

## 2. Como transformar o enunciado em testes

1.  Liste as classes de domínio e identifique quais têm comportamento
    próprio.
2.  Identifique os atributos, construtores e métodos de cada classe.
3.  Transforme cada regra de negócio em um cenário de teste.
4.  Para cada regra, pense em:
    -   cenário válido: o que deve acontecer?
    -   cenário inválido: qual erro ou exceção deve acontecer?
    -   mudança de estado: qual status/valor deve ficar depois da
        operação?
5.  Faça manualmente as contas de descontos, taxas, frete e valores
    finais.
6.  Crie as classes de teste exigidas pelo professor e teste todos os
    tipos concretos.

## 3. Estrutura básica de uma classe de teste

``` java
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ProdutoTest {

    private Produto produto;

    @BeforeEach
    public void configurar() {
        // Cria um objeto novo antes de cada teste.
        produto = new Produto("Caderno", 20.0);
    }

    @AfterEach
    public void finalizar() {
        // Executa depois de cada teste.
        System.out.println("Teste finalizado!");
    }

    @Test
    public void testarPreco() {
        assertEquals(20.0, produto.getPreco(), 0.01);
    }
}
```

Troque `Produto`, os argumentos do construtor, getters e métodos pelos
nomes reais do projeto.

## 4. Anotações do JUnit 5

  -----------------------------------------------------------------------
  Anotação                            Para que serve
  ----------------------------------- -----------------------------------
  `@Test`                             Marca um método como teste.

  `@BeforeEach`                       Executa antes de cada teste. Use
                                      para criar/preparar objetos e
                                      dados.

  `@AfterEach`                        Executa depois de cada teste. Use
                                      para informar a finalização ou
                                      limpar recursos.

  `@BeforeAll`                        Executa uma vez antes de todos os
                                      testes da classe. Normalmente o
                                      método é `static`.

  `@AfterAll`                         Executa uma vez depois de todos os
                                      testes da classe. Normalmente o
                                      método é `static`.
  -----------------------------------------------------------------------

**Atenção:** para instanciar e inicializar objetos antes de cada teste,
use `@BeforeEach`, não `@AfterEach`.

## 5. Asserções mais usadas

  ---------------------------------------------------------------------------------------------------------------------------------------
  Método                                   O que verifica          Exemplo
  ---------------------------------------- ----------------------- ----------------------------------------------------------------------
  `assertEquals(esperado, atual)`          Se dois valores são     `assertEquals(10, obj.calcular());`
                                           iguais.                 

  `assertEquals(esperado, atual, delta)`   Igualdade aproximada    `assertEquals(10.5, obj.total(), 0.01);`
                                           para `double`.          

  `assertTrue(condicao)`                   Se a condição é         `assertTrue(obj.estaAtivo());`
                                           verdadeira.             

  `assertFalse(condicao)`                  Se a condição é falsa.  `assertFalse(obj.temEstoque());`

  `assertNull(valor)`                      Se o valor é `null`.    `assertNull(obj.getResponsavel());`

  `assertNotNull(valor)`                   Se o valor não é        `assertNotNull(obj.getId());`
                                           `null`.                 

  `assertThrows(Tipo.class, lambda)`       Se o trecho lança a     `assertThrows(IllegalArgumentException.class, () -> obj.setId(-1));`
                                           exceção esperada.       
  ---------------------------------------------------------------------------------------------------------------------------------------

### Entendendo `assertEquals`

``` java
assertEquals(105.0, pedido.calcularValorFinal(), 0.01);
```

-   `105.0`: valor esperado, calculado com base na regra do enunciado.
-   `pedido.calcularValorFinal()`: valor real retornado pelo programa.
-   `0.01`: tolerância para pequenas diferenças decimais. Se a diferença
    for de até `0.01`, a comparação passa.

Para enum, texto e inteiros, normalmente não é necessário informar
tolerância:

``` java
assertEquals(StatusPedido.PROCESSANDO, pedido.getStatus());
```

### Entendendo `assertThrows`

``` java
assertThrows(ValorInvalidoException.class, () -> {
    new Pedido("A-10", -50);
});
```

-   `ValorInvalidoException.class`: tipo de exceção esperado.
-   `() -> { ... }`: expressão lambda; contém o trecho que o JUnit deve
    executar.
-   O teste passa se a exceção esperada acontecer. Falha se nenhuma
    exceção acontecer ou se for lançada outra exceção.

## 6. Modelos para testar regras de negócio

### A) Testar cálculo

``` java
@Test
public void testarCalculoTotal() {
    // Exemplo: preço 100 + taxa 5 = 105.
    assertEquals(105.0, pedido.calcularTotal(), 0.01);
}
```

Faça a conta manualmente. Se houver desconto, taxa ou frete, confira
cada parcela e depois o total.

### B) Testar exceção personalizada

``` java
@Test
public void testarValorNegativo() {
    assertThrows(ValorInvalidoException.class, () -> {
        new Produto("Caneta", -2.0);
    });
}
```

Use a exceção que o construtor ou método realmente lança. Confira o nome
exato e se ela é checked (pode exigir `throws` ou `try/catch`).

### C) Testar exceção nativa do Java

``` java
@Test
public void testarParametroNulo() {
    assertThrows(NullPointerException.class, () -> {
        objeto.alterarStatus(null);
    });
}
```

Só escreva esse teste se o código realmente rejeitar `null` e lançar
essa exceção. Não invente uma regra: confira a implementação.

### D) Testar mudança de estado

``` java
@Test
public void testarProcessamento() throws PedidoInvalidoException {
    pedido.processar();
    assertEquals(Status.PROCESSANDO, pedido.getStatus());
}
```

Chame a operação e depois confira o estado pelo getter.

### E) Testar uma alteração proibida

``` java
@Test
public void testarAlteracaoBloqueada() throws PedidoInvalidoException {
    pedido.alterarStatus(Status.ENTREGUE);

    assertThrows(PedidoInvalidoException.class, () -> {
        pedido.alterarStatus(Status.CANCELADO);
    });
}
```

Primeiro prepare o objeto na situação proibida (por exemplo, status
`ENTREGUE`); depois tente a alteração que deve falhar.

## 7. Como testar herança, classe abstrata e interface

-   **Classe abstrata:** não pode ser instanciada diretamente. Crie um
    objeto de uma classe concreta filha e, se útil, armazene-o numa
    variável do tipo abstrato.

    ``` java
    Pedido pedido = new PedidoRoupa(...);
    ```

-   **Herança:** teste métodos herdados usando uma subclasse concreta.
    Teste também comportamentos específicos de cada subclasse.

-   **Polimorfismo:** teste cada tipo concreto quando cada um implementa
    a regra de forma diferente.

-   **Interface:** normalmente não se cria um teste separado para a
    interface. Testam-se as classes que implementam seus métodos.

-   **Enum:** teste os comportamentos que dependem dos valores do enum,
    como transições de status.

## 8. Modelo completo adaptável

``` java
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class NomeDaClasseTest {

    private NomeDaClasse objeto;

    @BeforeEach
    public void configurar() throws Exception {
        // Crie um objeto válido para cada teste.
        objeto = new NomeDaClasse(/* argumentos */);
    }

    @AfterEach
    public void finalizar() {
        System.out.println("Teste finalizado!");
    }

    @Test
    public void testarResultado() {
        assertEquals(/* esperado */, objeto.metodo(), 0.01);
    }

    @Test
    public void testarRegraDeEstado() throws Exception {
        objeto.executarAcao();
        assertEquals(/* estado esperado */, objeto.getEstado());
    }

    @Test
    public void testarExcecao() {
        assertThrows(ExcecaoEsperada.class, () -> {
            objeto.operacaoInvalida();
        });
    }
}
```

Este é um esqueleto. Substitua os nomes e remova/adapte os testes que
não fizerem sentido para o enunciado.

## 9. Passo a passo durante a prova

1.  Leia o enunciado inteiro.
2.  Destaque substantivos (possíveis classes), características
    (atributos) e ações (métodos).
3.  Identifique regras válidas e inválidas.
4.  Confira a implementação das classes: nomes, construtores, getters e
    exceções.
5.  Crie a classe de teste com imports, atributo e `@BeforeEach`.
6.  Escreva um teste para cada comportamento exigido: cálculo, estado,
    exceção e restrições.
7.  Compile primeiro. Corrija erros de digitação, imports e classpath
    antes de executar o JUnit.
8.  Execute os testes e leia a primeira falha com atenção: compare
    esperado e atual e confira a regra.
9.  Execute todos os testes juntos no final.

## 10. Erros comuns e como resolver

  ------------------------------------------------------------------------------------
  Erro                                             O que conferir
  ------------------------------------------------ -----------------------------------
  `cannot find symbol`                             Nome da classe/método, maiúsculas,
                                                   digitação, imports e se a classe
                                                   foi compilada.

  `package org.junit.jupiter.api does not exist`   JAR do JUnit não está no classpath
                                                   ou não foi adicionado corretamente.

  `ClassNotFoundException` ao executar             A compilação falhou ou a classe não
                                                   está no diretório de saída. Compile
                                                   antes de executar.

  `Expected ... but was ...`                       O resultado real é diferente do
                                                   esperado. Confira a regra e a
                                                   conta; não altere o esperado só
                                                   para passar.

  `Expected exception ...`                         A exceção não aconteceu. Confira se
                                                   o cenário realmente deveria
                                                   lançá-la.

  `Unexpected exception`                           O código lançou outra exceção. Leia
                                                   a mensagem e veja onde ocorreu.
  ------------------------------------------------------------------------------------

## 11. Comandos PowerShell --- projeto sem Maven

Os comandos abaixo seguem o projeto de estudo, com pasta
`src/main/java`, `src/test/java`, pasta `out` e JAR em `lib`.

Compile as classes principais:

``` powershell
$fontes = Get-ChildItem .\src\main\java -Filter *.java
javac -d out $fontes.FullName
```

Compile todas as classes de teste:

``` powershell
$testes = Get-ChildItem .\src\test\java -Filter *.java
javac -cp "out;lib\junit-platform-console-standalone-1.11.4.jar" -d out $testes.FullName
```

Execute todos os testes:

``` powershell
java -jar lib\junit-platform-console-standalone-1.11.4.jar execute --class-path out --scan-class-path
```

Se a prova usar Maven ou outra IDE, siga a configuração indicada pelo
professor. O classpath acima é específico do projeto sem Maven e dos
caminhos usados no projeto de estudo.

## 12. Checklist antes de entregar

-   [ ] Existe uma classe de teste para cada classe de domínio exigida?
-   [ ] Todos os tipos concretos foram testados?
-   [ ] Os cálculos foram conferidos manualmente?
-   [ ] As exceções personalizadas foram testadas?
-   [ ] As exceções nativas previstas foram testadas?
-   [ ] O processamento altera o estado como esperado?
-   [ ] As regras que proíbem mudanças de estado foram testadas?
-   [ ] `@BeforeEach` prepara um objeto novo antes de cada teste?
-   [ ] `@AfterEach` executa depois de cada teste?
-   [ ] Todos os testes passam individualmente e em conjunto?
-   [ ] Os testes estão fora da classe `Main`?

## 13. Lembretes essenciais

-   **Teste não é implementação:** o teste chama o método da aplicação;
    não repete a lógica de cálculo dentro do `assert`.
-   **Não force um teste a passar:** se o resultado divergir, investigue
    a regra, o código e os dados usados.
-   **Adapte nomes e regras:** não copie cegamente as regras de pedidos
    para outro tema. Preserve o padrão e substitua pelo que o enunciado
    realmente pede.
