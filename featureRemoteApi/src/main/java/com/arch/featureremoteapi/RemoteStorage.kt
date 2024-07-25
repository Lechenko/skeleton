package com.arch.featureremoteapi


import com.arch.featureremoteapi.api.ApiService
import com.arch.featureremoteapi.api.HttpService



class RemoteStorage : IRemoteStorage{
    private val api : ApiService = HttpService().apiService

}