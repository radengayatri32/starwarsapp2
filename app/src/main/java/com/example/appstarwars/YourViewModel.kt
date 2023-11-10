package com.example.appstarwars

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourViewModel(applicationContext: Context) : ViewModel() {
    private val _adapterData = MutableLiveData<List<ResultsItem>>()
    val adapterData: LiveData<List<ResultsItem>> get() = _adapterData

    private val _filteredData = MutableLiveData<List<ResultsItem>>()
    val filteredData: LiveData<List<ResultsItem>> get() = _filteredData

    private val database: CharacterDatabase = CharacterDatabase.getDatabase(applicationContext)

    fun fetchData() {
        val service = ApiConfig.getService()
        val call = service.getResponse()

        call.enqueue(object : Callback<Responses> {
            override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                if (response.isSuccessful) {
                    val data = response.body()?.results
                    if (data != null) {
                        _adapterData.value = data as List<ResultsItem>?
                        _filteredData.value = data as List<ResultsItem>?
                        saveDataToRoom(data)
                    } else {
                        _adapterData.value = emptyList()
                        _filteredData.value = emptyList()
                    }
                } else {
                    _adapterData.value = emptyList()
                    _filteredData.value = emptyList()
                }
            }

            override fun onFailure(call: Call<Responses>, t: Throwable) {
                // Handle the failure here
                _adapterData.value = emptyList()
                _filteredData.value = emptyList()
            }
        })
    }

    fun formatHeight(height: String?): String {
        return "$height cm"
    }

    private fun saveDataToRoom(data: List<ResultsItem>) {
        val characterEntities = data.map {
            CharacterEntity(
                name = it.name,
                gender = it.gender,
                birthYear = it.birthYear,
                skinColor = it.skinColor,
                eyeColor = it.eyeColor,
                height = it.height
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.characterDao().insertCharacter(characterEntities)
            }
        }
    }

    fun filterData(searchText: String) {
        val data = _adapterData.value
        if (data != null) {
            val filteredList = data.filter { item ->
                item.name?.contains(searchText, ignoreCase = true) == true
            }
            _filteredData.value = filteredList
        }
    }
}