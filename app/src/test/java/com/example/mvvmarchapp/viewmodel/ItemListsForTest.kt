package com.example.mvvmarchapp.viewmodel

import com.example.mvvmarchapp.model.Item

object ItemListsForTest {

    val emptyList = arrayListOf<Item>()

    val oneItemA = arrayListOf(
        Item(
            "Same day shipping",
            "Item 1",
            "₹ 200",
            ""
        )
    )

    val oneItemB = arrayListOf(
        Item(
            null,
            "Item 2",
            "₹ 100",
            ""
        )
    )

    val twoItems = arrayListOf(
        Item(
            "Same day shipping",
            "Item 1",
            "₹ 200",
            ""
        ),
        Item(
            null,
            "Item 2",
            "₹ 300",
            ""
        )
    )

    val threeItems = arrayListOf(
        Item(
            "Same day shipping",
            "Item 1",
            "₹ 200",
            ""
        ),
        Item(
            null,
            "Item 2",
            "₹ 300",
            ""
        ),
        Item(
            "Same day shipping",
            "Item 2",
            "₹ 300",
            ""
        )
    )

    val fiveItems = arrayListOf(
        Item(
            "Same day shipping",
            "Item 1",
            "₹ 200",
            ""
        ),
        Item(
            null,
            "Item 2",
            "₹ 300",
            ""
        ),
        Item(
            "Same day shipping",
            "Item 3",
            "₹ 300",
            ""
        ),
        Item(
            null,
            "Item 3",
            "₹ 190",
            ""
        ),
        Item(
            "Same day shipping",
            "Item 5",
            "₹ 70",
            ""
        )
    )

}