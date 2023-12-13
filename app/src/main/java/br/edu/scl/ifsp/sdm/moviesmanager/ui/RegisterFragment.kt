package br.edu.scl.ifsp.sdm.moviesmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import br.edu.scl.ifsp.sdm.moviesmanager.databinding.FragmentRegisterBinding
import br.edu.scl.ifsp.sdm.moviesmanager.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MoviesViewModel
    private var isMovieWatched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveNewMovie()
        setupClickCheckBox()
    }

    private fun saveNewMovie() {
        binding.commonLayout.submitRatingButton.setOnClickListener {
            val nota = binding.commonLayout.ratingBar.rating
            val nome = binding.commonLayout.editTextNome.text.toString()
            val ano = binding.commonLayout.editTextAno.text.toString()
            val duracao = binding.commonLayout.editTextDuracao.text.toString().toIntOrNull()
            val movie = Movie(
                nome = nome,
                genero = null,
                ano = ano,
                produtora = null,
                duracao = duracao,
                nota = nota,
                assistido = isMovieWatched
            )
            viewModel.insert(movie)
            Snackbar.make(binding.root, "Filme salvo com sucesso!", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
            true
        }
    }

    private fun setupClickCheckBox() {
        binding.commonLayout.checkboxMovie.setOnCheckedChangeListener { _, isChecked ->
            isMovieWatched = isChecked
        }
    }
}