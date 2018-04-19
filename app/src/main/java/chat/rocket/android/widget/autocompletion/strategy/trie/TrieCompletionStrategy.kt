package com.goalify.chat.android.widget.autocompletion.strategy.trie

import com.goalify.chat.android.widget.autocompletion.model.SuggestionModel
import com.goalify.chat.android.widget.autocompletion.strategy.CompletionStrategy
import com.goalify.chat.android.widget.autocompletion.strategy.trie.data.Trie

class TrieCompletionStrategy : CompletionStrategy {
    private val items = mutableListOf<SuggestionModel>()
    private val trie = Trie()

    override fun getItem(prefix: String, position: Int): SuggestionModel {
        val item: SuggestionModel
        if (prefix.isEmpty()) {
            item = items[position]
        } else {
            item = autocompleteItems(prefix)[position]
        }
        return item
    }

    override fun autocompleteItems(prefix: String) = trie.autocompleteItems(prefix)

    override fun addAll(list: List<SuggestionModel>) {
        items.addAll(list)
        list.forEach {
            trie.insert(it)
        }
    }

    override fun addPinned(list: List<SuggestionModel>) {

    }

    override fun size() = items.size
}