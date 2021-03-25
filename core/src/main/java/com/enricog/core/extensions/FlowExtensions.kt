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

fun <T> Flow<T>.chunked(maxSize: Int, timeMillis: Long): Flow<List<T>> =
    flow {
        coroutineScope {
            require(maxSize > 0)
            require(timeMillis > 0)

            val upstreamChannel = this@chunked
                .buffer(capacity = BUFFERED)
                .produceIn(this)

            while (isActive) { // this loop goes over each chunk
                val chunk = ArrayList<T>(maxSize) // current chunk

                try {
                    whileSelect {
                        // Start time if chunk is not empty
                        if (chunk.isNotEmpty()) {
                            onTimeout(timeMillis) {
                                false // stop when time run out
                            }
                        }
                        // Should be receiveOrClosed when boxing issues are fixed
                        upstreamChannel.onReceive {
                            if (it != null) {
                                chunk.add(it)
                            }
                            chunk.size < maxSize // stop if reached max size
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
