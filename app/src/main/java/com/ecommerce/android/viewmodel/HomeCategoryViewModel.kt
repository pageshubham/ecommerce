package com.ecommerce.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.android.data.Product
import com.ecommerce.android.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeCategoryViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {

    private val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProduct = _specialProduct.asStateFlow()

    private val _bestDealProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealProduct = _bestDealProduct.asStateFlow()

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct = _bestProduct.asStateFlow()

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProduct()
        fetchBestDeals()
        fetchBestProducts()
    }

    fun fetchSpecialProduct() {
        viewModelScope.launch {
            _specialProduct.emit(Resource.Loading())
        }
        db.collection("Products").whereEqualTo("category", "Special Product").get()
            .addOnSuccessListener {
                val specialProductList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProduct.emit(Resource.Success(specialProductList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDealProduct.emit(Resource.Loading())
        }
        db.collection("Products").whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener {
                val bestDealList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProduct.emit(Resource.Success(bestDealList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestDealProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProduct.emit(Resource.Loading())
                db.collection("Products").limit(pagingInfo.bestProductsPage * 10).get()
                    .addOnSuccessListener {
                        val bestProductList = it.toObjects(Product::class.java)
                        pagingInfo.isPagingEnd = bestProduct == pagingInfo.oldBestProducts
                        pagingInfo.oldBestProducts = bestProductList
                        viewModelScope.launch {
                            _bestProduct.emit(Resource.Success(bestProductList))
                        }
                        pagingInfo.bestProductsPage++
                    }
                    .addOnFailureListener {
                        viewModelScope.launch {
                            _bestProduct.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        }
    }
}

internal data class PagingInfo(
    var bestProductsPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)