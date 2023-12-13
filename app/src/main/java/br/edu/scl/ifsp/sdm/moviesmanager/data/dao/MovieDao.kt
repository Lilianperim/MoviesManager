package br.edu.scl.ifsp.sdm.moviesmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update (movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY nome")
    fun getAllMovies(): LiveData<List<Movie>>
}