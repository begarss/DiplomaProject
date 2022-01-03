package kz.kbtu.diplomaproject.presentation.company

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.R.string
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.databinding.ItemCompanyBinding
import kz.kbtu.diplomaproject.presentation.company.CompanyAdapter.CompanyViewHolder
import kz.kbtu.diplomaproject.presentation.home.OpportunityDiffCallback

class CompanyAdapter(
  private val onFollowClick: (id: Int) -> Unit,
  private val onItemClick: (id: Int) -> Unit
) :
  RecyclerView.Adapter<CompanyViewHolder>() {
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

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
    val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CompanyViewHolder(binding)
  }

  override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
    holder.bind(item = items[position])
  }

  override fun getItemCount() = items.size
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