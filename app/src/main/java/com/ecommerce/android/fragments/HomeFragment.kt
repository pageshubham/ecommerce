package com.ecommerce.android.fragments

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecommerce.android.adapter.HomeViewPagerAdapter
import com.ecommerce.android.databinding.FragmentHomeBinding
import com.ecommerce.android.fragments.containers.*
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryFragments = arrayListOf(
            HomeCategoryFragment(),
            ChairCategoryFragment(),
            CupboardCategoryFragment(),
            TableCategoryFragment(),
            AccessoryCategoryFragment()
        )

        binding.viewPager.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewPagerAdapter(categoryFragments, childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Main"
                1 -> "Chair"
                2 -> "Cupboard"
                3 -> "Table"
                4 -> "Accessory"
                else -> { throw Resources.NotFoundException("Page not found")}
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}