package org.example;

public class Book{
    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String url;
    private String image;
    private String author;
    private String name;
    private String price;

    @Override
    public String toString() {

        return "{\n" +
                "  \"url\": \"" + url + "\",\n" +
                "  \"image\": \"" + image + "\",\n" +
                "  \"author\": \"" + author + "\",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"price\": \"" + price + "\"\n" +
                "}";
    }
}