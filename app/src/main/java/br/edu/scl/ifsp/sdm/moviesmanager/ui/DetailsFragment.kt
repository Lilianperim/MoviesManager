package br.edu.scl.ifsp.sdm.moviesmanager.ui

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import br.edu.scl.ifsp.sdm.moviesmanager.R
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.edu.scl.ifsp.sdm.moviesmanager.data.entity.Movie
import br.edu.scl.ifsp.sdm.moviesmanager.databinding.FragmentDetailsBinding
import br.edu.scl.ifsp.sdm.moviesmanager.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var movie: Movie
    private lateinit var assistidoCheckBox: CheckBox
    private lateinit var nomeEditText: EditText
    private lateinit var anoEditText: EditText
    private lateinit var produtoraEditText: EditText
    private lateinit var duracaoEditText: EditText
    private lateinit var categoriaSpinner: AppCompatSpinner
    private lateinit var notaRatingBar: AppCompatRatingBar
    private lateinit var viewModel: MoviesViewModel
    private lateinit var movieGenreList: List<String>
    private var genreSelected: String = ""
    private var isMovieWatched: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        val movieList = resources.getStringArray(R.array.movie_genres)
        movieGenreList = movieList.toList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinnerGenre()
        setupClickCheckBox()
        setupSaveNewMovieButton()
        with(binding.commonLayout) {
            assistidoCheckBox = checkboxMovie
            nomeEditText = editTextNome
            anoEditText = editTextAno
            produtoraEditText = editTextProdutora
            duracaoEditText = editTextDuracao
            categoriaSpinner = genreSpinner
            notaRatingBar = ratingBar
        }

        assistidoCheckBox.isEnabled = false
        nomeEditText.isEnabled = false
        anoEditText.isEnabled = false
        produtoraEditText.isEnabled = false
        duracaoEditText.isEnabled = false
        categoriaSpinner.isEnabled = false
        notaRatingBar.isEnabled = false
        binding.commonLayout.submitRatingButton.isVisible = false

        val idMovie = requireArguments().getInt("idMovie")
        viewModel.getMovieById(idMovie)
        viewModel.movie.observe(viewLifecycleOwner) { result ->
            result?.let {
                movie = result
                assistidoCheckBox.isChecked = movie.assistido
                nomeEditText.setText(movie.nome)
                anoEditText.setText(movie.ano)
                produtoraEditText.setText(movie.produtora)
                duracaoEditText.setText(movie.duracao.toString())
                notaRatingBar.rating = movie.nota

                val indexCategoriaSelected = movieGenreList.indexOf(movie.genero)
                categoriaSpinner.setSelection(indexCategoriaSelected)
            }
        }
        setupMenu()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.context_menu_movie, menu)

                val editItem = menu.findItem(R.id.editMovie)
                val removeItem = menu.findItem(R.id.removeMovie)
                setMenuItemColor(editItem)
                setMenuItemColor(removeItem)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.editMovie -> {
                        assistidoCheckBox.isEnabled = true
                        anoEditText.isEnabled = true
                        produtoraEditText.isEnabled = true
                        duracaoEditText.isEnabled = true
                        categoriaSpinner.isEnabled = true
                        notaRatingBar.isEnabled = true
                        binding.commonLayout.submitRatingButton.isVisible = true
                        true
                    }

                    R.id.removeMovie -> {
                        viewModel.deleteMovie(movie)
                        Snackbar.make(
                            binding.root,
                            "Filme removido com sucesso!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setMenuItemColor(menuItem: MenuItem) {
        val spannableString = SpannableString(menuItem.title)
        spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, spannableString.length, 0)
        menuItem.title = spannableString
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
            updateMovie(nome, ano, produtora, duracao, nota)
        }
    }

    private fun updateMovie(
        nome: String,
        ano: String,
        produtora: String,
        duracao: Int,
        nota: Float
    ) {
        val movie =
            Movie(movie.id, nome, genreSelected, ano, produtora, duracao, nota, isMovieWatched)
        viewModel.updateMovie(movie)
        Snackbar.make(binding.root, "Filme editado com sucesso!", Snackbar.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun setupClickCheckBox() {
        binding.commonLayout.checkboxMovie.setOnCheckedChangeListener { _, isChecked ->
            isMovieWatched = isChecked
        }
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

}