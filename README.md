
# API Transação

Projeto para aprendizado de desenvolvimento, baseado no **Itaú Unibanco - Desafio de Programação**
(https://github.com/rafaellins-itau/desafio-itau-vaga-99-junior)



O desenvolvimento refere-se a:

- Criação de API REST, utilizando linguagem Java com Spring Boot
- Com endpoints para:
  - Recebimento de transações financeiras
  - Limpeza de transações existentes
  - Cálculo de estatísticas em determinado intervalo de tempo


## Requisitos

Java Development Kit (JDK): Certifique-se de ter o JDK 23 instalado.

Lombok: Certifique-se de que o Lombok está configurado no seu ambiente.

IDE: Recomenda-se o uso de uma IDE como Eclipse para facilitar o desenvolvimento.

## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/felmanc/Itau-Desafio-JavaJunior
```


## Funcionalidades



### 2.2. Endpoints da API

A seguir serão especificados os endpoints presentes na API.

#### 2.2.1. Receber Transações: `POST /transacao`

Este é o endpoint que recebe as Transações. Cada transação consiste de um `valor` e uma `dataHora` de quando ela aconteceu:

```json
{
    "valor": 123.45,
    "dataHora": "2020-08-07T12:34:56.789-03:00"
}
```

Os campos no JSON acima significam o seguinte:

| Campo      | Significado                                                   | Obrigatório? |
|------------|---------------------------------------------------------------|--------------|
| `valor`    | **Valor em decimal com ponto flutuante** da transação         | Sim          |
| `dataHora` | **Data/Hora no padrão ISO 8601** em que a transação aconteceu | Sim          |

**OBS:** Utilizado atributo do tipo `OffsetDateTime` para lidar com Data/Hora no padrão ISO 8601

A API só aceita transações que:

1. Tenham os campos `valor` e `dataHora` preenchidos
2. A transação **NÃO DEVE** acontecer no futuro
3. A transação **DEVE** ter acontecido a qualquer momento no passado
4. A transação **NÃO DEVE** ter valor negativo
5. A transação **DEVE** ter valor igual ou maior que `0` (zero)

Como resposta, este endpoint responde com:

- `201 Created` sem nenhum corpo
  - A transação foi aceita (ou seja foi validada, está válida e foi registrada)
- `422 Unprocessable Entity`
  - A transação **não** foi aceita por qualquer motivo (1 ou mais dos critérios de aceite não foram atendidos - por exemplo: uma transação com valor menor que `0`)
- `400 Bad Request`
  - A API não compreendeu a requisição do cliente (por exemplo: um JSON inválido)

#### 2.2.2. Limpar Transações: `DELETE /transacao`

Este endpoint simplesmente **apaga todos os dados de transações** que estejam armazenados.

Como resposta, este endpoint responde com:

- `200 OK` sem nenhum corpo
  - Todas as informações foram apagadas com sucesso

#### 2.2.3. Calcular Estatísticas: `GET /estatistica`

Este endpoint retorna estatísticas das transações com dois comportamentos possíveis:

1. **Comportamento padrão (sem parâmetro):**  
   Retorna as estatísticas das transações que ocorreram nos últimos **60 segundos (1 minuto)**.
   
2. **Comportamento parametrizado (com parâmetro `intervalo`):**  
   Considera o valor passado no parâmetro `intervalo` (em segundos) e retorna as estatísticas das transações nesse intervalo de tempo.


As estatísticas calculadas são:

```json
{
    "count": 10,
    "sum": 1234.56,
    "avg": 123.456,
    "min": 12.34,
    "max": 123.56
}
```

Os campos no JSON acima significam o seguinte:

|  Campo  | Significado                                                   | Obrigatório? |
|---------|---------------------------------------------------------------|--------------|
| `count` | **Quantidade de transações** nos últimos 60 segundos          | Sim          |
| `sum`   | **Soma total do valor** transacionado nos últimos 60 segundos | Sim          |
| `avg`   | **Média do valor** transacionado nos últimos 60 segundos      | Sim          |
| `min`   | **Menor valor** transacionado nos últimos 60 segundos         | Sim          |
| `max`   | **Maior valor** transacionado nos últimos 60 segundos         | Sim          |

>**OBS:** Utilizado objeto do Java 8+ chamado `DoubleSummaryStatistics` para obter as estatísticas.

Como resposta, este endpoint responde com:

- `200 OK` com os dados das estatísticas
  - Um JSON com os campos `count`, `sum`, `avg`, `min` e `max` todos preenchidos com seus respectivos valores
  - **Atenção!** Quando não houverem transações nos últimos 60 segundos são considerados todos os valores como `0` (zero)

## Extras desenvolvidos:

1. **Testes automatizados**: Implementação de testes unitários e de integração.
2. **Logs**: Adicionado logs detalhados e configuráveis.
3. **Observabilidade**: Criado endpoint de health check.
5. **Performance**: Estimado o tempo para cálculo das estatísticas em implementação de teste unitário do Service.
6. **Tratamento de Erros**: Customizados erros para informar as causas de falhas.
7. **Documentação da API**: Disponibilizado Swagger(https://swagger.io/) para documentar os endpoints.
8. **Documentação do Sistema**: Criada esta documentação.
9. **Configurações Dinâmicas**: Configurável o período de cálculo das estatísticas através de parâmetro na chamada do endpoint /transacao.
