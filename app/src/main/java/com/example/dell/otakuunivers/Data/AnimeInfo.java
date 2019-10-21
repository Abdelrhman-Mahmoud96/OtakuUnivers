package com.example.dell.otakuunivers.Data;

import java.util.List;

public class AnimeInfo {

    String id;
    String title;
    String startedAt;
    String endAt;
    String description;
    String coverImage;
    String posters;
    String characters;
    String rating;
    String popularity;
    String ageRating;
    String ratingGuid;
    String status;
    String noOfEpisodes;
    String trailerLink;
    String genre;
    String episodes;
    String StreamLinks;
    String production;
    String staff;

    public AnimeInfo()
    {

    }
    public AnimeInfo( String c_id,String c_title,String c_startedAt,String c_endAt,String c_description,String c_coverImage,String c_posters,String c_rating,
                        String c_characters,String c_popularity,String c_ageRating,String c_ratingGuid,String c_status,String c_noOfEpisodes,String c_staff,
                        String c_trailerLink,String c_genre,String c_episodes,String c_StreamLinks,String c_production)
    {
        id = c_id;
        startedAt = c_startedAt;
        title = c_title;
        endAt = c_endAt;
        description = c_description;
        coverImage = c_coverImage;
        posters = c_posters;
        characters = c_characters;
        rating = c_rating;
        popularity = c_popularity;
        ageRating = c_ageRating;
        ratingGuid = c_ratingGuid;
        status = c_status;
        noOfEpisodes = c_noOfEpisodes;
        trailerLink = c_trailerLink;
        genre = c_genre;
        episodes = c_episodes;
        StreamLinks = c_StreamLinks;
        production = c_production;
        staff = c_staff;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getCharacters() {
        return characters;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getPosters() {
        return posters;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getRating() {
        return rating;
    }

    public String getRatingGuid() {
        return ratingGuid;
    }

    public String getNoOfEpisodes() {
        return noOfEpisodes;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getEpisodes() {
        return episodes;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getProduction() {
        return production;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public String getStaff() {
        return staff;
    }

    public String getStreamLinks() {
        return StreamLinks;
    }

}
