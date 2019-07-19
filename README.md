Atividade 02 (Daniel Olinto)
===

## Antes de começar

Na atividade-01 eu não tinha conseguido fazer com que o link de paginação funcionasse. Consequentemente o arquivo .CSV continham apenas 36 anúncios.

Nessa atividade-02, eu consegui implementar tal funcionalidade. Portanto, optei por utilizar o .CSV que gerei utilizando o JSOUP.

## Como foi feito

Foi realizada a atividade utilizando o .CSV que criei na atividade-01.

1. Realizado o webscraping do site Viva Real filtrando por "Natal - RN" e "Apartamento";
    * https://www.vivareal.com.br/venda/rio-grande-do-norte/natal/apartamento_residencial/?__vt=rpca:a 
2. Utilizado o JSOUP;
3. Os arquivos .JAVA, .KTR, .MD seguem em anexo;
4. Utilizei o ";" como delimitador dos campos.

## Realizando o webscraping

1. Foi gerado com .CSV com 8377 anúncios;
2. Realizei alguns filtros no .JAVA, seguem:
    * Mesmo filtrando apenas por "Natal - RN" e "Apartamento", o site trazia anúncios de outras cidades e estados. Portanto filtrei no código para trazer apenas com "Natal - RN" de fato;
    * Tinham diversos casos que não havia o logradouro no site, trazendo apenas bairro e cidade no campo de endereço. Portanto, criei essa condição no código para facilitar o trabalho no Pentaho;
    * Houve um único caso que estava cadastrado "Rua;" que estava causando crash no Pentaho. Portanto, criei a condição para eliminar o ";".

---

```java=
Feature: Condições utilizadas

// Só escrever no arquivo os anúncios que terminarem "Natal - RN".
// Mesmo filtrando por "Natal - RN" e "Apartamento" no site, estava trazendo anúncios de outros estados e cidades. 
if(endereco.endsWith("Natal - RN")) {
    writer.append(titulo);
    writer.append(";");

    // Se endereço não começar com ..., inserir " | ".
    // Inseri a barra vertical para auxiliar na divisão de logradouro, bairro e cidade no Pentaho.
    if(!endereco.startsWith("Rua") && !endereco.startsWith("Av") && !endereco.startsWith("R.") 
            && !endereco.startsWith("Alameda") && !endereco.startsWith("Travessa") && !endereco.startsWith("Vila")
            && !endereco.startsWith("Largo") && !endereco.startsWith("Praça") && !endereco.startsWith("Senador")
            && !endereco.startsWith("Passagem") && !endereco.startsWith("1ª Travessa")) {
        writer.append(" | ");
        writer.append(endereco);

    // Foi necessário a condição porque havia um anúncio com ";", delimitador utilizado para separar os campos.
    } else if (endereco.startsWith("Rua;")) {
        writer.append(endereco.replace("Rua;", "Rua"));
    } else {
        writer.append(endereco);
    }
    writer.append(";");
    writer.append(taxa);
    writer.append(";");
    writer.append(area);
    writer.append(";");
    writer.append(quartos);
    writer.append(";");
    writer.append(banheiros);
    writer.append(";");
    writer.append(suites);
    writer.append(";");
    writer.append(vagas);
    writer.append(";");
    writer.append(preco);
    writer.append("\n");
}
```
> Condições utilizadas para facilitar o trabalho no Pentaho [name=Daniel Olinto]

## Utilizando o Pentaho

1. CSV file input;
2. Replace in string;
    * Tratado os dados em endereço;
3. Split fields;
    * Dividido o endereço em logradouro, bairro e cidade;
4. Replace in string;
    * Tratado os dados em preço, taxa e acomodações (banheiro, quarto, etc.);
5. Value mapper
    * Criado o campo zona;
6. Split field
    * Tratado os dados em área.
7. Select values
    * Convertido os campos para tipo number;
8. Calculator
    * Feito o cálculo APO;
9. Calculator
    * Feito o cálculo preço/área;
10. Text file output;
