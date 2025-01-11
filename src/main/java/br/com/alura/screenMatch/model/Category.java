package br.com.alura.screenMatch.model;

public enum Category {
    AÇÃO("action"),
    AVENTURA("adventure"),
    COMÉDIA("comedy"),
    CRIME("crime"),
    DRAMA("drama"),
    FANTASIA("fantasy"),
    HISTÓRICO("historical"),
    HORROR("horror"),
    MISTÉRIO("mystery"),
    ROMANCE("romance"),
    FICÇÃO_CIENTÍFICA("science fiction"),
    SUSPENSE("thriller"),
    FAROESTE("western");

    private String omdbCategory;

    Category(String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
