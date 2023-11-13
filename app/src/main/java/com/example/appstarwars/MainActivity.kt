package com.example.appstarwars

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appstarwars.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: YourViewModel by viewModels()
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.adapterData.observe(this) { data ->
            val convertedData = data.map { characterEntity ->
                ResultsItem(
                    name = characterEntity.name,
                    gender = characterEntity.gender,
                    birthYear = characterEntity.birthYear,
                    skinColor = characterEntity.skinColor,
                    eyeColor = characterEntity.eyeColor,
                    height = characterEntity.height
                )
            }
            adapter.submitList(convertedData)
        }

        viewModel.fetchData()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}
