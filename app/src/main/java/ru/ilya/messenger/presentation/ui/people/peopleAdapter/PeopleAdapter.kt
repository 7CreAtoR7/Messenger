package ru.ilya.messenger.presentation.ui.people.peopleAdapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ru.ilya.messenger.R
import ru.ilya.messenger.domain.entities.UserPeople

class PeopleAdapter : ListAdapter<UserPeople, PeopleAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.people_item,
            parent, false
        )
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.circle_bg)

        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .centerCrop()
            .circleCrop()
            .apply(requestOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    holder.userPhoto.background = AppCompatResources.getDrawable(
                        holder.itemView.context,
                        R.drawable.circle_bg
                    )
                    holder.userPhoto.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Очищаем ImageView если изображение не загружено
                    holder.userPhoto.background = null
                    holder.userPhoto.setImageDrawable(placeholder)
                }
            })

        holder.userName.text = user.fullName
        holder.userEmail.text = user.deliveryEmail
        when (user.status) {
            "active" -> holder.userStatus.setBackgroundResource(R.drawable.status_active)
            "idle" -> holder.userStatus.setBackgroundResource(R.drawable.status_idle)
            "offline" -> holder.userStatus.setBackgroundResource(R.drawable.status_offline)
            else -> holder.userStatus.setBackgroundResource(R.drawable.status_offline)
        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userPhoto: ImageView = view.findViewById(R.id.user_photo)
        val userName: TextView = view.findViewById(R.id.user_name)
        val userEmail: TextView = view.findViewById(R.id.user_email)
        val userStatus: View = view.findViewById(R.id.user_status)
    }


}