package com.example.cleanapp

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.inOrder

open class Person {
    var name:String=""

    @Test
    fun checkName(){
        val mockedList = Mockito.mock(mutableListOf<Person>().javaClass)
        val mockedP = Mockito.mock(Person::class.java)
        mockedP.name = "1"
        val mockedP2 = Mockito.mock(Person::class.java)
        mockedP2.name = "2"
        mockedList.add(mockedP)
        mockedList.add(mockedP2)
        val inOrder = inOrder(mockedList)
        inOrder.verify(mockedList).add(mockedP2)
        inOrder.verify(mockedList).add(mockedP)
    }
}