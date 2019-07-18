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
			String url = "https://www.vivareal.com.br/venda/rio-grande-do-norte/natal/apartamento_residencial/";
			String arquivo = "resultado-scraping.csv";
			
			// buscar o documento por HTTP
			Document doc = Jsoup.connect(url).get();
			
			Elements anuncios = doc.getElementsByClass("property-card__container");
			Elements proximaPagina = doc.select("li.pagination__item a[title='Próxima página']");
			
			FileWriter writer = new FileWriter(arquivo);
			writer.append("Título");
			writer.append(";");
			writer.append("Endereço");
			writer.append(";");
			writer.append("Taxa condomínio");
			writer.append(";");
			writer.append("Área");
			writer.append(";");
			writer.append("Quartos");
			writer.append(";");
			writer.append("Banheiros");
			writer.append(";");
			writer.append("Suítes");
			writer.append(";");
			writer.append("Estacionamento");
			writer.append(";");
			writer.append("Preço");
			writer.append("\n");
			
			for (Element anuncio : anuncios) {
				String titulo = anuncio.getElementsByClass("property-card__title").text();
				String endereco = anuncio.getElementsByClass("property-card__address").text();
				String taxa = anuncio.select("div.property-card__price-details--condo strong.js-condo-price").text();
				String area = anuncio.select("li.property-card__detail-area span.property-card__detail-area").text();
				String quarto = anuncio.select("li.property-card__detail-room span.property-card__detail-value").text();
				String banheiro = anuncio.select("li.property-card__detail-bathroom span.property-card__detail-value").text();
				String suite = anuncio.select("li.property-card__detail-bathroom span.property-card__detail-value").text();
				String estacionamento = anuncio.select("li.property-card__detail-garage span.property-card__detail-value").text();
				String price = anuncio.getElementsByClass("property-card__price").text();
				
				writer.append(titulo);
				writer.append(";");
				writer.append(endereco);
				writer.append(";");
				writer.append(taxa);
				writer.append(";");
				writer.append(area);
				writer.append(";");
				writer.append(quarto);
				writer.append(";");
				writer.append(banheiro);
				writer.append(";");
				writer.append(suite);
				writer.append(";");
				writer.append(estacionamento);
				writer.append(";");
				writer.append(price);
				writer.append("\n");
				
				String url2 = proximaPagina.attr("abs:href");
				doc = Jsoup.connect(url2).get();
			}
			
//			proximaPagina.attr("abs:href");
//			System.out.println(proximaPagina.attr("abs:href"));
//			System.out.println("Apartamentos adicionados: " + anuncios.size());
			
//			for (Element next : proximaPagina) {
//				String url2 = next.attr("abs:href");
//		        //Document page = Jsoup.connect(url2).get();
//		        //scrape the page..
//				System.out.println(url2);
//			}

			writer.flush();
	        writer.close();
	        
	        System.out.println("\nProcesso concluído. Arquivo gerado.");
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
