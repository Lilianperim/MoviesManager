package br.edu.scl.ifsp.sdm.moviesmanager.repository

import androidx.lifecycle.LiveData
import br.edu.scl.ifsp.sdm.moviesmanager.data.dao.MovieDao
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie

class MovieRepository(private val movieDao: MovieDao) {
    suspend fun insertMovie(movie: Movie) {
        movieDao.insert(movie)
    }

    suspend fun updateMovie(movie: Movie) {
        movieDao.update(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.delete(movie)
    }

    fun getAllMovies(): LiveData<List<Movie>> {
        return movieDao.getAllMovies()
    }

    fun isMovieNameExists(movieName: String): Boolean {
        return movieDao.countMoviesByName(movieName) > 0
    }
}