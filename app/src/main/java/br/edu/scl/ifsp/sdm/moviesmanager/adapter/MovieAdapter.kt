package br.edu.scl.ifsp.sdm.moviesmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.moviesmanager.R
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import br.edu.scl.ifsp.sdm.moviesmanager.databinding.MovieItemBinding

class MovieAdapter(private val onMovieWatchedChanged: (Movie, Boolean) -> Unit) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    var moviesList = ArrayList<Movie>()

    fun updateList(newList: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moviesList[position]
        val context = holder.itemView.context
        with(holder) {
            nomeView.text = context.getString(R.string.format_nome, movie.nome)
            generoView.text = context.getString(R.string.format_genero, movie.genero)
            anoView.text = context.getString(R.string.format_ano, movie.ano)
            produtoraView.text = context.getString(R.string.format_produtora, movie.produtora)
            duracaoView.text = context.getString(R.string.format_duracao, movie.duracao)
            notaView.text = context.getString(R.string.format_nota, movie.nota.toString())
            assistidoView.isChecked = movie.assistido
            assistidoView.setOnCheckedChangeListener { _, isChecked ->
                onMovieWatchedChanged(movie, isChecked)
            }
        }
    }

    override fun getItemCount() = moviesList.size

    inner class MovieViewHolder(binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nomeView = binding.nome
        val generoView = binding.genero
        val anoView = binding.ano
        val produtoraView = binding.produtora
        val duracaoView = binding.duracao
        val notaView = binding.nota
        val assistidoView = binding.checkboxAssistido
    }
}