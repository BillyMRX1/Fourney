package com.bearbrand.fourney.utils

import com.bearbrand.fourney.model.LeaderboardModel

object DummyLeaderBoard {
    fun generateDummy(): ArrayList<LeaderboardModel> {
        val listItem = ArrayList<LeaderboardModel>()
        listItem.add(
            LeaderboardModel(
                1,
                "https://i.ibb.co/rt42VS9/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash.png",
                "Annette Black",
                2450
                )
        )
        listItem.add(
            LeaderboardModel(
                2,
                "https://i.ibb.co/w4ttQXV/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash-1.png",
                "Esther Howard",
                2400
            )
        )
        listItem.add(
            LeaderboardModel(
                3,
                "https://i.ibb.co/Ks3fVcY/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash-2.png",
                "Eleanor Pena",
                2350
            )
        )
        listItem.add(
            LeaderboardModel(
                4,
                "https://i.ibb.co/wMXh97Y/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash-3.png",
                "Jane Cooper",
                2300
            )
        )
        listItem.add(
            LeaderboardModel(
                5,
                "https://i.ibb.co/NS3QJst/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash-4.png",
                "Bessie Cooper",
                2450
            )
        )
        listItem.add(
            LeaderboardModel(
                6,
                "https://i.ibb.co/cCTPm9P/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash-5.png",
                "Dianne Russell",
                2400
            )
        )
        listItem.add(
            LeaderboardModel(
                7,
                "https://i.ibb.co/cCTPm9P/Unsplash-Avatars-0004s-0006-tyler-nix-DM28ml-R5-Bs-unsplash-5.png",
                "Anisa",
                2350
            )
        )
        listItem.add(
            LeaderboardModel(
                8,
                "",
                "Dianasan",
                2300
            )
        )
        listItem.add(
            LeaderboardModel(
                9,
                "",
                "Russella",
                2250
            )
        )
        listItem.add(
            LeaderboardModel(
                10,
                "",
                "Xaviersion",
                2200
            )
        )
        listItem.add(
            LeaderboardModel(
                11,
                "",
                "Xaviersion",
                2200
            )
        )
        listItem.add(
            LeaderboardModel(
                12,
                "",
                "Xaviersion",
                2200
            )
        )
        listItem.add(
            LeaderboardModel(
                13,
                "",
                "Xaviersion",
                2200
            )
        )
        return listItem
    }
}