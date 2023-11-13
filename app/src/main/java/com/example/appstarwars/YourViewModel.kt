package com.example.appstarwars

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

class YourViewModel : ViewModel() {
    private val _adapterData = MutableLiveData<List<CharacterEntity>>()
    val adapterData: LiveData<List<CharacterEntity>> get() = _adapterData

    private val _filteredData = MutableLiveData<List<CharacterEntity>>()
    val filteredData: LiveData<List<CharacterEntity>> get() = _filteredData

    private lateinit var characterDao: CharacterDao

    fun setCharacterDao(characterDao: CharacterDao) {
        this.characterDao = characterDao
    }

    fun fetchData() {
        val service = ApiConfig.getService()
        val call = service.getResponse()

        call.enqueue(object : Callback<Responses> {
            override fun onResponse(call: Call<Responses>, response: Response<Responses>) {
                if (response.isSuccessful) {
                    val data = response.body()?.results
                    if (data != null) {
                        saveDataToRoom(data as List<ResultsItem>)
                    }
                }
            }

            override fun onFailure(call: Call<Responses>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun saveDataToRoom(data: List<ResultsItem>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                characterDao.insertCharacter(data.map {
                    CharacterEntity(
                        name = it.name,
                        gender = it.gender,
                        birthYear = it.birthYear,
                        skinColor = it.skinColor,
                        eyeColor = it.eyeColor,
                        height = it.height
                    )
                })
            }
        }
    }

    fun filterData(searchText: String) {
        viewModelScope.launch {
            val data = characterDao.searchCharacters("%$searchText%")
            _filteredData.value = data.value // Retrieve the List<CharacterEntity> from LiveData
        }
    }

}
