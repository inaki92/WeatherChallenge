package com.inaki.weathercodechallenge.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inaki.weathercodechallenge.R
import com.inaki.weathercodechallenge.adapters.ForecastAdapter
import com.inaki.weathercodechallenge.adapters.ForecastDetailsListener
import com.inaki.weathercodechallenge.databinding.FragmentForecastBinding
import com.inaki.weathercodechallenge.model.Forecast
import com.inaki.weathercodechallenge.model.WeatherResponse
import com.inaki.weathercodechallenge.utils.*
import com.inaki.weathercodechallenge.viewmodel.WeatherViewModel

class ForecastFragment : BaseFragment(), ForecastDetailsListener {

    private lateinit var binding: FragmentForecastBinding

    private val viewModel: WeatherViewModel by activityViewModels()

    private val forecastAdapter: ForecastAdapter by lazy {
        ForecastAdapter(requireContext(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater)
        binding.weatherToolbar.setNavigationOnClickListener {
            navigateBack()
        }

        viewModel.currentWeather.observe(viewLifecycleOwner, ::handleWeather)

        binding.forecastRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.forecastRecycler.setHasFixedSize(true)
        binding.forecastRecycler.adapter = forecastAdapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val city = arguments?.getString(ARG_CITY, "") ?: ""
        viewModel.loadCurrentLocationWeather(city)
    }

    private fun handleWeather(result: Result<WeatherResponse, Failure>) {
        result
            .onLoading {
                binding.loadingForecast.visibility = View.VISIBLE
                binding.forecastRecycler.visibility = View.GONE
            }
            .onSuccess {
                forecastAdapter.setNewData(it.list)
                binding.loadingForecast.visibility = View.GONE
                binding.forecastRecycler.visibility = View.VISIBLE
            }
            .onFailure {
                binding.loadingForecast.visibility = View.GONE
                binding.forecastRecycler.visibility = View.GONE
                navigateBack(isError = true)
            }
    }

    private fun navigateBack(isError: Boolean = false) {
        viewModel.reset()
        viewModel.isError = isError
        viewModel.currentWeather.removeObservers(viewLifecycleOwner)
        findNavController().popBackStack(R.id.entryCityFragment, false)
    }

    companion object {
        fun newInstance() = ForecastFragment()
    }

    override fun navigateToDetails(forecast: Forecast) {
        viewModel.passForecastDetails(forecast)
        findNavController().navigate(R.id.navigate_from_forecast_to_details)
    }
}