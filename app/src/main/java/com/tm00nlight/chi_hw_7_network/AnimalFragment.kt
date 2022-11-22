package com.tm00nlight.chi_hw_7_network

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tm00nlight.chi_hw_7_network.data.Animal
import com.tm00nlight.chi_hw_7_network.databinding.FragmentAnimalListBinding
import com.tm00nlight.chi_hw_7_network.db.AnimalDataBaseManager
import com.tm00nlight.chi_hw_7_network.network.AnimalApiService
import kotlinx.coroutines.*
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_ANIMAL = "https://zoo-animal-api.herokuapp.com/animals/rand/"

class AnimalFragment : Fragment() {
    private lateinit var binding: FragmentAnimalListBinding
    var animals: List<Animal> = emptyList()

    private var dbManager: AnimalDataBaseManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimalListBinding.inflate(inflater, container, false)

        with(binding.root) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyAnimalRecyclerViewAdapter(listOf())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager = AnimalDataBaseManager(requireContext())

        CoroutineScope(Dispatchers.Default).launch {
            loadWithOkHttp()
            loadWithRetrofit("2")
            Log.d("AnimalFragment", "$animals")
            animals = dbManager?.fetchAnimals()!!
            updateUI(animals)
        }
    }

    private fun loadWithOkHttp() {
        val clientOkHttp = OkHttpClient()
        val request = Request.Builder()
            .url(BASE_URL_ANIMAL + "6")
            .build()

        clientOkHttp.newCall(request).execute().use { response ->
            val animalListType = object : TypeToken<ArrayList<Animal>>() {}.type
//            animals = Gson().fromJson(response.body!!.string(), animalListType)
            dbManager?.deleteAnimals()
            dbManager?.insertAnimals(Gson().fromJson(response.body!!.string(), animalListType))
            Log.d("OkHttp", "$animals")
        }
    }

    private fun loadWithRetrofit(count: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_ANIMAL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animalApi = retrofit.create(AnimalApiService::class.java)

        val result = animalApi.getAnimals(count).execute()
        if (result.isSuccessful) {
//            animals += result.body()!!
                dbManager?.insertAnimals(result.body()!!)
        }
        Log.d("Retrofit", "$animals")
    }

    private fun updateUI(animals: List<Animal>) {
        Handler(Looper.getMainLooper()).post {
            binding.root.adapter = MyAnimalRecyclerViewAdapter(animals)
        }
    }

}