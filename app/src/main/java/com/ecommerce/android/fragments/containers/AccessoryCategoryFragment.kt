package com.ecommerce.android.fragments.containers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ecommerce.android.databinding.FragmentAccessoryCategoryBinding
import com.ecommerce.android.databinding.FragmentProfileBinding
import com.ecommerce.android.viewmodel.RegisterViewModel

class AccessoryCategoryFragment : Fragment() {

    private var _binding: FragmentAccessoryCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccessoryCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}