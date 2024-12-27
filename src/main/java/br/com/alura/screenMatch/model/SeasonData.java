package br.com.alura.screenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(
        @JsonAlias({"Season", "seasonNumber"}) int seasonNumber,
        @JsonAlias("Episodes") List<EpisodeData> episodes
) {}
