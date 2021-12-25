package kz.kbtu.diplomaproject.presentation.home.promotion

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.BannerDTO
import kz.kbtu.diplomaproject.databinding.ItemAdBinding
import kz.kbtu.diplomaproject.presentation.home.promotion.PromotionAdapter.AdsViewHolder

class PromotionAdapter(
  private val items: ArrayList<BannerDTO>,
  private val onBannerClick: (image: String?) -> Unit,
  private val viewPager2: ViewPager2

) : RecyclerView.Adapter<AdsViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
    val binding = ItemAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AdsViewHolder(binding)
  }

  override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
    holder.bind(items[position])
  }

  override fun getItemCount(): Int = items.size

  fun addAll(list: List<BannerDTO>) {
    items.clear()
    items.addAll(list)
    notifyDataSetChanged()
  }

  private val runnable = Runnable {
    items.addAll(items)
    notifyDataSetChanged()
  }

  inner class AdsViewHolder(val binding: ItemAdBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BannerDTO) {
      Log.d("TAGA", "bind: ${item.image}")
      if (adapterPosition == items.size - 2) {
        viewPager2.post(runnable)
      }
      binding.promotionImage.load(item.image, placeholder = R.drawable.test_ad)
      itemView.setOnClickListener {
        onBannerClick.invoke(item.image)
      }
    }
  }
}