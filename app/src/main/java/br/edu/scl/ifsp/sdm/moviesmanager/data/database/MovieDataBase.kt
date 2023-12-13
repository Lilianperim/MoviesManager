package br.edu.scl.ifsp.sdm.moviesmanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.scl.ifsp.sdm.moviesmanager.data.dao.MovieDao
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDataBase? = null

        fun getDatabase(context: Context): MovieDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDataBase::class.java,
                    "moviesManager.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}