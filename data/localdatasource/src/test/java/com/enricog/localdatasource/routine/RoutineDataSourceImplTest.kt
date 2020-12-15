package com.enricog.localdatasource.routine

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.localdatasource.TempoDatabase
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class RoutineDataSourceImplTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val database: TempoDatabase = mockk()


}