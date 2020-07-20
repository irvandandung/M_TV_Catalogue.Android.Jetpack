package com.example.jetpackpro.data.source.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jetpackpro.data.movieentity.Movie
import com.example.jetpackpro.data.movieentity.MoviesEntity
import com.example.jetpackpro.data.movieentity.Result
import com.example.jetpackpro.data.source.remote.network.ApiServices
import com.example.jetpackpro.data.tvshowentity.TvShow
import com.example.jetpackpro.data.tvshowentity.TvShowsEntity
import com.example.jetpackpro.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Response

class RemoteDataSource private constructor(private var context: Context, private var api : ApiServices){
    companion object{
        @Volatile
        private var instance : RemoteDataSource? = null

        fun getInstance(helper: Context, api : ApiServices): RemoteDataSource =
            instance
                ?: synchronized(this){
                instance
                    ?: RemoteDataSource(
                        helper,
                        api
                    )
        }
    }

    fun getAllMovies() : LiveData<List<Result>>{
        EspressoIdlingResource.increment()
        val list = MutableLiveData<List<Result>>()
        api.getPopularMovies(page = 1, language = "en-US").enqueue(object : retrofit2.Callback<MoviesEntity>{
            override fun onFailure(call: Call<MoviesEntity>, t: Throwable) {
                Log.e("errorgetdara",t.message.toString())
            }
            override fun onResponse(call: Call<MoviesEntity>, response: Response<MoviesEntity>) {
                EspressoIdlingResource.decrement()
                if (response.isSuccessful){
                    val listmovie = response.body()?.results
                    list.postValue(listmovie)
                }
            }
        })
        return list
    }

    fun getDetailMovie(movieId : Int) : LiveData<Movie>{
        EspressoIdlingResource.increment()
        val movie = MutableLiveData<Movie>()
        api.getDetailMovie(movie_id = movieId, language = "en-US").enqueue(object : retrofit2.Callback<Movie>{
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.e("errorgetdara",t.message.toString())
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                EspressoIdlingResource.decrement()
                if (response.isSuccessful){
                    movie.postValue(response.body())
                }
            }
        })
        return movie
    }

    fun getAllTvShow() : LiveData<List<com.example.jetpackpro.data.tvshowentity.Result>>{
        EspressoIdlingResource.increment()
        val list = MutableLiveData<List<com.example.jetpackpro.data.tvshowentity.Result>>()
        api.getPopularTV(page = 1, language = "en-US").enqueue(object : retrofit2.Callback<TvShowsEntity>{
            override fun onFailure(call: Call<TvShowsEntity>, t: Throwable) {
                Log.e("errorgetdara",t.message.toString())
            }

            override fun onResponse(call: Call<TvShowsEntity>, response: Response<TvShowsEntity>) {
                EspressoIdlingResource.decrement()
                if (response.isSuccessful){
                    val listtvshow = response.body()?.results
                    list.postValue(listtvshow)
                }
            }

        })
       return list
    }

    fun getDetailTv(idTvShow : Int): LiveData<TvShow>{
        EspressoIdlingResource.increment()
        val tvShow = MutableLiveData<TvShow>()
        api.getDetailTV(tv_id = idTvShow, language = "en-US").enqueue(object : retrofit2.Callback<TvShow>{
            override fun onFailure(call: Call<TvShow>, t: Throwable) {
                Log.e("errorgetdara",t.message.toString())
            }

            override fun onResponse(call: Call<TvShow>, response: Response<TvShow>) {
                EspressoIdlingResource.decrement()
                if (response.isSuccessful){
                    Log.d("response API", response.body().toString())
                    tvShow.postValue(response.body())
                }
            }
        })

        return tvShow
    }
}