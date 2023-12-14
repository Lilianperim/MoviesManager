package br.edu.scl.ifsp.sdm.moviesmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private lateinit var viewModel: MoviesViewModel
    private var isMovieWatched: Boolean = false
    private val movieGenreList = listOf("Romance", "Comédia", "Terror", "Drama", "Aventura")
    private var genreSelected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
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
        setupSpinnerGenre()
        setupClickCheckBox()
        setupSaveNewMovieButton()
    }

    private fun setupSaveNewMovieButton() {
        binding.commonLayout.submitRatingButton.setOnClickListener {
            val nota = binding.commonLayout.ratingBar.rating
            val nome = binding.commonLayout.editTextNome.text.toString()
            val ano = binding.commonLayout.editTextAno.text.toString()
            val produtora = binding.commonLayout.editTextProdutora.text.toString()
            val duracao = binding.commonLayout.editTextDuracao.text.toString().toIntOrNull()

            if (nome.isBlank() || ano.isBlank() || produtora.isBlank() || duracao == null) {
                Snackbar.make(
                    binding.root,
                    "Por favor, preencha todos os campos",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            // no cadastro o filme nao tem ID cadastrado
            val movieID = 0
            viewModel.isMovieNameExists(nome, movieID) { exists ->
                if (exists) {
                    Snackbar.make(
                        binding.root,
                        "Um filme com esse nome já existe!",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    saveMovie(nome, ano, produtora, duracao, nota)
                }
            }
        }
    }

    private fun saveMovie(
        nome: String,
        ano: String,
        produtora: String,
        duracao: Int,
        nota: Float
    ) {
        val movie = Movie(0, nome, genreSelected, ano, produtora, duracao, nota, isMovieWatched)
        viewModel.insert(movie)
        Snackbar.make(binding.root, "Filme salvo com sucesso!", Snackbar.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun setupSpinnerGenre() {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            movieGenreList
        )
        binding.commonLayout.genreSpinner.adapter = adapter
        binding.commonLayout.genreSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    genreSelected = movieGenreList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Snackbar.make(
                        binding.root,
                        "Por favor, selecione um gênero",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setupClickCheckBox() {
        binding.commonLayout.checkboxMovie.setOnCheckedChangeListener { _, isChecked ->
            isMovieWatched = isChecked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
