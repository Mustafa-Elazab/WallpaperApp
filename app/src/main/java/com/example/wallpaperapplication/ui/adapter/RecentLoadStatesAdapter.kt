import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapplication.data.repository.EMPTY_LIST
import com.example.wallpaperapplication.databinding.RecentLoadStatesBinding


class RecentLoadStatesAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RecentLoadStatesAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder(
            RecentLoadStatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(var binding: RecentLoadStatesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryLoading.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) = with(itemView) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.layoutError.isVisible =
                loadState is LoadState.Error && loadState.error.message != EMPTY_LIST

        }
    }
}