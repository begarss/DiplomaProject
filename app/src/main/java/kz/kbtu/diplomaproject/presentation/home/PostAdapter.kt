package kz.kbtu.diplomaproject.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.banner.BannerDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTOItem
import kz.kbtu.diplomaproject.databinding.ItemAdBinding
import kz.kbtu.diplomaproject.databinding.ItemPostBinding
import kz.kbtu.diplomaproject.presentation.home.PostAdapter.PostViewHolder

class PostAdapter(
  private val items: ArrayList<OpportunityDTOItem>,
) : RecyclerView.Adapter<PostViewHolder>() {

  inner class PostViewHolder(val itemBinding: ItemPostBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: OpportunityDTOItem) {
      with(itemBinding) {
        ivCompany.load(item.company?.picture, placeholder = R.drawable.test_company_iv)
        tvTitle.text = item.title
        tvCategory.text = item.jobCategory?.title
        tvCompanyName.text = item.company?.name
        tvJobType.text = item.jobType
      }
    }
  }

  fun addAll(list: List<OpportunityDTOItem>) {
    items.clear()
    items.addAll(list)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bind(items[position])
  }

  override fun getItemCount(): Int = items.size

}