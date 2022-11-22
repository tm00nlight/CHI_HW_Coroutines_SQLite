package com.tm00nlight.chi_hw_7_network

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tm00nlight.chi_hw_7_network.data.Animal
import com.tm00nlight.chi_hw_7_network.data.Marvel
import com.tm00nlight.chi_hw_7_network.databinding.FragmentMarvelListBinding
import com.tm00nlight.chi_hw_7_network.network.AnimalApiService
import com.tm00nlight.chi_hw_7_network.network.MarvelApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_MARVEL = "https://www.simplifiedcoding.net/demos/marvel/"

class MarvelFragment : Fragment() {
    private lateinit var binding: FragmentMarvelListBinding
    var heroes: MutableList<Marvel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarvelListBinding.inflate(inflater, container, false)

        with(binding.root) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyMarvelRecyclerViewAdapter(listOf())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_MARVEL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val marvelApi = retrofit.create(MarvelApiService::class.java)

            val result = marvelApi.getHeroes().execute()
            if (result.isSuccessful) {
                heroes = result.body()!!
            }
            Log.d("Retrofit", "$heroes")

            updateUI(heroes)
        }.start()
    }

    private fun updateUI(heroes: MutableList<Marvel>) {
        Handler(Looper.getMainLooper()).post {
            binding.root.adapter = MyMarvelRecyclerViewAdapter(heroes)
        }
    }

}