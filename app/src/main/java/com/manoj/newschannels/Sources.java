package com.manoj.newschannels;

public  class Sources {

    private String name ;
    private  String description;
    private  String url;
    private String category;
    private String language;

    public Sources(String name, String description, String url, String category, String language) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }
}
