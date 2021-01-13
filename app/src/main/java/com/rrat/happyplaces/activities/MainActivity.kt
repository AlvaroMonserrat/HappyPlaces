package com.rrat.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rrat.happyplaces.adapters.HappyPlacesAdapter
import com.rrat.happyplaces.database.DatabaseHandler
import com.rrat.happyplaces.databinding.ActivityMainBinding
import com.rrat.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
        getHappyPlacesListFromLocalDB()
    }

    private fun setupHappyPlacesRecycleView(happyPlaceList: ArrayList<HappyPlaceModel>){
        binding.recyclerViewPlaceList.layoutManager = LinearLayoutManager(this)

        binding.recyclerViewPlaceList.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        binding.recyclerViewPlaceList.adapter = placesAdapter

    }

    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlaceList()

        if(getHappyPlaceList.size > 0){
            binding.recyclerViewPlaceList.visibility = View.VISIBLE
            binding.textViewNoRecords.visibility = View.GONE
            setupHappyPlacesRecycleView(getHappyPlaceList)
        }else{
            binding.recyclerViewPlaceList.visibility = View.GONE
            binding.textViewNoRecords.visibility = View.VISIBLE
        }
    }
}