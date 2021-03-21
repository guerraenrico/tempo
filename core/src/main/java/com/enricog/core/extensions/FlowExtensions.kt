package com.enricog.core.extensions

import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.selects.whileSelect

fun <T> Flow<T>.chunked(timeMillis: Long): Flow<List<T>> =
    flow {
        coroutineScope {
            val upstreamChannel = this@chunked
                .buffer(capacity = BUFFERED)
                .produceIn(this)

            while (isActive) { // this loop goes over each chunk
                val chunk = mutableListOf<T>() // current chunk

                try {
                    whileSelect {
                        onTimeout(timeMillis) {
                            false // stop when time run out
                        }
                        upstreamChannel.onReceive {
                            if (it != null) {
                                chunk.add(it)
                            }
                            true // continue always
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                    return@coroutineScope // that is normal exception when the source channel is over -- just stop
                } finally {
                    if (chunk.isNotEmpty()) {
                        emit(chunk.toList()) // send non-empty chunk on exit from whileSelect
                    }
                }
            }
        }
    }
