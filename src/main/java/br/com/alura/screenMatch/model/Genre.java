package br.com.alura.screenMatch.model;

public enum Genre {
    ACTION("Action", "Ação"),
    ADVENTURE("Adventure", "Aventura"),
    COMEDY("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    DRAMA("Drama", "Drama"),
    FANTASY("Fantasy", "Fantasia"),
    HISTORICAL("Historical", "Histórico"),
    HORROR("Horror", "Terror"),
    MYSTERY("Mystery", "Mistério"),
    ROMANCE("Romance", "Romance"),
    SCIENCE_FICTION("Science Fiction", "Ficção Científica"),
    THRILLER("Thriller", "Suspense"),
    WESTERN("Western", "Faroeste");

    private String omdbGenre;
    private String genreInPortuguese;

    Genre(String omdbGenre, String genreInPortuguese) {
        this.omdbGenre = omdbGenre;
        this.genreInPortuguese = genreInPortuguese;
    }

    public static Genre fromString(String text) {
        for (Genre genre : Genre.values()) {
            if (genre.omdbGenre.equalsIgnoreCase(text)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
    public static Genre fromPortuguese(String text) {
        for (Genre genre : Genre.values()) {
            if (genre.genreInPortuguese.equalsIgnoreCase(text)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
