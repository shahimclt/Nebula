package com.example.nebula.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.nebula.R
import com.example.nebula.data.model.ImageObject
import com.example.nebula.databinding.ListImageItemBinding

class ImageListQuickAdapter(data: MutableList<ImageObject>?): BaseQuickAdapter<ImageObject, BaseDataBindingHolder<ListImageItemBinding>>(
    R.layout.list_image_item,data) {

    override fun convert(holder: BaseDataBindingHolder<ListImageItemBinding>, item: ImageObject) {
        holder.dataBinding?.apply {
            image = item
            executePendingBindings()
        }
        Glide.with(context)
            .load(item.url)
            .placeholder(R.drawable.bg_placeholder)
            .error(R.drawable.bg_placeholder)
            .into(holder.getView(R.id.list_image))
    }

    class DiffCallback: DiffUtil.ItemCallback<ImageObject>() {
        override fun areItemsTheSame(oldItem: ImageObject, newItem: ImageObject): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ImageObject, newItem: ImageObject): Boolean {
            return true
        }
    }

}