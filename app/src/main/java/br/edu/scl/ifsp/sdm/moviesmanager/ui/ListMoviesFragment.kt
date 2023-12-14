package br.edu.scl.ifsp.sdm.moviesmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.scl.ifsp.sdm.moviesmanager.adapter.MovieAdapter
import br.edu.scl.ifsp.sdm.moviesmanager.databinding.FragmentListMoviesBinding
import br.edu.scl.ifsp.sdm.moviesmanager.viewmodel.MoviesViewModel
import androidx.navigation.fragment.findNavController
import br.edu.scl.ifsp.sdm.moviesmanager.R
import androidx.lifecycle.ViewModelProvider
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import androidx.recyclerview.widget.LinearLayoutManager

class ListMoviesFragment : Fragment() {

    private var _binding: FragmentListMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        movieAdapter = MovieAdapter { movie, watched ->
            movie.assistido = watched
            viewModel.updateMovie(movie)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMoviesBinding.inflate(inflater, container, false)
        setupClick()
        configureRecyclerView()
        return binding.root
    }

    private fun configureRecyclerView() {
        viewModel.allMovies.observe(viewLifecycleOwner) { list ->
            list?.let {
                movieAdapter.updateList(list as ArrayList<Movie>)
            }
        }
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = movieAdapter
    }

    private fun setupClick() = with(binding){
        submitOrdernarPorNome.setOnClickListener {
            viewModel.orderAllMoviesByNome()
        }

        submitOrdernarPorNota.setOnClickListener {
            viewModel.orderAllMoviesByNota()
        }

       floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_listMoviesFragment_to_registerFragment)
        }

        val listener = object : MovieAdapter.OnMovieClickListener {
            override fun onMovieClick(position: Int) {
                val c = movieAdapter.moviesList[position]
                val bundle = Bundle()
                bundle.putInt("idMovie", c.id)
                findNavController().navigate(
                    R.id.action_listaFilmesFragment_to_detailsFragment,
                    bundle
                )
            }
        }
        movieAdapter.setClickListener(listener)
    }

}