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
    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMoviesBinding.inflate(inflater, container, false)
        binding.floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_listMoviesFragment_to_registerFragment)
        }
        configureRecyclerView()
        return binding.root
    }

    private fun configureRecyclerView() {
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.allMovies.observe(viewLifecycleOwner) { list ->
            list?.let {
                movieAdapter.updateList(list as ArrayList<Movie>)
            }
        }
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieAdapter()
        recyclerView.adapter = movieAdapter
    }

}