package com.rrat.happyplaces.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rrat.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import com.rrat.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var happyPlaceDetailsModel : HappyPlaceModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailsModel = intent.getSerializableExtra(
                    MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }

        if(happyPlaceDetailsModel != null){
            setSupportActionBar(binding.toolbarHappyPlaceDetails)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailsModel.title

            binding.toolbarHappyPlaceDetails.setNavigationOnClickListener {
                onBackPressed()
            }

            binding.imageViewPlaceImageDetails.setImageURI(Uri.parse(happyPlaceDetailsModel.image))
            binding.textViewDescriptionDetails.text = happyPlaceDetailsModel.description
            binding.textViewLocationDetails.text = happyPlaceDetailsModel.location

            binding.buttonViewOnMap.setOnClickListener{
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailsModel)
                startActivity(intent)
            }
        }

    }
}