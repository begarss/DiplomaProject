package kz.kbtu.diplomaproject.presentation.explore.filter.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterInfo(
  val jobCategory: Int?,
  val jobType: String?,
  val contractType: String?,
  val company: Int?
) : Parcelable
