package com.enricog.base_android.extensions

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope

val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

fun <T : Fragment> T.applyArguments(vararg pairs: Pair<String, Any?>): T {
    this.arguments = bundleOf(*pairs)
    return this
}