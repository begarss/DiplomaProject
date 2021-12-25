package kz.kbtu.diplomaproject.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.airba.infrastructure.helpers.load
import kz.airba.infrastructure.helpers.setOnClickListenerWithDebounce
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.databinding.ItemPostBinding
import kz.kbtu.diplomaproject.presentation.home.PostAdapter.PostViewHolder

class PostAdapter(
  private var items: ArrayList<OpportunityDTO>,
  private val onFavClick: (item: OpportunityDTO) -> Unit
) : RecyclerView.Adapter<PostViewHolder>() {

  inner class PostViewHolder(val itemBinding: ItemPostBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: OpportunityDTO) {
      with(itemBinding) {
        ivCompany.load(item.company?.picture, placeholder = R.drawable.test_company_iv)
        tvTitle.text = item.title
        tvCategory.text = item.jobCategory?.title
        tvCompanyName.text = item.company?.name
        tvJobType.text = item.jobType

        if (item.isFavourate == true) {
          ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill)
        } else {
          ivBookmark.setBackgroundResource(R.drawable.ic_bookmark)
        }

        ivBookmark.setOnClickListenerWithDebounce(debounceTime = 100) {
          onFavClick.invoke(item)
        }

      }
    }
  }

  fun addAll(list: List<OpportunityDTO>?) {
    val diffCallback = list?.let { OpportunityDiffCallback(items, it) }
    val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
    items = list as ArrayList<OpportunityDTO>
    diffResult?.dispatchUpdatesTo(this)
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

class OpportunityDiffCallback(
  private val oldList: List<OpportunityDTO>,
  private val newList: List<OpportunityDTO>
) : DiffUtil.Callback() {

  override fun getOldListSize(): Int = oldList.size
  override fun getNewListSize(): Int = newList.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldOpportunity = oldList[oldItemPosition]
    val newOpportunity = newList[newItemPosition]
    return oldOpportunity.id == newOpportunity.id
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldOpportunity = oldList[oldItemPosition]
    val newOpportunity = newList[newItemPosition]
    return oldOpportunity == newOpportunity
  }

}