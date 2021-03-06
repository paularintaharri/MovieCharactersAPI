package com.project.moviecharacterapi.controllers;

import com.project.moviecharacterapi.models.Character;
import com.project.moviecharacterapi.models.Movie;
import com.project.moviecharacterapi.repositories.CharacterRepository;
import com.project.moviecharacterapi.repositories.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieRepository movieRepository;
    private final CharacterRepository characterRepository;

    public MovieController(MovieRepository movieRepository, CharacterRepository characterRepository) {
        this.movieRepository = movieRepository;
        this.characterRepository = characterRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Movie>> getAllMovies() {
        HttpStatus status;
        List<Movie> movies = movieRepository.findAll();
        if(movies.size() ==0){
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(movies, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getSpecificMovie(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Movie returnMovie = new Movie();
        boolean exists = movieRepository.existsById(id);
        if(exists){
            returnMovie = movieRepository.findById(id).get();
            status = HttpStatus.OK;
            return new ResponseEntity<>(returnMovie, status);
        }
        return new ResponseEntity<>(null, status);
    }
    //Get all characters in a movie
    @GetMapping("/{id}/characters")
    public ResponseEntity<List<Character>> getCharactersForMovie(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Movie returnMovie = new Movie();
        boolean exists = movieRepository.existsById(id);
        List<Character> charactersForMovie = new ArrayList<>();
        if(exists){
            returnMovie = movieRepository.findById(id).get();
            status = HttpStatus.OK;
            charactersForMovie = characterRepository.findAllByMovies(returnMovie);
        }
        return new ResponseEntity<>(charactersForMovie, status);
    }

    @PostMapping
    public ResponseEntity<Movie> addNewMovie(@RequestBody Movie movie) {
        HttpStatus status = HttpStatus.CREATED;
        Movie returnMovie = movieRepository.save(movie);
        return new ResponseEntity<>(returnMovie, status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie){
        Movie returnMovie = new Movie();
        HttpStatus status = HttpStatus.NO_CONTENT;
        if(!id.equals(movie.getId())){
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(returnMovie, status);
        }
        returnMovie = movieRepository.save(movie);
        return new ResponseEntity<>(returnMovie, status);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteMovie(@RequestBody Movie movie){
        HttpStatus status = HttpStatus.NO_CONTENT;
        if(movieRepository.existsById(movie.getId())){
            movieRepository.delete(movie);
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(status);
    }
}
