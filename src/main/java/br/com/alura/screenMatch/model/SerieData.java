package br.com.alura.screenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieData(
        @JsonAlias({"name", "title", "Title"}) String title,
        @JsonAlias({"synopsis", "Plot"}) String synopsis,
        @JsonAlias({"release_date", "first_air_date", "Released"}) String releaseDate,
        @JsonAlias({"vote_average", "imdbRating"}) Double rating,
        int totalSeasons,
        @JsonAlias("Genre") String genre,
        @JsonAlias("Actors") String actors,
        @JsonAlias("Poster") String poster
) {}
