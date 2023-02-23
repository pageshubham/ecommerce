package com.ecommerce.android.fragments.containers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.ecommerce.android.adapter.BestDealAdapter
import com.ecommerce.android.adapter.BestProductAdapter
import com.ecommerce.android.adapter.SpecialProductAdapter
import com.ecommerce.android.databinding.FragmentHomeCategoryBinding
import com.ecommerce.android.databinding.FragmentProfileBinding
import com.ecommerce.android.util.Resource
import com.ecommerce.android.viewmodel.HomeCategoryViewModel
import com.ecommerce.android.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeCategoryFragment : Fragment() {

    private val TAG = "HomeCategoryFragment"

    private var _binding: FragmentHomeCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeCategoryViewModel by viewModels()
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestDealAdapter: BestDealAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        setupBestDealsRv()
        setupBestProductRv()

        //special product observer
        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        toastMessage(it.message)
                    }
                    else -> Unit
                }
            }
        }

        //best deal observer
        lifecycleScope.launchWhenStarted {
            viewModel.bestDealProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        toastMessage(it.message)
                    }
                    else -> Unit
                }
            }
        }

        //best product observer
        lifecycleScope.launchWhenStarted {
            viewModel.bestProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        bestProductAdapter.differ.submitList(it.data)
                        binding.bestProductProgressBar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding.bestProductProgressBar.visibility = View.GONE
                        toastMessage(it.message)
                    }
                    else -> Unit
                }
            }
        }

        binding.nestedScrollHomeCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v,_,scrollY,_,_ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchBestProducts()
            }
        })
    }

    private fun setupSpecialProductRv() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }
    }

    private fun setupBestProductRv() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2 , GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealAdapter = BestDealAdapter()
        binding.rvBestDealProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealAdapter
        }
    }

    private fun toastMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        binding.homeCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.homeCategoryProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}