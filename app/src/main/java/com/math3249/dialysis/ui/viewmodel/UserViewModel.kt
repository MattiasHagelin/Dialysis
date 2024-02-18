package com.math3249.dialysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.data.repository.repository_interface.IUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: IUser
): ViewModel() {

    private val _groupKey = MutableStateFlow("")
    val groupKey = _groupKey.value

    init {
        getGroupId()
    }
    private fun getGroupId() {
        viewModelScope.launch {
            repository.getGroupKey(BaseApp.userData.userId).collect { result ->
                when {
                    result.isSuccess -> {
                        if (result.getOrNull() != null) {
                            _groupKey.value = result.getOrNull()!!
                        } else
                            _groupKey.value = ""
                    }
                    result.isFailure -> {
                    }
                }
            }
        }
    }

   suspend fun createNewGroup(user: String) {
            repository.createNewGroup(user)
    }
}