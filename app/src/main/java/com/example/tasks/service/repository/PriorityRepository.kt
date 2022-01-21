package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository( val context: Context ) : BaseRepository( context ) {

    private val mRemote = RetrofitClient.createService( PriorityService::class.java )
    private val mPriorityDataBase = TaskDatabase.getDatabase( context ).priorityDAO()

    fun all() {

        if ( !isConnectionAvailable( context ) ) {
            return
        }

        val call: Call<List<PriorityModel>> = mRemote.list()
        call.enqueue( object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if ( response.code() == TaskConstants.HTTP.SUCCESS ) {
                    mPriorityDataBase.clear()
                    response.body()?.let { mPriorityDataBase.save(it) }
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
            }
        })
    }

    fun list() = mPriorityDataBase.list()

    fun getDescription( id: Int ) = mPriorityDataBase.getDEscription( id )
}