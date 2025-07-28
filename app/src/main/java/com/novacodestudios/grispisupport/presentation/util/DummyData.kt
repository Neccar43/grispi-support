package com.novacodestudios.grispisupport.presentation.util

import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.model.User
import com.novacodestudios.grispisupport.presentation.model.UserRole

val user1 = User("u1", "Ahmet Kuru", "ahmet@example.com", UserRole.END_USER)
val user2 = User("u2", "Mehmet Zeybek", "mehmet@zendesk.com", UserRole.AGENT)
val user3 = User("u3", "Ayşe Yılmaz", "ayse@zendesk.com", UserRole.AGENT)
val user4 = User("u4", "Zeynep Demir", "zeynep@example.com", UserRole.END_USER)
val user5 = User("u5", "Ali Sever", "ali@zendesk.com", UserRole.AGENT)

val userList = listOf(user1, user2, user3, user4, user5)

val dummyTicketList = listOf(
    Ticket(
        id = "t1",
        number = 1001,
        subject = "Ürün teslim edilmedi",
        requester = user1,
        assignee = user2,
        followers = listOf(user3, user5),
        tags = listOf("teslimat", "sipariş"),
        form = "Teslimat Sorunu",
        status = TicketStatus.IN_PROGRESS,
        createdAt = System.currentTimeMillis() - 3 * 86400000,
        updatedAt = System.currentTimeMillis() - 2 * 86400000
    ),
    Ticket(
        id = "t2",
        number = 1002,
        subject = "Yanlış ürün gönderildi",
        requester = user4,
        assignee = user3,
        followers = listOf(user2),
        tags = listOf("ürün", "yanlış"),
        form = "Sipariş Sorunu",
        status = TicketStatus.OPEN,
        createdAt = System.currentTimeMillis() - 2 * 86400000,
        updatedAt = System.currentTimeMillis() - 86400000
    ),
    Ticket(
        id = "t3",
        number = 1003,
        subject = "Fatura ulaşmadı",
        requester = user1,
        assignee = user5,
        followers = listOf(),
        tags = listOf("fatura", "email"),
        form = "Faturalama",
        status = TicketStatus.PENDING,
        createdAt = System.currentTimeMillis() - 4 * 86400000,
        updatedAt = System.currentTimeMillis() - 3 * 86400000
    ),
    Ticket(
        id = "t4",
        number = 1004,
        subject = "Destek çok yavaş",
        requester = user4,
        assignee = null,
        followers = listOf(user3, user2),
        tags = listOf("şikayet"),
        form = "Genel Destek",
        status = TicketStatus.ON_HOLD,
        createdAt = System.currentTimeMillis() - 86400000,
        updatedAt = System.currentTimeMillis() - 3600000
    ),
    Ticket(
        id = "t5",
        number = 1005,
        subject = "Sorun çözüldü, teşekkürler",
        requester = user1,
        assignee = user2,
        followers = listOf(user5),
        tags = listOf("teşekkür", "çözüldü"),
        form = "Geri Bildirim",
        status = TicketStatus.RESOLVED,
        createdAt = System.currentTimeMillis() - 7 * 86400000,
        updatedAt = System.currentTimeMillis() - 6 * 86400000
    )
)

val messagesT1 = listOf(
    Message(
        "m1",
        "t1",
        "u1",
        "Merhaba, siparişim hala elime ulaşmadı.",
        System.currentTimeMillis() - 3 * 86400000 + 3600000
    ),
    Message(
        "m2",
        "t1",
        "u2",
        "Merhaba Ahmet Bey, hemen kontrol ediyorum.",
        System.currentTimeMillis() - 3 * 86400000 + 7200000
    ),
    Message(
        "m3",
        "t1",
        "u2",
        "Kargo şirketiyle görüştüm, bugün teslim edilmesi bekleniyor.",
        System.currentTimeMillis() - 2 * 86400000 + 3600000
    ),
    Message(
        "m4",
        "t1",
        "u1",
        "Tamamdır. Halloldu.",
        System.currentTimeMillis() - 2 * 86400000 + 4600000
    ),
    Message("m5", "t1", "u1", "Teşekkürler.", System.currentTimeMillis() - 2 * 86400000 + 4700000),
    Message(
        "m6",
        "t1",
        "u2",
        "Ne demek. Biz teşekkür ederiz.",
        System.currentTimeMillis() - 2 * 86400000 + 5000000
    ),
    Message(
        "m7",
        "t1",
        "u2",
        "Bizi puanlamayı unutmayın.",
        System.currentTimeMillis() - 2 * 86400000 + 5100000
    ),
)
val messagesT2 = listOf(
    Message(
        "m4",
        "t2",
        "u4",
        "Merhaba, siparişimde yanlış ürün geldi.",
        System.currentTimeMillis() - 2 * 86400000 + 3600000
    ),
    Message(
        "m5",
        "t2",
        "u3",
        "Merhaba Zeynep Hanım, hangi ürünü bekliyordunuz?",
        System.currentTimeMillis() - 2 * 86400000 + 7200000
    ),
    Message(
        "m6",
        "t2",
        "u4",
        "Kırmızı tişört sipariş etmiştim, mavi geldi.",
        System.currentTimeMillis() - 86400000 + 3600000
    )
)
val messagesT3 = listOf(
    Message(
        "m7",
        "t3",
        "u1",
        "Merhaba, alışverişimden sonra e-fatura gelmedi.",
        System.currentTimeMillis() - 4 * 86400000 + 3600000
    ),
    Message(
        "m8",
        "t3",
        "u5",
        "Merhaba Ahmet Bey, e-faturayı sistem üzerinden yeniden gönderdim.",
        System.currentTimeMillis() - 3 * 86400000 + 7200000
    )
)
val messagesT4 = listOf(
    Message(
        "m9",
        "t4",
        "u4",
        "Merhaba, 2 gündür destek alamıyorum. Çok yavaşsınız.",
        System.currentTimeMillis() - 86400000 + 1800000
    ),
    Message(
        "m10",
        "t4",
        "u3",
        "Zeynep Hanım, gecikme için özür dileriz. Konuyu hemen inceliyoruz.",
        System.currentTimeMillis() - 86400000 + 5400000
    )
)
val messagesT5 = listOf(
    Message(
        "m11",
        "t5",
        "u1",
        "Merhaba, destek için teşekkür ederim. Sorun çözüldü.",
        System.currentTimeMillis() - 7 * 86400000 + 3600000
    ),
    Message(
        "m12",
        "t5",
        "u2",
        "Biz teşekkür ederiz Ahmet Bey. Yardımcı olabildiysek ne mutlu.",
        System.currentTimeMillis() - 6 * 86400000 + 3600000
    )
)
val dummyMessageList = messagesT1 + messagesT2 + messagesT3 + messagesT4 + messagesT5