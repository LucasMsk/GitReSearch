package com.codeadd.gitresearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Detail(val fullName: String, val avatarUrl: String, val repoUrl: String, val starCount: Int = 0): Parcelable