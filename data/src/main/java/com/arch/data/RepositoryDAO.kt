package com.arch.data

import android.content.Context
import com.arch.featurelocalstorage.Dao
import com.arch.featurelocalstorage.LocaleStorage
import com.arch.portdata.IRepositoryDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryDAO @Inject constructor(private val context: Context) : BaseDAO(),
    IRepositoryDAO {
    private val storage: Dao = LocaleStorage(context)


}