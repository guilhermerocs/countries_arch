package com.example.countries.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countries.R
import com.example.countries.model.Country
import com.example.countries.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val countriesAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel.refresh()

        countriesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh()
        }
        observeViewModel()


    }

    private fun observeViewModel() {

        viewModel.countries.observe(this, Observer { countries: List<Country>? ->
            onCountriesAdded(countries)
        })

        viewModel.countryLoadError.observe(this, Observer { isError: Boolean? ->
            manageError(isError)
        })

        viewModel.loading.observe(this, Observer { isLoading: Boolean? ->
            manageLoading(isLoading)
        })
    }


    private fun onCountriesAdded(countries: List<Country>?) {
        countries?.let {
            countriesList.visibility = View.VISIBLE
            countriesAdapter.updateCountries(countries)
        }

    }

    private fun manageError(isError: Boolean?) {
        isError?.let {

            if (isError)
                list_error.visibility = View.VISIBLE
            else
                list_error.visibility = View.GONE

        }


    }

    private fun manageLoading(isLoading: Boolean?) {
        isLoading?.let {
            loading_view.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                list_error.visibility = View.GONE
                countriesList.visibility = View.GONE
            }

        }
    }
}
