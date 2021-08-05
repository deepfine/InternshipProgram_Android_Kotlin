package mjc.woo.internprojectkotlin.viewmodel.userdetail

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class UserDetailViewModel {
    companion object {
        @JvmStatic
        @BindingAdapter("imgSrc")
        fun changeFavoriteImage(view: ImageView, srcId: Int) {
            view.setImageResource(srcId)
        }
    }
}