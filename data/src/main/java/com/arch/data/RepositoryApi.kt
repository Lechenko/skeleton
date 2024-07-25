package com.arch.data

import com.arch.featureremoteapi.IRemoteStorage
import com.arch.featureremoteapi.RemoteStorage
import com.arch.portdata.IRepositoryApi


class RepositoryApi : BaseRepApi(), IRepositoryApi {
    private val remoteStorage : IRemoteStorage   = RemoteStorage()

}