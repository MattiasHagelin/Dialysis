package com.math3249.dialysis.other

import com.math3249.dialysis.data.model.GroupMember

interface IGroupMemberCallback {
    fun onCallback(value: GroupMember)
}