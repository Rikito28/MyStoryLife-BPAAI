package com.riski.mystorylife.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.riski.mystorylife.databinding.ItemLoadingstateBinding

class LoadStateAdapter(private val retry: () -> Unit): androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadingStateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = ItemLoadingstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


class LoadingStateViewHolder(private val binding: ItemLoadingstateBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.btnTry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.texterror.text = loadState.error.localizedMessage
        }
        binding.texterror.isVisible = loadState is LoadState.Error
        binding.loading.isVisible = loadState is LoadState.Loading
        binding.btnTry.isVisible  = loadState is LoadState.Error

    }
}}