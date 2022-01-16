package kz.kbtu.diplomaproject.presentation.explore.filter.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class FilterInfo(
  var title: String?,
  val jobCategory: Int?,
  val jobType: String?,
  val contractType: String?,
  val company: Int?,
  val savedChips: ArrayList<ChipIds>?
) : Parcelable

@Parcelize
data class ChipIds(
  val groupType: FilterGroupType,
  val groupId: Int,
  val checkedId: Int
) : Parcelable

enum class FilterGroupType {
  CATEGORY,
  TYPE,
  CONTRACT,
  COMPANY
}
