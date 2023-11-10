package com.example.appstarwars


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appstarwars.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: YourViewModel
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(YourViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe the LiveData for adapter data
        viewModel.adapterData.observe(this) { data ->
            adapter.submitList(data)
        }

        // Fetch data from the API using LiveData
        viewModel.fetchData()

        // Form Pencarian
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // When text changes, update the filter in the ViewModel
                viewModel.filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed here
            }
        })

        // Observe the filtered data
        viewModel.filteredData.observe(this) { filteredData ->
            adapter.submitList(filteredData)
        }
    }
}