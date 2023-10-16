package com.example.kotlintask

import android.accounts.Account
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val medicineApi: MedicineApi
) : ViewModel() {

    val user = MutableLiveData<User?>()

    fun insertUser(username: String, password: String) {
        viewModelScope.launch {
            val id = accountDao.insert(User(username = username, password = password))
            user.value = accountDao.getUser(username)
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            user.value = accountDao.getUser(username)
        }
    }

    val medicines = MutableLiveData<List<Medicine>>()
    fun fetchMedicines() {
        viewModelScope.launch {
            val medicineList = medicineApi.getMedicines()  // Fetch the medicine data
            medicines.value = medicineList  // Update the LiveData
        }
    }
    fun getMedicine(medicineId: String?): LiveData<Medicine> {
        val medicineLiveData = MutableLiveData<Medicine>()
        // Assume you have a method to fetch medicine by id
        viewModelScope.launch {
            val medicine = fetchMedicineById(medicineId)
            medicineLiveData.value = medicine
        }
        return medicineLiveData
    }


}



