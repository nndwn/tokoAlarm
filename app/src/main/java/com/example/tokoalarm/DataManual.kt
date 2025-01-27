package com.example.tokoalarm

//Todo: Dibuat secara hardcode karena di api tidak tersedia
class DataManual {
    val banner :List<String> = listOf(
        "https://tokoalarm.com/promo/",
        "https://tokoalarm.com/informasi-update-apk/")

    val listPrice : List<Price> = listOf(
        Price(
            id = 1,
            price = 50000
        ),
        Price(
            id = 2,
            price = 30000
        ),
        Price(
            id = 3,
            price = 50000
        ),
        Price(
            id = 4,
            price = 100000
        ),
        Price(
            id = 5,
            price = 1000000
        )
    )

    val listBankAccount : List<BankAccount> = listOf(
        BankAccount(
            id = 1,
            name = "bank mandiri",
            numberRek = "1060012526581",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 2,
            name = "bank bni",
            numberRek = "1865753033",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 3,
            name = "bank bri",
            numberRek = "30101052706538",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 4,
            name = "bank cimb niaga",
            numberRek = "707895618400",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 5,
            name = "bank jago",
            numberRek = "507381262547",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 6,
            name = "sea bank",
            numberRek = "901167626190",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 7,
            name = "super bank",
            numberRek = "000000077305",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 8,
            name = "ovo",
            numberRek = "081264628242",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 9,
            name = "dana",
            numberRek = "081264628242",
            owner =  "Dani Saputra"
        ),
        BankAccount(
            id = 10,
            name = "gopay",
            numberRek = "081264628242",
            owner =  "Dani Saputra"
        )
    )

}