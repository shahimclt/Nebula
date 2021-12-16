package com.example.nebula.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.example.nebula.R
import com.example.nebula.data.model.ImageObject
import com.example.nebula.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private val listViewModel: ListViewModel by activityViewModels()

    private var _binding: FragmentListBinding? = null

    private lateinit var mAdapter: ImageListQuickAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        _binding?.viewModel = listViewModel

        init()
        observe()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        initAdapter()
    }

    private fun observe() {
        listViewModel.images.observe(viewLifecycleOwner) {
            mAdapter.setDiffNewData(it.toMutableList())
            binding.recyclerView.apply {
                if (adapter != mAdapter) {
                    adapter = mAdapter
                }
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            setHasFixedSize(false)
        }

        mAdapter = ImageListQuickAdapter(mutableListOf())

        mAdapter.apply {
            animationEnable = true
            adapterAnimation = SlideInBottomAnimation()
            isAnimationFirstOnly = true
//            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            val eds = layoutInflater.inflate(R.layout.list_image_eds,binding.recyclerView,false)
            setEmptyView(eds)

            setDiffCallback(ImageListQuickAdapter.DiffCallback())

            setOnItemClickListener { adapter, view, position ->
                val image = adapter.getItem(position) as ImageObject
//                val action = BookshelfFragmentDirections.actionNavigationBookshelfToBookDetailFragment(book.id,book.title,book.author,book.coverImage)
//                val coverView = view.findViewById(R.id.book_cover) as View
//                val titleView = view.findViewById(R.id.bookName) as View
//                val extras = FragmentNavigator.Extras.Builder()
//                    .addSharedElements(
//                        mapOf(
//                            coverView to coverView.transitionName
////                            titleView to titleView.transitionName
//                        )
//                    ).build()
//                findNavController().navigate(action,extras)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}