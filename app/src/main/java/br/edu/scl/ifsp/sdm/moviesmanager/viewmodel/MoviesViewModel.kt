package br.edu.scl.ifsp.sdm.moviesmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.scl.ifsp.sdm.moviesmanager.data.database.MovieDataBase
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import br.edu.scl.ifsp.sdm.moviesmanager.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    var allMovies: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()
    lateinit var movie: LiveData<Movie>

    init {
        val dao = MovieDataBase.getDatabase(application).movieDao()
        repository = MovieRepository(dao)
        repository.getAllMovies().observeForever { movieList ->
            movieList?.let {
                allMovies.value = it
            }
        }
    }

    fun orderAllMoviesByNome() = viewModelScope.launch {
        val sortedList = allMovies.value?.let { listMovie ->
            ArrayList(listMovie.sortedBy { it.nome })
        }
        allMovies.value = sortedList!!
    }

    fun orderAllMoviesByNota() = viewModelScope.launch {
        val sortedList = allMovies.value?.let { listMovie ->
            ArrayList(listMovie.sortedBy { it.nota })
        }
        allMovies.value = sortedList!!
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

    fun updateMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMovie(movie)
    }

    fun getMovieById(id: Int) {
        viewModelScope.launch {
            movie = repository.getMovieById(id)
        }
    }
}