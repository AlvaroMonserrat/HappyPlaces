package com.rrat.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rrat.happyplaces.adapters.HappyPlacesAdapter
import com.rrat.happyplaces.database.DatabaseHandler
import com.rrat.happyplaces.databinding.ActivityMainBinding
import com.rrat.happyplaces.models.HappyPlaceModel
import com.rrat.happyplaces.utils.SwipeToDeleteCallback
import com.rrat.happyplaces.utils.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, HAPPY_PLACES_RESULT)
        }
        getHappyPlacesListFromLocalDB()
    }

    private fun setupHappyPlacesRecycleView(happyPlaceList: ArrayList<HappyPlaceModel>){
        binding.recyclerViewPlaceList.layoutManager = LinearLayoutManager(this)

        binding.recyclerViewPlaceList.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        binding.recyclerViewPlaceList.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerViewPlaceList.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, HAPPY_PLACES_RESULT)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.recyclerViewPlaceList)


        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerViewPlaceList.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesListFromLocalDB()
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.recyclerViewPlaceList)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == HAPPY_PLACES_RESULT){
            if(resultCode == Activity.RESULT_OK){
                getHappyPlacesListFromLocalDB()
            }else{
                Log.i("Activity", "Cancelled or Back pressed")
            }
        }
    }

    companion object{
        const val HAPPY_PLACES_RESULT = 1
        const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}