package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(@JsonAlias({"name", "title", "Title"}) String title, @JsonAlias({"release_date", "first_air_date", "Released"}) String releaseDate, @JsonAlias({"Episode", "episodeNumber"}) int episodeNumber, @JsonAlias({"vote_average", "imdbRating"}) String rating) { }
