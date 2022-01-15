package kz.kbtu.diplomaproject.presentation.company

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.R.string
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.databinding.ItemCompanyBinding
import kz.kbtu.diplomaproject.databinding.ItemFollowedCompanyBinding
import kz.kbtu.diplomaproject.presentation.company.CompanyHolder.FOLLOWED
import kz.kbtu.diplomaproject.presentation.company.CompanyHolder.REGULAR

class CompanyAdapter(
  private val type: CompanyHolder,
  private val onFollowClick: (id: Int) -> Unit,
  private val onItemClick: (id: Int) -> Unit
) :
  RecyclerView.Adapter<ViewHolder>() {
  private var items: ArrayList<Company> = arrayListOf()

  fun addAll(newItems: List<Company>?) {
    val diffCallback = newItems?.let { CompanyDiffCallback(items, it) }
    val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
    items = newItems as ArrayList<Company>
    diffResult?.dispatchUpdatesTo(this)
  }

  inner class CompanyViewHolder(val binding: ItemCompanyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Company) {
      with(binding) {
        tvTitle.text = item.name
        ivCompany.load(item.picture)
        btnFollow.setOnClickListener {
          item.id?.let { it1 -> onFollowClick.invoke(it1) }
        }
        itemView.setOnClickListener {
          item.id?.let(onItemClick)
        }
        if (item.isSubscribed == true) {
          btnFollow.apply {
            text = context.getString(string.btn_unfollow)
            setBackgroundResource(R.drawable.button_background_colored_disabled)
          }
        } else {
          btnFollow.apply {
            text = context.getString(string.btn_follow)
            setBackgroundResource(R.drawable.button_background_default)
          }
        }
      }
    }
  }

  inner class FollowedCompanyViewHolder(val binding: ItemFollowedCompanyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Company) {
      with(binding) {
        tvCompanyName.text = item.name
        ivCompany.load(item.picture)
        itemView.setOnClickListener {
          item.id?.let(onItemClick)
        }
        tvAboutCompany.text = item.aboutCompany
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return when (viewType) {
      REGULAR_VIEW -> {
        val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        CompanyViewHolder(binding)
      }
      FOLLOWED_VIEW -> {
        val binding =
          ItemFollowedCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowedCompanyViewHolder(binding)
      }
      else -> {
        val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        CompanyViewHolder(binding)
      }
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when (holder) {
      is CompanyViewHolder -> {
        holder.bind(item = items[position])
      }
      is FollowedCompanyViewHolder -> {
        holder.bind(item = items[position])
      }
    }
  }

  override fun getItemCount() = items.size

  override fun getItemViewType(position: Int): Int {
    return when (type) {
      REGULAR -> REGULAR_VIEW
      FOLLOWED -> FOLLOWED_VIEW
    }
  }

  companion object {
    private const val REGULAR_VIEW = 1
    private const val FOLLOWED_VIEW = 2
  }
}

class CompanyDiffCallback(
  private val oldList: List<Company>,
  private val newList: List<Company>
) : DiffUtil.Callback() {

  override fun getOldListSize(): Int = oldList.size
  override fun getNewListSize(): Int = newList.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldCompany = oldList[oldItemPosition]
    val newCompany = newList[newItemPosition]
    return oldCompany.id == newCompany.id
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldCompany = oldList[oldItemPosition]
    val newCompany = newList[newItemPosition]
    return oldCompany == newCompany
  }
}

enum class CompanyHolder {
  REGULAR,
  FOLLOWED
}