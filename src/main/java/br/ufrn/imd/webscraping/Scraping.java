package br.ufrn.imd.webscraping;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraping {
	    
	public static void main(String [] args) {
		
		System.out.println("Iniciando scraping. Aguarde...\n");
		
		try {
			String url = "https://www.vivareal.com.br/venda/rio-grande-do-norte/natal/apartamento_residencial/?__vt=rpca:a";
			String urlFiltro = "#onde=BR-Rio_Grande_do_Norte-NULL-Natal&tipos=apartamento_residencial";
			String arquivo = "resultado-scraping.csv";
			
			// buscar o documento por HTTP
			Document doc = Jsoup.connect(url + urlFiltro).get();
			
			int paginaAtual = 1;
			
			Elements anuncios = doc.getElementsByClass("property-card__container");
			Elements proximaPagina = doc.select("li.pagination__item a[title='Próxima página']");
			
			FileWriter writer = new FileWriter(arquivo);
			writer.append("Título");
			writer.append(";");
			writer.append("Endereço");
			writer.append(";");
			writer.append("Taxa");
			writer.append(";");
			writer.append("Área");
			writer.append(";");
			writer.append("Quartos");
			writer.append(";");
			writer.append("Banheiros");
			writer.append(";");
			writer.append("Suítes");
			writer.append(";");
			writer.append("Vagas");
			writer.append(";");
			writer.append("Preço");
			writer.append("\n");
			
			for (int i = 0; i < anuncios.size(); i++) {
				Element anuncio = anuncios.get(i);
				
				String titulo = anuncio.getElementsByClass("property-card__title").text();
				String endereco = anuncio.getElementsByClass("property-card__address").text();
				String taxa = anuncio.select("div.property-card__price-details--condo strong.js-condo-price").text();
				String area = anuncio.select("li.property-card__detail-area span.property-card__detail-area").text();
				String quartos = anuncio.select("li.property-card__detail-room span.property-card__detail-value").text();
				String banheiros = anuncio.select("li.property-card__detail-bathroom span.property-card__detail-value").text();
				String suites = anuncio.select("li.property-card__detail-bathroom span.property-card__detail-value").text();
				String vagas = anuncio.select("li.property-card__detail-garage span.property-card__detail-value").text();
				String preco = anuncio.getElementsByClass("property-card__price").text();
				
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
				
				if(i % 35 == 0 && i != 0){
					writer.flush();
					
					System.out.println("Concluída a paginação: " + paginaAtual);
					
					// Quando o link próxima página estiver desabilitado, é encerrado o for. 
					if(proximaPagina.hasAttr("data-disabled")) {
						break;
					}
					
					String urlPagina = url + "&pagina=" + ++paginaAtual + urlFiltro;
					doc = Jsoup.connect(urlPagina).get();
					anuncios = doc.getElementsByClass("property-card__container");
					i = 0;
				}
			}

	        writer.close();
	        
	        System.out.println("\nProcesso concluído. Arquivo gerado.");
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
