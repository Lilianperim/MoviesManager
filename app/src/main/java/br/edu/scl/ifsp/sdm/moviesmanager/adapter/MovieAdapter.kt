package br.edu.scl.ifsp.sdm.moviesmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import br.edu.scl.ifsp.sdm.moviesmanager.databinding.MovieItemBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private lateinit var binding: MovieItemBinding
    var moviesList = ArrayList<Movie>()
    var movieListFilterable = ArrayList<Movie>()

    fun updateList(newList: ArrayList<Movie>) {
        moviesList = newList
        movieListFilterable = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.nomeView.text = moviesList[position].nome
        holder.anoView.text = moviesList[position].ano
        holder.duracaoView.text = moviesList[position].duracao.toString()
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    inner class MovieViewHolder(binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nomeView = binding.nome
        val anoView = binding.ano
        val duracaoView = binding.duracao
    }
}