package kz.kbtu.diplomaproject.presentation.company

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.databinding.ItemCompanyBinding
import kz.kbtu.diplomaproject.presentation.company.CompanyAdapter.CompanyViewHolder

class CompanyAdapter(
  private val onFollowClick: (id: Int) -> Unit,
  private val onItemClick: (id: Int) -> Unit
) :
  RecyclerView.Adapter<CompanyViewHolder>() {
  private val items: ArrayList<Company> = arrayListOf()

  fun addAll(newItems: List<Company>?) {
    items.clear()
    if (newItems != null) {
      items.addAll(newItems)
    }
    notifyDataSetChanged()
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