package br.edu.scl.ifsp.sdm.moviesmanager.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Movie(
    @PrimaryKey
    val nome: String,
    val genero: String,
    var ano: String,
    var produtora: String,
    var duracao: Int,
    var nota: Int,
    var assistido: Boolean
) : Parcelable