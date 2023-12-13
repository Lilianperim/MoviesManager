package br.edu.scl.ifsp.sdm.moviesmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.edu.scl.ifsp.sdm.moviesmanager.data.database.MovieDataBase
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import br.edu.scl.ifsp.sdm.moviesmanager.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    var allMovies: LiveData<List<Movie>>
    lateinit var movie: LiveData<Movie>

    init {
        val dao = MovieDataBase.getDatabase(application).movieDao()
        repository = MovieRepository(dao)
        allMovies = repository.getAllMovies()
    }

    fun insert(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMovie(movie)
    }

    fun isMovieNameExists(movieName: String, callback: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val exists = repository.isMovieNameExists(movieName)
            withContext(Dispatchers.Main) {
                callback(exists)
            }
        }
}