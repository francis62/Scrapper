package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void scrapeProductPage(
            List<Book> books,
            Set<String> pagesDiscovered,
            List<String> pagesToScrape
    ){
        String url = pagesToScrape.remove(0);

        pagesDiscovered.add(url);

        Document doc;

        //try catch por si la conexion a la pagina falla
        try{
            //agregando la pagina
            //muchos sitios blockean automaticamente las consultas que no tienen ciertos headers http, una manera de evitar esto es setteando manualmente estos headers
            doc = Jsoup
                    .connect("https://www.wob.com/en-gb/category/fiction-books")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .header("Accept-Language","*")
                    .get();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        Elements products = doc.select("div.gridItem");

        for (Element product:products){
            Book book = new Book();

            book.setUrl("https://www.wob.com" + product.selectFirst("a").attr("href"));
            book.setImage(product.selectFirst("img").attr("data-original"));
            book.setName(product.selectFirst("span.title").text());
            book.setAuthor(product.selectFirst("span.author").text());
            book.setPrice(product.selectFirst("div.itemPrice").text());

            books.add(book);
        }

        Elements paginationElements = doc.select("a.page-link");
        //Elements paginationElements = doc.select("span.pagnLA.a");
        
        for (Element pageElement : paginationElements){
            String pageUrl = pageElement.attr("href");

            if (!pagesDiscovered.contains(pageUrl) && !pagesToScrape.contains(pageUrl)){
                pagesToScrape.add(pageUrl);
            }

            pagesDiscovered.add(pageUrl);
        }
    }

    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();

        Set<String> pageDiscovered = new HashSet<>();

        List<String> pagesToScrape = new ArrayList<>();

        pagesToScrape.add("https://www.wob.com/en-gb/category/fiction-books");

        int i = 0;
        int limit = 6;

        while(!pagesToScrape.isEmpty() && i < limit){
            scrapeProductPage(books, pageDiscovered, pagesToScrape);
            i++;
        }

        System.out.println(books.size());

        String json = books.toString();

        String filePath = "src/main/data/output.json";

        try {
            Files.write(Paths.get(filePath), json.getBytes());
            System.out.println("Archivo JSON creado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al crear el archivo JSON: " + e.getMessage());
        }

    }
}